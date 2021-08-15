package com.modules.payment.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 自定义消息推送测试
 * @Author: QiuQiang
 * @Date: 2021-07-22
 */
@RestController
@RequestMapping("testpush")
@ApiIgnore
public class TestPushDefined {

    @Autowired
    private ObjectMapper mapper;

    public void pushMsg() {
        /*CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(Constant.JiGuang.VERIFY_URL);
        String authStringEnc = Base64.encodeToString(Constant.JiGuang.APP_KEY + ":" + Constant.JiGuang.MASTER_SECRET);
        postRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
        postRequest.setHeader("Authorization", "Basic " + authStringEnc);

        OneKeyVo vo = new OneKeyVo();
        JsonObject params = new JsonObject();
        params.addProperty("loginToken", dto.getOneKeyLoginToken());
        StringEntity requestEntity = new StringEntity(params.toString(), "UTF-8");
        postRequest.setEntity(requestEntity);
        // 发送请求
        CloseableHttpResponse response = httpclient.execute(postRequest);
        HttpEntity entity = response.getEntity();
        // 解析结果
        String entityStr = EntityUtils.toString(entity, "UTF-8");
        OneKeyRespVo oneKeyRespVo = mapper.readValue(entityStr, new TypeReference<OneKeyRespVo>() {
        });*/
        //PushPayload.newBuilder().setAudience()
    }

    /*public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(""))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }*/

    /**
     * 构建payload
     *
     * @return
     */
    public PushPayload buildPushObject(String userId) throws JsonProcessingException {
        Map map = new HashMap<>();
        map.put("function", "alipaySuccess");
        map.put("orderId", "5243262344223426");
        String extraData = mapper.writeValueAsString(map);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias(userId))
                        .build())
                .setMessage(Message.newBuilder()
                        .setContentType("text")
                        .setTitle("Push of operated status.")
                        .setMsgContent("Your payment is success!")
                        .addExtra("uid_" + userId, extraData)
                        .build())
                .build();
    }

    /**
     * 发送自定义消息
     *
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @PostMapping("push")
    public HttpResult push() throws APIConnectionException, APIRequestException, JsonProcessingException {
        String userId = "123524363424";
        PushPayload pushPayload = buildPushObject(userId);
        // 全局初始化配置
        JPushClient jPushClient = new JPushClient(Constant.JiGuang.MASTER_SECRET, Constant.JiGuang.APP_KEY);
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        int responseCode = pushResult.getResponseCode();
        if (200 == responseCode) {
            return HttpResult.success(pushResult);
        }
        return HttpResult.fail();
    }


}
