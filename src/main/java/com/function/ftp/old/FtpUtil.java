package com.function.ftp.old;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FtpUtil {

	/**
	 * 默认每次上传下载缓存大小
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 获取FTP连接
	 * @CreateDate:2017-6-14 上午10:08:47
	 * @return
	 * @throws FtpException
	 */
	public FTPClient connectFtpClient() throws FtpException, FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 关闭FTP连接
	 * @CreateDate:2017-6-14 上午10:09:39
	 * @param client
	 */
	public void closeFtpClient(FTPClient client);

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 下载一个文件到临时目录中
	 * @CreateDate:2017-6-14 上午11:43:19
	 * @param path
	 *            文件所在目录
	 * @param filename
	 *            文件名
	 * @return
	 * @throws FtpException
	 */
	public File downLoadFile(String path, String filename) throws FtpException, FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 下载一个文件到指定本地目录中
	 * @CreateDate:2017-6-14 上午11:46:00
	 * @param path
	 *            文件所在目录
	 * @param filename
	 *            文件名
	 * @param localPath
	 *            本地目录
	 * @return
	 * @throws FtpException
	 */
	public File downLoadFile(String path, String filename, String localPath) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 下载一个文件到指定本地目录中
	 * @CreateDate:2017-6-14 上午11:46:31
	 * @param ftpClient
	 *            FTPClient对象
	 * @param dir
	 *            文件所在目录
	 * @param fileName
	 *            文件名
	 * @param localPath
	 *            本地目录
	 * @return
	 * @throws FtpException
	 */
	public File downLoadFile(FTPClient ftpClient, String dir, String fileName, String localPath) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传一个文件到指定目录
	 * @CreateDate:2017-6-14 上午11:53:04
	 * @param path
	 *            指定目录
	 * @param file
	 *            文件对象
	 * @param isCover
	 *            当存在相同文件名时，是否覆盖，若不覆盖，文件会重命名，命名规则如:<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *            假设文件名原为test.txt，则重命名为test-yyyyMMddHHmmss:sss.txt。
	 * @return 返回上传后的名称
	 * @throws FtpException
	 */
	public String uploadFile(String path, File file, boolean isCover) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传多个文件到指定目录
	 * @CreateDate:2017年12月27日 下午3:40:43
	 * @param path
	 *            指定目录
	 * @param fileList
	 *            文件对象集合
	 * @param isCover
	 *            当存在相同文件名时，是否覆盖，若不覆盖，文件会重命名，命名规则如:<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *            假设文件名原为test.txt，则重命名为test-yyyyMMddHHmmss:sss.txt。
	 * @return
	 * @throws FtpException
	 */
	public List<String> uploadFile(String path, List<File> fileList, boolean isCover) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传一个文件到指定目录并重命名
	 * @CreateDate:2017-6-14 下午3:15:10
	 * @param path
	 *            指定目录
	 * @param file
	 *            文件对象
	 * @param fileName
	 *            重命名
	 * @param isCover
	 *            当存在相同文件名时，是否覆盖，若不覆盖，文件名fileName会重命名，命名规则如:<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *            假设文件名原为test.txt，则重命名为test-yyyyMMddHHmmss:sss.txt。
	 * @return 返回上传后的名称
	 * @throws FtpException
	 */
	public String uploadFile(String path, File file, String fileName, boolean isCover) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传一个文件到指定目录
	 * @CreateDate:2017-6-14 下午2:18:28
	 * @param ftpClient
	 *            FTPClient对象
	 * @param path
	 *            指定目录
	 * @param file
	 *            文件对象
	 * @param fileName
	 *            重命名，为空时默认为file的文件名
	 * @param isCover
	 *            当存在相同文件名时，是否覆盖，若不覆盖，文件名fileName会重命名，命名规则如:<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *            假设文件名原为test.txt，则重命名为test-yyyyMMddHHmmss:sss.txt。
	 * @return 返回上传后的名称
	 * @throws FtpException
	 */
	public String uploadFile(FTPClient ftpClient, String path, File file, String fileName, boolean isCover)
			throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 先解压到临时文件夹，再上传到指定目录
	 * @CreateDate:2017-6-14 下午3:52:13
	 * @param file
	 *            压缩文件对象
	 * @param path
	 *            指定目录
	 * @throws FtpException
	 */
	public void uploadZipFile(File file, String path) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传一个目录到指定目录
	 * @CreateDate:2017-6-14 下午3:24:32
	 * @param dirFile
	 *            目录对象
	 * @param path
	 *            指定目录
	 * @throws FtpException
	 */
	public void uploadDir(File dirFile, String path) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 上传一个目录到指定目录
	 * @CreateDate:2017-6-14 下午3:24:32
	 * @param ftpClient
	 *            FTPClient对象
	 * @param dirFile
	 *            目录对象
	 * @param path
	 *            指定目录
	 * @throws FtpException
	 */
	public void uploadDir(FTPClient ftpClient, File dirFile, String path) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有文件
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> listFiles(String path, boolean isRec) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有文件
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param ftpClient
	 *            FTPClient对象
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> listFiles(FTPClient ftpClient, String path, boolean isRec) throws FtpException;

	public List<FTPFile> listFIles(String path, String fileFilter) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有目录
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> listDirectorys(String path, boolean isRec) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有目录
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param ftpClient
	 *            FTPClient对象
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> listDirectorys(FTPClient ftpClient, String path, boolean isRec) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有文件和目录
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> lists(String path, boolean isRec) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 遍历获取指定目录下的所有文件和目录
	 * @CreateDate:2017-6-14 下午4:00:13
	 * @param ftpClient
	 *            FTPClient对象
	 * @param path
	 *            指定目录
	 * @param isRec
	 *            是否递归
	 * @return 返回文件Map，其中key为文件路径，value为FTPFile对象
	 * @throws FtpException
	 */
	public Map<String, FTPFile> lists(FTPClient ftpClient, String path, boolean isRec) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 删除文件
	 * @CreateDate:2017-6-14 下午4:44:22
	 * @param path
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FtpException
	 */
	public boolean deleteFile(String path, String fileName) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 删除文件
	 * @CreateDate:2017-6-14 下午4:45:23
	 * @param ftpClient
	 *            FTPClient对象
	 * @param path
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FtpException
	 */
	public boolean deleteFile(FTPClient ftpClient, String path, String fileName) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 清空目录
	 * @CreateDate:2017-6-14 下午4:53:18
	 * @param dirPath
	 *            目录路径
	 * @param isDelDir
	 *            是否删除目录
	 * @return
	 * @throws FtpException
	 */
	public boolean cleanDirectory(String dirPath, boolean isDelDir) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 清空目录
	 * @CreateDate:2017-6-14 下午4:53:18
	 * @param ftpClient
	 *            FTPClient对象
	 * @param dirPath
	 *            目录路径
	 * @param isDelDir
	 *            是否删除目录
	 * @return
	 * @throws FtpException
	 */
	public boolean cleanDirectory(FTPClient ftpClient, String dirPath, boolean isDelDir) throws FtpException;

	/**
	 * 连接到FTP服务器
	 * 
	 * public void connectFtpServe() throws FtpException;
	 */

	/**
	 * 重命名一个文件或文件夹，使用FTP的文件描述方式 例： 重命名一个文件
	 * renameFTPFile("/uplo/a.txt","/uplo/b.txt"); 重命名一个文件夹
	 * renameFTPFile("/upload/a","/upload/b");
	 * 
	 * @param fromName
	 * @param toName
	 */
	public boolean renameFTPFile(String fromName, String toName) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 目录是否存在
	 * @CreateDate:2017-6-14 上午10:04:05
	 *            目录
	 * @return
	 * @throws FtpException
	 */
	public boolean isDirExists(String dir) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 文件或目录是否存在
	 * @CreateDate:2017-6-14 上午10:04:05
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件名，为空时判断path是否存在
	 * @return
	 * @throws FtpException
	 */
	public boolean isFileExists(String dir, String fileName) throws FtpException;

	/**
	 * @ClassName: FtpUtil.java
	 * @Description: 文件或目录是否存在
	 * @CreateDate:2017-6-14 上午10:01:55
	 * @param ftpClient
	 *            FTPClient对象
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件名，为空时判断path是否存在
	 * @return
	 * @throws FtpException
	 */
	public boolean isFileExists(FTPClient ftpClient, String dir, String fileName) throws FtpException;

	/**
	 * 是否是目录
	 * 
	 * @param path
	 * @return
	 */
	public boolean isDirectory(String path) throws FtpException;

	/**
	 * 是否是文件
	 * 
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public boolean isFile(String path) throws FtpException;

	public void initTmpDir(String dir);

	public String getTmpDir();

}
