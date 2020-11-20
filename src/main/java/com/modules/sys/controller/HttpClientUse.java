package com.modules.sys.controller;

import com.util.SslUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.InputStream;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-20
 */
public class HttpClientUse {

    private String ip = "172.28.129.6";
    private int port = 9990;
    private String userName = "admin";
    private String passWord = "rtdpTest@2019";
    private Integer REQUEST_TIMEOUT_MS = 15;
    //String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/{marketId}/competitiveStrategy/{strategyId}";
    private String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/AIRPORT_AIRPORT_CAN_USA/competitiveStrategy?id=AIRPORT_AIRPORT_CAN_USA_817d2651-39a6-496d-ad31" +
            "-729856987043";
    @Test
    public void testHttpGet() throws Exception {
        CredentialsProvider credentialsPovider = new BasicCredentialsProvider();
        credentialsPovider.setCredentials(new AuthScope(ip, port),
                new UsernamePasswordCredentials(userName, passWord));

        HttpClientBuilder clientbuilder = HttpClients.custom();
        clientbuilder = clientbuilder.setDefaultCredentialsProvider(credentialsPovider);

        CloseableHttpClient httpclient = clientbuilder.build();

        HttpGet httpget = new HttpGet(url);

        //在openConnection前调用该方法,信任所有SSL证书
        SslUtils.ignoreSsl();

        HttpResponse httpresponse = httpclient.execute(httpget);
        HttpEntity entity = httpresponse.getEntity();
        InputStream content = entity.getContent();
        System.out.println(content.toString());


    }
}