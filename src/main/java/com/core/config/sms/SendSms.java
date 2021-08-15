package com.core.config.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-05-17
 */
@Slf4j
public class SendSms {

    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;

    private static final String REGIN_ID = "cn-qingdao";
    private static final String ACCESSKEY_ID = "LTAI4FctyAHeJAhtgfpbyEza";
    private static final String ACCESSKEY_SECRET = "ABAI4FctyAHeJAhtgfpbyEza";

    private static final String PHONE_REGEX = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/";
    private static String loginPhone = "19928363372";

    public static void main(String[] args) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(REGIN_ID, ACCESSKEY_ID, ACCESSKEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", loginPhone);
        request.putQueryParameter("SignName", "食遇食堂");
        request.putQueryParameter("TemplateCode", "SMS_173685790");
        //request.putQueryParameter("TemplateParam", "{\"code\":\"3311\"}");

        // 登录时: 1.校验手机号合法性; 2.校验手机号是否注册; 3.校验用户手机号不存在或已过期
        // 校验合法性
        Boolean valid = loginPhone.matches(PHONE_REGEX);
        if (!valid) {
            log.info("您输入的手机号无效!");
        }

        // 如果未注册, 提示用户手机号未注册
        Boolean registered = checkPhoneRegistered();
        if (!registered) {
            log.info("该手机号尚未注册!");
        }
        // 如果手机号key未过期, 直接取用手机号对应的验证码value 进行校验
        //Boolean expired = checkPhoneExpired(loginPhone);
        Object storedCode = redisTemplate.opsForValue().get(loginPhone);
        if (storedCode != null) {
            if ("inputCode".equals(storedCode.toString())) {
                log.info("校验成功");
            } else {
                log.info("验证码错误");
            }
        }

        // 下发短信验证码
        // 生成6位验证码
        String LOGIN_CHECK_CODE = RandomStringUtils.randomNumeric(6);
        request.putQueryParameter("TemplateParam", "{\"code\":" + LOGIN_CHECK_CODE + "}");
        CommonResponse response = client.getCommonResponse(request);
        log.info(response.getData());
        if (response.getData().equals("OK")) {
            // 发送成功后存入redis: key: 手机号, value: 验证码, 过期时间1分钟
            redisTemplate.opsForValue().set("LOGIN_PHONE", LOGIN_CHECK_CODE, 60*1, TimeUnit.SECONDS);
        }

    }

    private static Boolean checkPhoneExpired(String loginPhone) {
        Object login_phone = redisTemplate.opsForValue().get(loginPhone);
        if (null == login_phone) {
            return true;
        }
        return false;
    }

    private static Boolean checkPhoneRegistered() {
        return true;
    }
}
