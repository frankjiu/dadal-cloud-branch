package com.modules.sys.controller;

import com.util.SslUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
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

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-20
 */
@Slf4j
public class HttpClientUse {

    private String ip = "172.28.129.6";
    private int port = 9990;
    private String userName = "admin";
    private String passWord = "rtdpTest@2019";
    private Integer REQUEST_TIMEOUT_MS = 15;
    //String url = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/{marketId}/competitiveStrategy/{strategyId}";
    private String httpurl = "https://172.28.129.6:9990/rtdpadmin/services/rest/market/AIRPORT_AIRPORT_CAN_USA/competitiveStrategy?id=AIRPORT_AIRPORT_CAN_USA_817d2651-39a6-496d" +
            "-ad31" +
            "-729856987043";
    @Test
    public void testHttpGet() throws Exception {
        CredentialsProvider credentialsPovider = new BasicCredentialsProvider();
        credentialsPovider.setCredentials(new AuthScope(ip, port),
                new UsernamePasswordCredentials(userName, passWord));

        trustAllHosts();
        HttpClientBuilder clientbuilder = HttpClients.custom();
        clientbuilder = clientbuilder.setDefaultCredentialsProvider(credentialsPovider);
        //URL url = new URL(httpurl);
        //HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
        //https.setHostnameVerifier(DO_NOT_VERIFY);
        CloseableHttpClient httpclient = clientbuilder.build();

        httpurl = httpGet(httpurl);
        System.out.println(httpurl);
        HttpGet httpget = new HttpGet(httpurl);

        //在openConnection前调用该方法,信任所有SSL证书
        //SslUtils.ignoreSsl();

        HttpResponse httpresponse = httpclient.execute(httpget);
        HttpEntity entity = httpresponse.getEntity();
        InputStream content = entity.getContent();
        System.out.println(content.toString());

    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts1() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                log.info(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                log.info(TAG, "checkServerTrusted");
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
                log.info("skyapp", "checkClientTrusted");
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                log.info("skyapp", "checkServerTrusted");
            }
        } };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String httpGet(String httpUrl) {
        BufferedReader input = null;
        StringBuilder sb = null;
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL(httpUrl);
            try {
                // trust all hosts
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                if (url.getProtocol().toLowerCase().equals("https")) {
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    con = https;
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }
                input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String s;
                while ((s = input.readLine()) != null) {
                    sb.append(s).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } finally {
            // close buffered
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // disconnecting releases the resources held by a connection so they may be closed or reused
            if (con != null) {
                con.disconnect();
            }
        }
        return sb == null ? null : sb.toString();
    }


}