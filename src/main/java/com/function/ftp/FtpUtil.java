package com.function.ftp;

import com.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 大多客户端端口被防火墙屏蔽, 常使用被动模式PASV连接
 */
@EnableConfigurationProperties(FtpProperties.class)
@Slf4j
public class FtpUtil {

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
     * @param destFileName  上传文件命名
     * @param in 上传文件数据流
     * @return 是否上传成功
     */
    public static boolean uploadFile(FtpProperties ftp, String destFileName, InputStream in) {
        FTPClient ftpClient = createFTPClient(ftp);
        boolean status = false;
        try {
            /*ftpClient.changeWorkingDirectory(ftp.getFiledir());
            if ("250".equals(ftpClient.getReplyCode() + "")) {
                log.info(">>> Now working in {}", ftp.getFiledir());
            } else {
                log.info(">>>>>> Saving path: {} for file is not exist, now creating...", ftp.getFiledir());
                boolean flag = ftpClient.makeDirectory(ftp.getFiledir()); //check the dir is exist or not
                if (!flag) {
                    throw new RRException("Bpmin file path create failed, maybe you've no permission!");
                }
            }*/

            String fileUrl = ftp.getFiledir();

            /*该部分为逐级创建*/
            String[] split = fileUrl.split("/");

            for (String str : split) {
                if(StringUtils.isBlank(str)) {
                    continue;
                }
                if (!ftpClient.changeWorkingDirectory(str)) {
                    System.err.println("不存在");
                    boolean makeDirectory = ftpClient.makeDirectory(str);
                    boolean changeWorkingDirectory = ftpClient.changeWorkingDirectory(str);
                    System.err.println(str + "创建：" + makeDirectory + ";切换: " + changeWorkingDirectory);
                } else {
                    System.err.println("存在");
                }
            }

            /*boolean flag = ftpClient.makeDirectory(ftp.getFiledir()); //check the dir is exist or not
            if (flag) {
                log.info(">>> File path created!");
            }*/

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String remote = ftp.getFiledir() + File.separator + destFileName;
            status = ftpClient.storeFile(remote, in);
            FTPFile[] ftpFiles = ftpClient.listFiles(ftp.getFiledir());
            Arrays.stream(ftpFiles).map(e -> e.getName()).forEach(e -> System.out.println(">>> Current directory files: " + e));
            in.close();
            ftpClient.logout();
        } catch (Exception e) {
            log.info("Upload failed! Cause: {}", e.getMessage(), e);
            throw new CommonException("Upload failed! Cause: " + e.getMessage());
        }
        return status;
    }

    /**
     * FTP下载
     *
     * @param ftp       FTP服务器信息
     * @param fileName  待下载文件名称
     * @param response 响应
     */
    public static void downloadFile(FtpProperties ftp, String fileName, HttpServletResponse response) {
        FTPClient ftpClient = createFTPClient(ftp);
        ServletOutputStream out;
        InputStream in;
        try {
            ftpClient.changeWorkingDirectory(ftp.getFiledir());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setDefaultTimeout(1000 * 60 * 30);
            ftpClient.setDataTimeout(1000 * 60 * 30);
            ftpClient.setConnectTimeout(1000 * 60 * 30);
            // 中文名处理
            fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
            fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
            // 设置下载方式
            if (fileName.endsWith(".xls")) {
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            if (fileName.endsWith(".xlsx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            // 文件下载
            in = ftpClient.retrieveFileStream(ftp.getFiledir() + File.separator + fileName);
            if (in == null) {
                throw new CommonException("Target file " + fileName + " is not exist!");
            }
            out = response.getOutputStream();
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
     * 清理过期文件
     *
     * @param ftp    FTP服务器信息
     * @param expireTime 过期时间(毫秒)
     */
    public static List<String> cleanExpiredFiles(FtpProperties ftp, long expireTime) throws IOException {
        FTPClient ftpClient = createFTPClient(ftp);
        ftpClient.changeWorkingDirectory(ftp.getFiledir());
        ftpClient.enterLocalPassiveMode();

        FTPFile[] ftpFiles = ftpClient.listFiles(ftp.getFiledir());
        List<FTPFile> fileList = filterDir(ftpFiles, expireTime);
        Iterator<FTPFile> expiredFiles = fileList.iterator();

        // 删除符合条件的文件
        List<String> nameList = new ArrayList<>();
        while (expiredFiles.hasNext()) {
            FTPFile ftpFile = expiredFiles.next();
            //boolean result = file.delete(); //windows
            boolean result = ftpClient.deleteFile(ftpFile.getName());
            if (result) {
                nameList.add(ftpFile.getName());
                log.info("delete: {} successful!", ftpFile.getName());
            } else {
                log.info("delete: {} failed!", ftpFile.getName());
            }
        }
        return nameList;
    }

    /**
     * 过滤文件
     */
    public static List<FTPFile> filterDir(FTPFile[] ftpFiles, long expireTime) {
        List<FTPFile> fileList = new ArrayList<>();
        // 文件过期条件 pointDate = 系统日期 - 一年 > 文件日期
        long pointDate = System.currentTimeMillis() - expireTime;
        for (FTPFile ftpFile : ftpFiles) {
            long fileDate = ftpFile.getTimestamp().getTimeInMillis();
            if (pointDate > fileDate) {
                fileList.add(ftpFile);
            }
        }
        return fileList;
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
        //boolean uploadStatus = uploadFile(ftp, "test0010.xlsx", "C:\\Users\\Administrator\\Desktop\\");

        // 测试下载
        // boolean downloadStatus = downloadFile(ftp,"test0010.xlsx", "D:\\test100");
    }


    /**
     * ftp upload
     */
    public void upload(FtpProperties ftp, MultipartFile file, String destFileName, HttpServletRequest request) throws IOException {
        // FTP服务器信息, 上传文件名称, 上传文件数据流
        boolean status = FtpUtil.uploadFile(ftp, destFileName, file.getInputStream());
        if (!status) {
            throw new CommonException("Upload failed!");
        }
    }

    /**
     * ftp download
     */
    @PostMapping("/download")
    public void downloading(@RequestParam("fileName") String fileName, @RequestParam("time") String time, HttpServletResponse response) {
        // FTP服务器信息, 待下载文件名称, 下载文件存放目录
        //FtpHelper.downloadFile(ftp, fileName, response);
    }

}