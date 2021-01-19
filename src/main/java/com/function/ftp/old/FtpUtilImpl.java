package com.function.ftp.old;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.io.CopyStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FtpUtilImpl implements FtpUtil {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String serverIp = "localhost";

	private int port = 8090;

	private String username;

	private String password;

	private String encoding = "utf8";

	private String tmpDir;

	private int retryCount;

	private String mode;

	public FtpUtilImpl() {
	}

	public FtpUtilImpl(String serverIp, int port, String username, String password, String encoding, int retryCount,
			String mode) {
		this.serverIp = serverIp;
		this.port = port;
		this.username = username;
		this.password = password;
		this.encoding = encoding;
		this.retryCount = retryCount;
		this.mode = mode;
	}

	@Override
	public FTPClient connectFtpClient() throws FtpException {
		FTPClient ftpClient = null;
		int i = 1;
		while (ftpClient == null && i <= retryCount) {
			try {
				ftpClient = createConnection();
				log.info("连接FTP服务器" + serverIp + ":" + port + "成功");
			} catch (FtpException e) {
				log.error("第" + i + "次连接FTP服务器" + serverIp + ":" + port + "失败，原因：" + e.getMessage(), e);
				i++;
			}
		}
		if (i > retryCount) {
			throw new FtpException("尝试" + retryCount + "次连接FTP服务器" + serverIp + ":" + port + "不成功");
		} else {
			return ftpClient;
		}
	}

	/**
	 * @ClassName: FtpUtilImpl.java
	 * @Description: 连接FTP服务器
	 * @CreateDate:2017-6-14 上午9:11:02
	 * @return
	 * @throws FtpException
	 */
	private FTPClient createConnection() throws FtpException {
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(encoding);
			ftpClient.connect(serverIp, port);
			ftpClient.login(username, password);
			int replay = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replay)) {
				throw new FtpException("connection error,replay for:" + replay);
			}
			if (this.mode != null && "PASSVIE".equals(this.mode.trim())) {
				ftpClient.enterLocalPassiveMode();
			} else {
				ftpClient.enterLocalActiveMode();
			}
			
			// 配置上传下载速度
			ftpClient.setSendBufferSize(DEFAULT_BUFFER_SIZE);
			ftpClient.setBufferSize(DEFAULT_BUFFER_SIZE);
			return ftpClient;
		} catch (Exception e) {
			closeFtpClient(ftpClient);
			throw new FtpException(e.getMessage(), e);
		}
	}

	@Override
	public void closeFtpClient(FTPClient client) {
		if (client != null) {
			if (client.isConnected()) {
				try {
					client.disconnect();
				} catch (IOException e) {
					log.error("关闭连接FTP服务器" + serverIp + ":" + port + "失败", e);
				}
			}
			client = null;
		}
	}

	@Override
	public File downLoadFile(String dir, String fileName) throws FtpException {
		return downLoadFile(dir, fileName, getTmpDir());
	}

	@Override
	public File downLoadFile(String dir, String fileName, String localPath) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return downLoadFile(ftpClient, dir, fileName, localPath);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public File downLoadFile(FTPClient ftpClient, String dir, String fileName, String localPath) throws FtpException {
		if (!isFileExists(ftpClient, dir, fileName)) {
			throw new FtpException("目录" + dir + "不存在文件" + fileName);
		}
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ftpClient.setDefaultTimeout(1000 * 60 * 60 * 1);
		ftpClient.setDataTimeout(1000 * 60 * 60 * 1);
		ftpClient.setConnectTimeout(1000 * 60 * 60 * 1);

		FileOutputStream fos = null;
		try {
			ftpClient.changeWorkingDirectory(dir);

			File localDir = new File(localPath);
			if (!localDir.exists()) {
				localDir.mkdirs();
			}

			// 判断本地是否存在同名文件
			File localFile = new File(localDir, fileName);
			if (localFile.exists()) {
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile ftpFile : ftpFiles) {
					if (ftpFile.getName().equals(fileName)) {
						// 判断修改时间是否一致
						if (ftpFile.getTimestamp().getTimeInMillis() == localFile.lastModified()) {
							return localFile;
						}
					}
				}
			}

			// 下载文件
			fos = new FileOutputStream(localFile);
			boolean download = ftpClient.retrieveFile(fileName, fos);
			if (download) {
				return localFile;
			} else {
				throw new FtpException("文件" + dir + "/" + fileName + "下载失败");
			}
		} catch (Exception e) {
			log.error("文件" + dir + "/" + fileName + "下载失败", e);
			throw new FtpException("文件" + dir + "/" + fileName + "下载失败", e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public String uploadFile(String path, File file, boolean isCover) throws FtpException {
		return uploadFile(path, file, file.getName(), isCover);
	}

	@Override
	public List<String> uploadFile(String path, List<File> fileList, boolean isCover) throws FtpException {
		List<String> successList = Lists.newArrayList();
		FTPClient ftpClient = connectFtpClient();
		for (File file : fileList) {
			try {
				successList.add(uploadFile(ftpClient, path, file, file.getName(), isCover));
			} catch (FtpException e) {
				log.error("文件" + file.getAbsolutePath() + "上传失败，停止上传", e);
				break;
			} finally {
				closeFtpClient(ftpClient);
			}
		}
		return successList;
	}

	@Override
	public String uploadFile(String path, File file, String fileName, boolean isCover) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return uploadFile(ftpClient, path, file, fileName, isCover);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public String uploadFile(FTPClient ftpClient, String path, File file, String fileName, boolean isCover)
			throws FtpException {
		FileInputStream fis = null;
		try {
			makeDirectory(ftpClient, path);
			log.info("当前工作目录为：" + ftpClient.printWorkingDirectory());
			if ("ASCII".equalsIgnoreCase((encoding.trim()))) {
				ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
			} else {
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			}
			fis = new FileInputStream(file);
			if (StringUtils.isBlank(fileName)) {
				fileName = file.getName();
			}
			String ftpName = null;

			if (isCover) {
				ftpName = fileName;
			} else {
				int num = 0;
				if (isFileExists(ftpClient, path, fileName)) {
					int index = fileName.lastIndexOf(".");
					final Pattern pattern = Pattern
							.compile(fileName.substring(0, index) + "\\((\\d+)\\)(" + fileName.substring(index) + ")");
					final List<Integer> numList = new ArrayList<Integer>();
					ftpClient.listFiles(path, new FTPFileFilter() {

						@Override
						public boolean accept(FTPFile file) {
							Matcher matcher = pattern.matcher(file.getName());
							if (matcher.find()) {
								numList.add(Integer.valueOf(matcher.group(1)));
								return true;
							}
							return false;
						}
					});
					if (!CollectionUtils.isEmpty(numList)) {
						Collections.sort(numList);
						num = numList.get(numList.size() - 1);
					}
					num++;
					ftpName = fileName.substring(0, index) + "(" + num + ")" + fileName.substring(index);
				} else {
					ftpName = fileName;
				}
				if (isFileExists(ftpClient, path, ftpName)) {// 避免两个应用上传同一个FTP服务器目录造成上传失败
					num++;
					int index = fileName.lastIndexOf(".");
					ftpName = fileName.substring(0, index) + "(" + num + ")" + fileName.substring(index);
				}
			}
			boolean upload = ftpClient.storeFile(ftpName, fis);
			if (upload) {
				log.info("文件" + file.getAbsolutePath() + "上传成功，已保存到FTP服务器的目录" + path + "下");
				return ftpName;
			} else {
				throw new FtpException("文件" + file.getAbsolutePath() + "上传失败");
			}
		} catch (CopyStreamException e) {
			log.error("文件" + file.getAbsolutePath() + "上传失败，原因：" + e.getMessage() + "，已传输大小："
					+ e.getTotalBytesTransferred(), e.getIOException());
			throw new FtpException("文件" + file.getAbsolutePath() + "上传失败", e);
		} catch (Exception e) {
			log.error("文件" + file.getAbsolutePath() + "上传失败", e);
			throw new FtpException("文件" + file.getAbsolutePath() + "上传失败", e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	@Override
	public void uploadZipFile(File file, String path) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			String tmpPath = getTmpDir() + file.getName();
			ZipUtil zipUtil = new ZipUtil();
			zipUtil.unZipFile(file, tmpPath);
			File uploadFile = new File(tmpPath);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			uploadDir(ftpClient, uploadFile, path);
			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
		} catch (Exception e) {
			throw new FtpException("上传失败", e);
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public void uploadDir(File dirFile, String path) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			uploadDir(ftpClient, dirFile, path);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public void uploadDir(FTPClient ftpClient, File dirFile, String path) throws FtpException {
		if (!path.equals("/")) {
			path += "/";
		}
		String dirPath = path + dirFile.getName();
		try {
			makeDirectory(ftpClient, dirPath);
		} catch (Exception e) {
			throw new FtpException(e);
		}
		File[] files = dirFile.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					uploadDir(ftpClient, file, dirPath);
				} else {
					uploadFile(ftpClient, dirPath, file, file.getName(), true);
				}
			}
		}
	}

	@Override
	public Map<String, FTPFile> listFiles(String path, boolean isRec) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return listFiles(ftpClient, path, isRec);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public Map<String, FTPFile> listFiles(FTPClient ftpClient, String path, boolean isRec) throws FtpException {
		Map<String, FTPFile> ftpFileMap = new LinkedHashMap<String, FTPFile>();
		if (!path.endsWith("/")) {
			path += "/";
		}
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(path);
			if (ftpFiles != null && ftpFiles.length > 0) {
				for (FTPFile ftpFile : ftpFiles) {
					String filePath = path + ftpFile.getName();
					if (ftpFile.isDirectory() && isRec) {
						ftpFileMap.putAll(listFiles(ftpClient, filePath, isRec));
					} else if (ftpFile.isFile()) {
						ftpFileMap.put(filePath, ftpFile);
					}
				}
			}
			return ftpFileMap;
		} catch (IOException e) {
			throw new FtpException("获取文件列表失败", e);
		}
	}

	public List<FTPFile> listFIles(String path, String fileFilter) throws FtpException {
		//Map<String, FTPFile> ftpFileMap = new LinkedHashMap<String, FTPFile>();
		List<FTPFile> ftpFileList = new ArrayList<>();
		FTPClient ftpClient = connectFtpClient();
		FTPFile[] files = new FTPFile[0];
		try {
			files = ftpClient.listFiles(path);
			for (FTPFile ftpFile : files) {
				String filePath = path + ftpFile.getName();
				if (ftpFile.isDirectory()) {
					listFIles(filePath,fileFilter);
					//ftpFileMap.put(ftpPath,ftpClient.listFiles());
				} else if (ftpFile.isFile()) {
					if(fileFilter == null ||ftpFile.getName().indexOf(fileFilter) >= 0) {
						ftpFileList.add(ftpFile);
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpFileList;
	}

	@Override
	public Map<String, FTPFile> listDirectorys(String path, boolean isRec) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return listDirectorys(ftpClient, path, isRec);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public Map<String, FTPFile> listDirectorys(FTPClient ftpClient, String path, boolean isRec) throws FtpException {
		Map<String, FTPFile> ftpFileMap = new LinkedHashMap<String, FTPFile>();
		if (!path.endsWith("/")) {
			path += "/";
		}
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(path, new FTPFileFilter() {

				@Override
				public boolean accept(FTPFile file) {
					return file.isDirectory();
				}
			});
			if (ftpFiles != null && ftpFiles.length > 0) {
				for (FTPFile ftpFile : ftpFiles) {
					String dirPath = path + ftpFile.getName();
					ftpFileMap.put(dirPath, ftpFile);
					if (isRec) {
						ftpFileMap.putAll(listDirectorys(ftpClient, dirPath, isRec));
					}
				}
			}
			return ftpFileMap;
		} catch (IOException e) {
			throw new FtpException("获取目录列表失败", e);
		}
	}

	@Override
	public Map<String, FTPFile> lists(String path, boolean isRec) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return lists(ftpClient, path, isRec);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public Map<String, FTPFile> lists(FTPClient ftpClient, String path, boolean isRec) throws FtpException {
		Map<String, FTPFile> ftpFileMap = new LinkedHashMap<String, FTPFile>();
		if (!path.endsWith("/")) {
			path += "/";
		}
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(path);
			if (ftpFiles != null && ftpFiles.length > 0) {
				for (FTPFile ftpFile : ftpFiles) {
					String dirPath = path + ftpFile.getName();
					if (ftpFile.isDirectory() && isRec) {
						ftpFileMap.putAll(lists(ftpClient, dirPath, isRec));
					}
					ftpFileMap.put(dirPath, ftpFile);
				}
			}
			return ftpFileMap;
		} catch (IOException e) {
			throw new FtpException("获取文件、目录列表失败", e);
		}
	}

	@Override
	public boolean renameFTPFile(String fromName, String toName) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		boolean result = false;
		try {
			result = ftpClient.rename(fromName, toName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		} finally {
			closeFtpClient(ftpClient);
		}
		return result;
	}

	@Override
	public boolean deleteFile(String path, String fileName) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return deleteFile(ftpClient, path, fileName);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public boolean deleteFile(FTPClient ftpClient, String path, String fileName) throws FtpException {
		if (!path.endsWith("/")) {
			path += "/";
		}
		try {
			boolean success = ftpClient.changeWorkingDirectory(path);
			if (success) {
				FTPFile[] ftpFiles = ftpClient.listFiles();
				if (ftpFiles != null && ftpFiles.length > 0) {
					for (FTPFile ftpFile : ftpFiles) {
						if (ftpFile.getName().equals(fileName)) {
							return ftpClient.deleteFile(fileName);
						}
					}
				}
				throw new FtpException("文件" + path + fileName + "不存在");
			} else {
				throw new FtpException("目录" + path + "不存在");
			}
		} catch (Exception e) {
			throw new FtpException("删除文件" + path + fileName + "失败", e);
		}
	}

	@Override
	public boolean cleanDirectory(String dirPath, boolean isDelDir) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return cleanDirectory(ftpClient, dirPath, isDelDir);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public boolean cleanDirectory(FTPClient ftpClient, String dirPath, boolean isDelDir) throws FtpException {
		try {
			if (!isFileExists(ftpClient, dirPath, null)) {
				boolean success = false;
				Map<String, FTPFile> ftpFileList = lists(ftpClient, dirPath, true);
				for (Entry<String, FTPFile> entry : ftpFileList.entrySet()) {
					String path = entry.getKey().substring(entry.getKey().lastIndexOf("/") + 1);
					if (entry.getValue().isFile()) {
						success = deleteFile(ftpClient, path, entry.getValue().getName());
					} else if (entry.getValue().isDirectory()) {
						success = cleanDirectory(ftpClient, entry.getKey(), true);
					}
					if (!success) {
						throw new FtpException("删除" + entry.getKey() + "失败");
					}
				}
				if (isDelDir) {
					ftpClient.changeWorkingDirectory(dirPath);
					if (dirPath.endsWith("/")) {
						dirPath = dirPath.substring(0, dirPath.length() - 1);
					}
					String dirName = dirPath.substring(dirPath.lastIndexOf("/") + 1);
					ftpClient.changeToParentDirectory();
					return ftpClient.removeDirectory(dirName);
				}
				return true;
			} else {
				throw new FtpException("目录" + dirPath + "不存在");
			}
		} catch (Exception e) {
			throw new FtpException("删除目录" + dirPath + "失败", e);
		}
	}

	@Override
	public boolean isDirExists(String dir) throws FtpException {
		return isFileExists(dir, null);
	}

	@Override
	public boolean isFileExists(String dir, String fileName) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		try {
			return isFileExists(ftpClient, dir, fileName);
		} catch (FtpException e) {
			throw e;
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	@Override
	public boolean isFileExists(FTPClient ftpClient, String dir, String fileName) throws FtpException {
		if (ftpClient == null || !ftpClient.isConnected()) {
			throw new FtpException("FTP连接已断开");
		}
		try {
			while (!ftpClient.changeWorkingDirectory(dir)) {
				ftpClient.changeToParentDirectory();
			}
			if (StringUtils.isNotBlank(fileName)) {
				FTPFile[] files = ftpClient.listFiles();
				if (files != null && files.length > 0) {
					for (FTPFile ftpFile : files) {
						if (ftpFile.isFile() && ftpFile.getName().equals(fileName)) {
							return true;
						}
					}
				}
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new FtpException(e);
		}
	}

	/**
	 * @ClassName: FtpUtilImpl.java
	 * @Description: 从根目录创建目录，支持多级创建，并进入该目录
	 * @CreateDate:2017-6-14 上午8:54:22
	 * @param ftpClient
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	private void makeDirectory(FTPClient ftpClient, String dir) throws Exception {
		ftpClient.changeWorkingDirectory("/");
		String[] dirs = dir.split("/");
		String str = "/";
		for (String string : dirs) {
			if (StringUtils.isNotBlank(string)) {
				string = string.trim();
				str += string + "/";
				if (!ftpClient.changeWorkingDirectory(string)) {
					if (!ftpClient.makeDirectory(string)) {
						throw new FtpException("目录" + str + "创建失败");
					} else {
						if (!ftpClient.changeWorkingDirectory(string)) {
							throw new FtpException("目录" + str + "进入失败");
						}
					}
				}
			}
		}
	}

	/**
	 * 是否是目录.
	 * 
	 * @param path
	 * @return
	 * @throws
	 */
	@Override
    public boolean isDirectory(String path) throws FtpException {
		FTPClient ftpClient = connectFtpClient();

		if (path.endsWith("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		final String fileName = path.substring(path.lastIndexOf("/") + 1);
		path = path.substring(0, path.lastIndexOf("/"));
		try {
			if (!this.isFileExists(path.substring(0, path.lastIndexOf("/")),
					path.substring(path.lastIndexOf("/") + 1))) {
				throw new FtpException("file not exits:" + path);
			}
			ftpClient.cwd("/");
			FTPFile[] files = ftpClient.listFiles(path, new FTPFileFilter() {// 文件过滤器

				@Override
				public boolean accept(FTPFile file) {
					return file.isDirectory() && file.getName().equals(fileName);
				}
			});
			return files.length != 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		} finally {
			closeFtpClient(ftpClient);
		}
	}

	/**
	 * 是否是文件.
	 * 
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	@Override
	public boolean isFile(String path) throws FtpException {
		FTPClient ftpClient = connectFtpClient();
		if (path.endsWith("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		path = path.substring(0, path.lastIndexOf("/"));
		boolean dir = false;
		try {
			ftpClient.cwd("/");
			ftpClient.cwd(path);
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile file : files) {
				if (file.getName().equals(fileName)) {
					if (file.isFile()) {
						return true;
					}
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		} finally {
			closeFtpClient(ftpClient);
		}
		return dir;
	}

	@Override
	public void initTmpDir(String path) {
		if (path == null) {
			path = System.getProperty("java.io.tmpdir");
		}
		if (!(new File(path).exists())) {
			new File(path).mkdirs();
		}
		this.tmpDir = path;
	}

	@Override
	public String getTmpDir() {
		if (StringUtils.isBlank(tmpDir)) {
			tmpDir = System.getProperty("java.io.tmpdir");
		}
		return tmpDir;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
