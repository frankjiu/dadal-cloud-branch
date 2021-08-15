package com.modules.payment.service;

import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.modules.payment.model.dto.ChainCardRechargeDto;
import com.modules.payment.model.dto.PaymentCodeDto;
import com.modules.payment.model.dto.PaymentParamDto;
import com.modules.payment.model.vo.CardQrCodeVo;
import com.modules.payment.model.vo.PayResultVo;
import com.modules.payment.model.vo.WxUnifiedResultVo;

public interface PaymentService {

    //生成会员卡二维码
    CardQrCodeVo generateQrCodeStr(PaymentCodeDto paymentCodeDto, String userId);

    //会员卡支付
    PayResultVo pay(PaymentParamDto paramDto) throws Exception;

    //生成支付宝扫码支付二维码
    AlipayTradePrecreateResponse generateAliPayQr(Long orderID);

    //支付宝条码支付
    AlipayTradePayResponse aliScanPay(Long orderId, String authCode);

    //查询支付宝订单
    AlipayTradeQueryResponse queryAlipayOrder(Long orderID, Boolean isMerchantOrder);

    //关闭支付宝订单
    AlipayTradeCloseResponse closeAliPayOrder(Long orderID, Long operatorID, Boolean isMerchantOrder);

    //微信扫码支付
    WxPayMicropayResult wxScanPay(Long orderID, String authCode, String ipAddr);

    //生成微信支付二维码
    WxUnifiedResultVo generateWxQr(Long orderID, String ipAddr);

    //关闭微信订单
    WxPayOrderCloseResult closeWxOrder(String wxOrderId);

    //查询微信订单状态
    WxPayOrderQueryResult queryWxOrder(String wxOrderId);

    //查询微信付款码订单状态
    String queryFKMOrder(String orderID) throws Exception;

    Object rechargeCrossCard(ChainCardRechargeDto param, Long userId, String ipAddr);

    PayResultVo drawReward(Long orderId);
}
