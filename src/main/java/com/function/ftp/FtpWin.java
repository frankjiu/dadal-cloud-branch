package com.function.ftp;

import com.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 大多客户端端口被防火墙屏蔽, 常使用被动模式PASV连接
 */
@EnableConfigurationProperties(FtpProperties.class)
@Slf4j
public class FtpWin {

    /**
     * 创建连接
     *
     * @param ftp FTP服务器信息
     * @return ftp连接
     */
    public static FTPClient createFTPClient(FtpProperties ftp) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding(ftp.getEncode());
            ftpClient.connect(ftp.getUrl(), ftp.getPort());
            ftpClient.login(ftp.getUsername(), ftp.getPassword());
            int code = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(code)) {
                log.info("Connect failed! Error code:{}" + code);
                ftpClient.disconnect();
            } else {
                log.error("<==============connect to {}:{} successed! ==============>", ftp.getUrl(), ftp.getPort());
            }
        } catch (Exception e) {
            log.info("Connection error: {}", e.getMessage());
        }
        return ftpClient;
    }

    /**
     * FTP上传
     *
     * @param ftp       FTP服务器信息
     * @param fileName  上传文件名称
     * @param localPath 上传文件目录源
     * @return 是否上传成功
     */
    public static boolean uploadFile(FtpProperties ftp, String fileName, String localPath) {
        FTPClient ftpClient = createFTPClient(ftp);
        boolean status = false;
        try {
            ftpClient.changeWorkingDirectory(ftp.getFiledir());
            ftpClient.enterLocalPassiveMode();

            log.info("FTP working path: {}", ftp.getFiledir());
            File file = new File(localPath + File.separator + fileName);
            if (!file.exists()) {
                throw new CommonException(localPath + " is not exist, maybe you've specified a wrong path!");
            }
            InputStream in = new FileInputStream(file);


            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            status = ftpClient.storeFile(fileName, in);
            FTPFile[] ftpFiles = ftpClient.listFiles(ftp.getFiledir());
            Arrays.stream(ftpFiles).map(e -> e.getName()).forEach(e -> System.out.println(">>> Current directory files: " + e));
            in.close();
            ftpClient.logout();
        } catch (Exception e) {
            log.info("Upload failed! Cause: {}", e.getMessage(), e);
        }
        return status;
    }

    /**
     * FTP下载
     *
     * @param ftp       FTP服务器信息
     * @param fileName  待下载文件名称
     * @param localPath 下载文件存放目录
     * @return 是否下载成功 , HttpServletResponse response
     */
    public static void downloadFile(FtpProperties ftp, String fileName, String localPath) {
        FTPClient ftpClient = createFTPClient(ftp);
        boolean status = false;
        //ServletOutputStream out = null;
        FileOutputStream out = null;
        try {
            ftpClient.changeWorkingDirectory(ftp.getFiledir());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setDefaultTimeout(1000 * 60 * 30);
            ftpClient.setDataTimeout(1000 * 60 * 30);
            ftpClient.setConnectTimeout(1000 * 60 * 30);
            File localDir = new File(localPath);
            if (!localDir.exists()) {
                log.info(">>>>>> Saving path: {} for file is not exist, now creating...", localPath);
                localDir.mkdirs();
            }
            File localFile = new File(localDir, fileName);
            out = new FileOutputStream(localFile);
            //out = response.getOutputStream();
            InputStream in = ftpClient.retrieveFileStream(fileName);
            int len;
            byte[] byteArray = new byte[1024];
            while ((len = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, len);
            }
            out.flush();
            out.close();
            ftpClient.logout();
        } catch (Exception e) {
            log.info("Download failed! Cause: {}", e.getMessage(), e);
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
        FtpProperties ftp = new FtpProperties();
        ftp.setUrl("10.79.5.29");
        ftp.setPort(9998);
        ftp.setUsername("fmmftp");
        ftp.setPassword("user*2019");
        ftp.setFiledir("/test78/");
        ftp.setEncode("GBK");
        ftp.setIsRunner(true);
        ftp.setMode("pasv");
        ftp.setRetryCount(3);

        // 测试上传
        boolean uploadStatus = uploadFile(ftp, "test0010.xlsx", "C:\\Users\\Administrator\\Desktop\\");
        System.out.println(">>>>>>>>>>>>>>>>>> upload status: " + uploadStatus);

        // 测试下载 boolean downloadStatus =
        downloadFile(ftp,"test0010.xlsx", "D:\\test100");
    }
}