package com.modules.payment.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-29
 */
@Data
public class WxPreOrderVo {

    private Wechat wechat;

    @Data
    public static class Wechat {
        private String timeStamp;
        private String partnerId;
        private String prepayId;
        private String nonceStr;
        private String wpackage;
        private String sign;
        private String appId;
    }

}
