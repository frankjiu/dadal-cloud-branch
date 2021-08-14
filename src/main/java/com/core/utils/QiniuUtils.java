package com.core.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wlhlwl.hyzc.customer.core.constant.Constant;
import com.wlhlwl.hyzc.customer.core.exception.CommonException;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public class QiniuUtils {

    /**
     * 上传文件
     *
     * @param in       输入流
     * @param bucketNm 空间名
     * @param key      新文件名
     * @return
     */
    public static String upload(InputStream in, String bucketNm, String key) throws QiniuException {
        if (StringUtils.isEmpty(key)) {
            throw new CommonException("Please input the image key!");
        }
        UploadManager uploadManager = getUploadManager();
        // 获取token
        String token = getToken(bucketNm);
        // 上传输入流
        Response response = uploadManager.put(in, key, token, null, null);
        // 解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

    public static String getNewName(String key) {
        // 生成新的文件名
        String suffix = key.substring(key.lastIndexOf('.'));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        return newFileName;
    }

    public static String getDomain(String bucketName) {
        BucketManager bucketManager = QiniuUtils.getBucketManager();
        try {
            String[] domains = bucketManager.domainList(bucketName);
            if (domains != null && domains.length > 0) {
                return "http://" + domains[0] + File.separator;
            }
        } catch (QiniuException e) {
        }
        throw new CommonException("获取七牛云空间域名失败!");
    }

    /**
     * 删除文件
     *
     * @param bucketNm 空间名
     * @param key      文件名
     * @return
     */
    public static boolean delete(String bucketNm, String key) {
        try {
            BucketManager bg = getBucketManager();
            bg.delete(bucketNm, key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取上传管理器
     */
    public static UploadManager getUploadManager() {
        return new UploadManager(configNorthRegion());
    }

    /**
     * 获取Bucket管理对象
     */
    public static BucketManager getBucketManager() {
        return new BucketManager(getAuth(), configNorthRegion());
    }

    /**
     * 获取七牛云上传Token
     *
     * @param bucketNm
     * @return
     */
    public static String getToken(String bucketNm) {
        return getAuth().uploadToken(bucketNm);
    }

    /**
     * 获取七牛云Auth
     */
    private static Auth getAuth() {
        Auth auth = Auth.create(Constant.QiniuDisk.ACCESS_KEY, Constant.QiniuDisk.SECRET_KEY);
        return auth;
    }

    /**
     * 获取华北服务器配置
     */
    private static Configuration configNorthRegion() {
        return new Configuration(Region.region1());
    }

}
