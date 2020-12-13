package com.function.resttemplate;

import com.core.config.resttemplate.RestTemplateUtils;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;

/**
 * @Description: 客户端接口调用
 * @Author: QiuQiang
 * @Date: 2020-11-07
 */
public class RestTemplateClientTest {

    private RestTemplate restTemplate = new RestTemplate();

    //get调用
    @Test
    public void testGETNoParams() {
        // 工具调用
        String result = RestTemplateUtils.get("http://localhost:55555/rest/user", String.class).getBody();
        // 注入调用
        //String result = restTemplate.getForObject("http://localhost:55555/rest/user", String.class);
        System.out.println(result);
    }

    //get带参调用
    @Test
    public void testGETParams() {
        // http://localhost:55555/rest/user/{1}
        String result = restTemplate.getForObject("http://localhost:55555/rest/user/{name}", String.class, "ZhangSan");
        System.out.println(result);
    }

    /**
     * post调用
     */
    @Test
    public void testPostMethod() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", "rest");
        HttpHeaders httpHeaders = new HttpHeaders();
        // 设置请求类型
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 封装参数和头信息
        HttpEntity<JSONObject> httpEntity = new HttpEntity(jsonObject, httpHeaders);
        String url = "http://localhost:55555/rest/provider";
        // 1.自建restTemplate
        RestTemplate restTemplate = new RestTemplate();
        // 2.加注解并通过Autowired注入
        ResponseEntity<String> mapResponseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        // 3.工具类
        //ResponseEntity<String> mapResponseEntity = RestTemplateUtils.post(url, httpEntity, String.class);
        System.out.println(mapResponseEntity.getBody());
    }

    //put调用
    @Test
    public void testPutMethod() throws MalformedURLException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", "rest");
        HttpHeaders httpHeaders = new HttpHeaders();
        // 设置请求类型
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 封装参数和头信息
        HttpEntity<JSONObject> httpEntity = new HttpEntity(jsonObject, httpHeaders);
        String url = "http://localhost:55555/rest/provider/{id}";
        restTemplate.put(url, httpEntity, 1327);
    }

    // delete调用
    @Test
    public void testDelete() {
        String url = "http://localhost:55555/rest/provider/{id}";
        restTemplate.delete(url, 1327);
    }

    // exchange调用
    @Test
    public void testExchange() {
        String url = "http://localhost:55555/rest/user/exchange/{name}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, "rest");
        System.out.println(exchange.getBody());
    }

}
