package com.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class FileUtil {

    /**
     * 清理过期文件
     *
     * @param dirPath    文件路径
     * @param expireTime 过期时间(毫秒)
     */
    public static void cleanExpiredFiles(String dirPath, long expireTime) {
        Date pointDate = new Date(System.currentTimeMillis() - expireTime);
        // 文件过滤条件
        IOFileFilter timeFileFilter = FileFilterUtils.ageFileFilter(pointDate, true);
        IOFileFilter filter = new AndFileFilter(FileFileFilter.FILE, timeFileFilter);
        File directory = new File(dirPath);
        Iterator<File> expiredFiles = FileUtils.iterateFiles(directory, filter, TrueFileFilter.INSTANCE);
        // 删除符合条件的文件
        while (expiredFiles.hasNext()) {
            File file = expiredFiles.next();
            boolean result = file.delete();
            if (result) {
                log.info("delete: {} successful!", file.getAbsolutePath());
            } else {
                log.warn("delete: {} failed!", file.getAbsolutePath());
            }

        }
    }

    /**
     * 级联创建目录和文件
     *
     * @param path
     * @throws IOException
     */
    public static void createFile(String path) throws IOException {
        if (StringUtils.isNotEmpty(path)) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
    }

    public static InputStream getInputstreamFromUrl(String url) throws IOException {
        URL sourceUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
        connection.setConnectTimeout(8 * 1000);
        connection.setReadTimeout(30000);
        return connection.getInputStream();
    }

    public static File inputStreamToFile(InputStream inputStream, String localPath, String prefix, String suffix) throws IOException {
        File localFile = new File(localPath);
        File file = File.createTempFile(prefix, suffix, localFile);
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        out.flush();
        out.close();
        inputStream.close();
        return file;
    }

    /**
     * 网络文件转存本地(图片与视频)
     */
    public static void storeRemoteFile(String netUrl, String localPath, String prefix, String suffix) throws Exception {
        // 获取网络文件流
        InputStream inputStream = getInputstreamFromUrl(netUrl);
        // 保存到本地
        File file = inputStreamToFile(inputStream, localPath, prefix, suffix);
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println(">>>>>>>>>> 已保存至:" + file.getPath());
        fileInputStream.close();
    }

    public static void main(String[] args) throws Exception {
        String newUrl = "http://aiphoto.wlhlwl.com/Testme.mp4";
        long start = System.currentTimeMillis();
        storeRemoteFile(newUrl, "D:/home/video/temp/1421393105072033792", "1421393105072033792", ".mp4");
        long end = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>>>>" + (end - start));
    }

}