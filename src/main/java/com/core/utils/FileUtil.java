package com.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

import java.io.File;
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

}