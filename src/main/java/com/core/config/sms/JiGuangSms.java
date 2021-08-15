package com.core.config.sms;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jiguang.common.utils.Preconditions;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.RespCode;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * 极光认证与短信验证码发送
 * signId:一般为当前app单独建立一个作为默认签名
 */
@Component
@Slf4j
public class JiGuangSms {

    @Resource(name = "AppSMSClient")
    private SMSClient smsClient;

    /**
     * 发送短信验证码
     *
     * @param tempId
     * @param mobile
     */
    public String sendSmsCodeDef(int tempId, String mobile) {
        String code = RandomStringUtils.randomNumeric(6);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(tempId)
                .addTempPara("code", code)
                .build();
        String messageId = "";
        try {
            SendSMSResult response = smsClient.sendTemplateSMS(payload);
            messageId = response.getMessageId();
        } catch (APIRequestException e) {
            log.error(e.getMessage(), e);
        } catch (APIConnectionException e) {
            log.error(e.getMessage(), e);
        }
        return messageId;
    }

    /**
     * 校验短信验证码
     *
     * @param messageId
     * @param code
     * @return
     * @throws Exception
     */
    public boolean checkCodeDef(String messageId, String code) {
        boolean result = false;
        try {
            ValidSMSResult validSMSResult = smsClient.sendValidSMSCode(messageId, code);
            result = validSMSResult.getIsValid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 自定义发送短信code
     *
     * @param tempId
     * @param mobile
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    public String sendSmsCode(int tempId, String mobile) {
        String code = RandomStringUtils.randomNumeric(4);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(tempId)
                .addTempPara("code", code)
                .build();

        String authCode = ServiceHelper.getBasicAuthorization(Constant.JiGuang.APP_KEY, Constant.JiGuang.MASTER_SECRET);
        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        smsClient.setHttpClient(httpClient);
        Preconditions.checkArgument(null != payload, "SMS payload should not be null");
        ResponseWrapper response = null;
        try {
            response = httpClient.sendPost(Constant.JiGuang.SEND_OR_CHECK_MSG_VALID_URL, payload.toString());
        } catch (APIConnectionException e) {
            throw new CommonException(RespCode.API_ERROR.getDescription() + "短信验证码发送连接异常:", e);
        } catch (APIRequestException e) {
            throw new CommonException(RespCode.API_ERROR.getDescription() + "短信验证码发送请求异常:", e);
        } catch (Exception e) {
            throw new CommonException(RespCode.INTERNAL_ERROR.getDescription(), e);
        }
        SendSMSResult sendSMSResult = SendSMSResult.fromResponse(response, SendSMSResult.class);
        return sendSMSResult.getMessageId();
    }

    /**
     * 自定义校验msgId
     *
     * @param msgId
     * @param code
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    public boolean checkCode(String msgId, String code) {
        Preconditions.checkArgument(null != msgId, "Message id should not be null");
        Pattern codePattern = Pattern.compile("^[0-9]{6}");
        Preconditions.checkArgument(codePattern.matcher(code).matches(), "The verification code shoude be consist of six number");
        JsonObject json = new JsonObject();
        json.addProperty("code", code);

        String authCode = ServiceHelper.getBasicAuthorization(Constant.JiGuang.APP_KEY, Constant.JiGuang.MASTER_SECRET);
        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        smsClient.setHttpClient(httpClient);

        ResponseWrapper response = null;
        try {
            response = httpClient.sendPost(Constant.JiGuang.SEND_OR_CHECK_MSG_VALID_URL + "/" + msgId + "/valid", json.toString());
        } catch (APIConnectionException e) {
            throw new CommonException(RespCode.API_ERROR.getDescription() + e.getMessage());
        } catch (APIRequestException e) {
            throw new CommonException(RespCode.API_ERROR.getDescription() + e.getMessage());
        } catch (Exception e) {
            throw new CommonException(RespCode.INTERNAL_ERROR.getDescription() + e.getMessage());
        }
        ValidSMSResult validSMSResult = ValidSMSResult.fromResponse(response, ValidSMSResult.class);
        return validSMSResult.getIsValid();
    }

    /**
     * 测试: 根据模板和用户手机号进行发送
     */
    @Test
    public void testSend() {
        String messageId = sendSmsCode(Constant.JiGuang.TEMPID_DYNAMIC_CODE, "19928363372");
        boolean isValid = checkCode(messageId, "0011");
        System.out.println(isValid);
    }

}
