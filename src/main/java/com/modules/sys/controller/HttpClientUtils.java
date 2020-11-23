package com.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-23
 */
public class HttpClientUtils {

    public static void main(String[] args) throws Exception {

        String ip = "172.28.129.6";
        int port = 9990;
        String userName = "admin";
        String passWord = "rtdpTest@2019";
        Integer REQUEST_TIMEOUT_MS = 15;
        //String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/{marketId}/competitiveStrategy/{strategyId}";
        String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/AIRPORT_AIRPORT_CAN_USA/competitiveStrategy";
        //String url = "https://www.choupangxia.com/";
        //String username = "admin1";
        //String password = "admin1";
        //MessageVo vo = new MessageVo();
        //vo.setId("No11111");
        //vo.setName("Steven");
        SslUtils.ignoreSsl();
        String result = createStream(url, userName, passWord, null);
        System.out.println(result);
    }

    /**
     * url： 请求的url
     * requestDTO： post请求的url
     * 此处根据自己需求更改为post请求还是其他请求
     */
    private static String createStream(String url, String username, String password, MessageVo requestDTO) {
        CloseableHttpClient httpClient = getHttpClient(username, password);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json; charset=utf-8");
        httpGet.setHeader("Accept", "application/json");
        String s = "";
        //String s1 = JSON.toJSONString(requestDTO);
        //httpGet.setEntity(new StringEntity(s1, StandardCharsets.UTF_8));
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            s = EntityUtils.toString(entity);
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    /**
     * 获取带有权限的HttpClient连接
     */
    private static CloseableHttpClient getHttpClient(String username, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClients.custom().setDefaultCredentialsProvider(provider).build();
    }
}

class MessageVo {
    private String id;
    private String name;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}