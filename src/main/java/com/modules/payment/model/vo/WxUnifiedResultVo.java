package com.modules.payment.model.vo;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import lombok.Data;

/**
 * @Description:
 * @date: 2021/6/11/0011 13:48
 * @author: YuZHenBo
 */
@Data
public class WxUnifiedResultVo {
    private WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
    private Long orderID;
    private String wxOrderID;
}
