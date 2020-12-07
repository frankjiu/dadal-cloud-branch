package com;

import com.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.modules.sys.model.entity.Demo;
/*import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;*/
import com.result.HttpResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-09-23
 */
public class DemoTest {

    @Test
    public void testme() throws JsonProcessingException {
        /*Object thirdVal = "next";
        Object fourthVal = "30";
        boolean flag = "NEXT".equalsIgnoreCase(String.valueOf(thirdVal));

        LocalDate localDate = LocalDate.now().plusDays(Long.valueOf(fourthVal.toString()));

        System.out.println(localDate);*/


        /*List<Demo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Demo demo = new Demo();
            demo.setCardName("a" + i);
            list.add(demo);
        }

        for (int i = 0; i < list.size(); i++) {
            Demo demo = list.get(i);
            String cardName = demo.getCardName();
            System.out.println(cardName+cardName);
        }*/
        /*Random random = new Random();
        random.nextInt();
        double v = new Random().nextDouble();
        System.out.println(v);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(0, "");*/

        // String s1 = JacksonUtil.objToString(new Object());

        /*String url = "http://www.weather.com.cn/";
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        Request request = new Request.Builder()
                .url(url)//请求链接
                .get()// 可省略，默认GET方法
                .build();//创建Request对象
        try {
            Response response = client.newCall(request).execute();//获取Response对象
            ResponseBody body = response.body();
            String string = body.string();
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }


}
