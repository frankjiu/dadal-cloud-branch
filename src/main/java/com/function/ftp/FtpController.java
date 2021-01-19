package com.function.ftp;

import com.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.ForkJoinPool;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-31
 */
@RestController
@RequestMapping("/ftp")
@Validated
@Slf4j
@EnableConfigurationProperties(FtpProperties.class)
public class FtpController {

    @Autowired
    private ForkJoinPool pool;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private String uploadFilePath;

    @Autowired
    private FtpProperties ftp;

    /**
     * 文件上传1
     */
    @PostMapping("/upload1")
    private String upload1(String srcDir, String destDir, String destFileName) throws IOException {
        File srcFile = new File(srcDir);
        File destFileDir = new File(destDir);
        if(!destFileDir.exists()){
            destFileDir.mkdir();
        }
        FileUtils.copyFile(srcFile, new File(destDir, destFileName));
        return "Upload successful!";
    }

    /**
     * 文件上传2
     */
    @PostMapping("/upload2")
    private void upload2(MultipartFile uploadFile, String destFileName, HttpServletRequest req) throws IOException {
        if (StringUtils.isEmpty(uploadFile)) {
            return;
        }
        // file save
        File file = new File(new File(uploadFilePath).getAbsolutePath() + File.separator + destFileName);
        if (!file.getParentFile().exists()) {
            boolean flag = file.getParentFile().mkdirs();
            if (!flag) {
                throw new CommonException("File path is not exist!");
            }
        }
        uploadFile.transferTo(file);
    }

    /**
     * 文件下载1
     */
    @PostMapping("/download1")
    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        ServletOutputStream os = null;
        try {
            String path = uploadFilePath + fileName;
            // 处理请求文件名中文乱码(服务器默认码表, 前台页面码表)
            fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
            // 处理下载文件名中文乱码(客户端码表, 响应头码表)
            String downLoadFileName = new String(fileName.getBytes("gbk"), "iso8859-1");
            // 设置下载方式
            response.setHeader("Content-Disposition", "attachment;filename=" + downLoadFileName);
            // 文件下载
            is = new BufferedInputStream(new FileInputStream(path)); // 从路径中读取被下载文件
            os = response.getOutputStream();
            int len;
            byte[] arrLen = new byte[1024];
            while ((len = is.read(arrLen)) != -1) {
                os.write(arrLen, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            log.info("下载出现异常!", e);
        } finally {
            // 关闭输出流
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    log.info("关闭输出流异常!", e);
                }
            }
            // 关闭输入流
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.info("关闭输入流异常!", e);
                }
            }
        }
    }

    /**
     * 文件下载2
     */
    @PostMapping("/download2")
    public HttpServletResponse download2(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            //文件目录uploadFilePath:  D:\demo\stored\
            String path = uploadFilePath + fileName;
            // path是指欲下载的文件的路径
            File file = new File(path);
            // 取得文件名
            String filename = file.getName();
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            os.write(buffer);
            os.flush();
            os.close();
        } catch (IOException ex) {
            log.info(ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 文件下载3
     */
    @PostMapping("/download3")
    public void download3(@RequestParam("fileName") String fileName, @RequestParam("time") String time, HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            // 中文名处理
            //fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
            //String downLoadFileName = new String(fileName.getBytes("gbk"), "iso8859-1");
            // 设置下载方式
            /*response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");*/
            if (fileName.endsWith(".xls")) {
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            if (fileName.endsWith(".xlsx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            // 文件下载
            in = new BufferedInputStream(new FileInputStream(uploadFilePath + fileName)); // 从路径中读取被下载文件
            out = response.getOutputStream();
            int len;
            byte[] byteArray = new byte[1024];
            while ((len = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CommonException("Download failed! cause: " + e.getMessage());
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new CommonException("Download failed! cause: " + e.getMessage());
            }
        }
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
    @RequiresPermissions("sys:sys:other")
    public void downloading(@RequestParam("fileName") String fileName, @RequestParam("time") String time, HttpServletResponse response) {
        // FTP服务器信息, 待下载文件名称, 下载文件存放目录
        FtpUtil.downloadFile(ftp, fileName, response);
    }


}
