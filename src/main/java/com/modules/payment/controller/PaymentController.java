package com.modules.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.core.anotation.AuthCheck;
import com.core.anotation.Logged;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.AESUtil;
import com.core.utils.IpUtil;
import com.core.utils.JWTPayload;
import com.core.utils.JWTUtils;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.modules.payment.model.dto.*;
import com.modules.payment.model.vo.CardQrCodeVo;
import com.modules.payment.model.vo.PayResultVo;
import com.modules.payment.model.vo.WxUnifiedResultVo;
import com.modules.payment.service.PayService;
import com.modules.payment.service.PaymentService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/payment")
@Api(tags = "05-支付")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PayService payService;

    @ApiOperation(value = "生成app用户支付二维码")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "生成app用户支付二维码")
    @PostMapping("/generateCodeStr")
    public HttpResult<CardQrCodeVo> generateQrCodeStr(@Valid @RequestBody PaymentCodeDto input) {
        //暂定token为用户ID,需与前端确认
        // 从请求头获取 token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return HttpResult.fail(RespCode.NO_TOKEN);
        }
        // 解析 token 获取用户ID
        Claims claims;
        try {
            claims = jwtUtils.parseJWT(token);
        } catch (Exception e) {
            return HttpResult.fail(RespCode.AUTHENTICATION_FAILED);
        }
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        CardQrCodeVo result = paymentService.generateQrCodeStr(input, uid);
        return HttpResult.success(result);
    }

    /**
     * 支付三种方式
     * 跨店卡 对账户表余额和餐厅表销售额进行修改  参数 账户ID, 订单金额
     * 微信 调用微信api
     * 支付宝 调用支付宝api
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "APP会员卡二维码支付(设备调用)")
    @Logged(type = Constant.Log.TYPE_ADD, module = Constant.Log.MODULE_PAY, remark = "会员卡支付")
    @PostMapping("/payMemberCard")
    @AuthCheck
    public HttpResult payMemberCard(@Valid @RequestBody CardQrPayDto input) {
        PaymentParamDto paramDto = new PaymentParamDto();
        BeanUtils.copyProperties(input, paramDto);
        this.parseParam(paramDto);
        PayResultVo result = null;
        try {
            result = paymentService.pay(paramDto);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CommonException("支付异常!");
        }
        return HttpResult.success(result);
    }

    /**
     * 根据二维码生成规则，逆推二维码字符串信息
     *
     * @param paramDto
     * @return
     */
    private PaymentParamDto parseParam(PaymentParamDto paramDto) {
        String pstr = paramDto.getQrCodeString();
        String qrCode = pstr.substring(3);
        String password = Constant.QRCODE_PASSWORD;
        //获取解密后的字符串
        String paramStr = AESUtil.decrypt(qrCode, password);
        String[] arr = paramStr.split("_");
        if (arr.length == 3) {
            paramDto.setUserId(Long.parseLong(arr[0]));
            paramDto.setPaymentMode(Integer.parseInt(arr[1]));
        }
        if (arr.length == 4) {
            paramDto.setUserId(Long.parseLong(arr[0]));
            paramDto.setRewardId(Long.parseLong(arr[1]));
            paramDto.setPaymentMode(Integer.parseInt(arr[2]));
        }

        return paramDto;
    }

    @ApiOperation(value = "生成支付包扫码支付二维码")
    @Logged(type = Constant.Log.TYPE_ADD, module = Constant.Log.MODULE_PAY, remark = "支付宝扫码支付")
    @PostMapping("/getAliPayQr")
    public HttpResult<AlipayTradePrecreateResponse> generateAliPayQr(@Valid @RequestBody AliPayQrDto input) {
        AlipayTradePrecreateResponse response = paymentService.generateAliPayQr(input.getOrderId());
        return HttpResult.success(response);
    }

    @ApiOperation(value = "查询支付宝订单状态", notes = "商家收款机/员工调用")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "商家查询支付宝订单")
    @PostMapping("/queryAliPayOrder")
    public HttpResult<AlipayTradeQueryResponse> queryAliOrder(@Valid @RequestBody AliPayQueryDto aliPayQueryDto) {
        AlipayTradeQueryResponse response = paymentService.queryAlipayOrder(aliPayQueryDto.getOrderId(), true);
        return HttpResult.success(response);
    }

    @ApiOperation(value = "查询支付宝订单状态", notes = "后台管理调用")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "管理查询支付宝订单")
    @PostMapping("/accountQueryAliPayOrder")
    public HttpResult<AlipayTradeQueryResponse> accountQueryAliOrder(@Valid @RequestBody AliPayQueryDto aliPayQueryDto) {
        AlipayTradeQueryResponse response = paymentService.queryAlipayOrder(aliPayQueryDto.getOrderId(), false);
        return HttpResult.success(response);
    }

    @ApiOperation(value = "关闭支付宝订单状态", notes = "商家收款机/员工调用")
    @Logged(type = Constant.Log.TYPE_DEL, module = Constant.Log.MODULE_PAY, remark = "商家关闭支付宝订单")
    @PostMapping("/closeAliPayOrder")
    public HttpResult<AlipayTradeCloseResponse> closeAliOrder(@Valid @RequestBody AliPayCloseDto aliPayCloseDto) {
        AlipayTradeCloseResponse response = paymentService.closeAliPayOrder(aliPayCloseDto.getOrderId(),
                aliPayCloseDto.getOperatorId(), true);
        return HttpResult.success(response);
    }

    @ApiOperation(value = "关闭支付宝订单状态", notes = "后台管理调用")
    @Logged(type = Constant.Log.TYPE_DEL, module = Constant.Log.MODULE_PAY, remark = "管理关闭支付宝订单")
    @PostMapping("/accountCloseAliPayOrder")
    public HttpResult<AlipayTradeCloseResponse> accountCloseAliOrder(@Valid @RequestBody AliPayCloseDto aliPayCloseDto) {
        AlipayTradeCloseResponse response = paymentService.closeAliPayOrder(aliPayCloseDto.getOrderId(),
                aliPayCloseDto.getOperatorId(), false);
        return HttpResult.success(response);
    }

    @ApiOperation(value = "生成微信扫码支付二维码", notes = "收款机/员工使用")
    @Logged(type = Constant.Log.TYPE_ADD, module = Constant.Log.MODULE_PAY, remark = "生成微信扫码支付二维码")
    @PostMapping("/generateWxQr")
    public HttpResult<WxUnifiedResultVo> generateWxQr(@Valid @RequestBody WxPayQrDto input,
                                                      HttpServletRequest request) {
        WxUnifiedResultVo resultVo = paymentService.generateWxQr(input.getOrderID(), IpUtil.getIp(request));
        return HttpResult.success(resultVo);
    }

    @ApiOperation(value = "取消微信订单", notes = "收款机/员工使用")
    @Logged(type = Constant.Log.TYPE_DEL, module = Constant.Log.MODULE_PAY, remark = "取消微信订单")
    @PostMapping("/closeWxOrder")
    public HttpResult<WxPayOrderCloseResult> closeWxOrder(@Valid @RequestBody WxCloseOrderDto input) {
        WxPayOrderCloseResult result = paymentService.closeWxOrder(input.getWxOrderId());
        return HttpResult.success(result);
    }

    @ApiOperation(value = "查询微信订单状态", notes = "收款机/员工使用")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "查询微信订单状态")
    @PostMapping("/queryWxOrder")
    public HttpResult<WxPayOrderQueryResult> queryWxOrder(@Valid @RequestBody WxPayQueryDto input) {
        WxPayOrderQueryResult result = paymentService.queryWxOrder(input.getWxOrderId());
        return HttpResult.success(result);
    }

    @ApiOperation(value = "查询微信扫码支付订单状态", notes = "收款机/员工使用")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "查询微信扫码支付订单状态")
    @PostMapping("/queryWxSMOrder")
    public HttpResult<String> queryWxScanOrder(@Valid @RequestBody WxPayQueryDto input) throws Exception {
        String result = paymentService.queryFKMOrder(input.getWxOrderId());
        return HttpResult.success(result);
    }

    @ApiOperation(value = "APP获取充值订单", notes = "收款机/员工使用")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "查询微信扫码支付订单状态")
    @PostMapping("/payOrder")
    public HttpResult rechargeChainCard(@Valid @RequestBody ChainCardRechargeDto input, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return HttpResult.fail(RespCode.NO_TOKEN);
        }
        // 解析 token 获取用户ID
        Claims claims;
        try {
            claims = jwtUtils.parseJWT(token);
        } catch (Exception e) {
            return HttpResult.fail(RespCode.AUTHENTICATION_FAILED);
        }
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        if (payload.getId() == null) {
            return HttpResult.fail(RespCode.USER_NOT_FOUND);
        }
        //卡号和用户ID相同，为跨店卡充值;否则为会员卡
        Object result = paymentService.rechargeCrossCard(input, payload.getId(), IpUtil.getIp(request));
        return HttpResult.success(result);
    }

    @ApiOperation(value = "订单关闭后订单信息处理")
    @PostMapping("/closeOrder")
    public Object closeOrder(@Valid @RequestBody CloseOrderDto input) {
        payService.closeOrder(input.getOrderID());
        return HttpResult.success();
    }
}
