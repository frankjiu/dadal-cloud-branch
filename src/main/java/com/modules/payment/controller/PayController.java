package com.modules.payment.controller;

import com.alipay.api.response.AlipayTradePayResponse;
import com.core.anotation.Logged;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.core.utils.IpUtil;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.modules.payment.model.dto.*;
import com.modules.payment.model.vo.PayResultVo;
import com.modules.payment.service.PayService;
import com.modules.payment.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/pay")
@Api(tags = "03-充值")
public class PayController {

    @Autowired
    private PayService payService;

    @ApiOperation(value = "会员卡充值")
    @PostMapping("/rechargeMember")
    public Object rechargeMember(@Valid @RequestBody RechargeMemberDto input, HttpServletRequest request) {
        return payService.rechargeMember(Long.valueOf(input.getCardID()), input.getAmount(), input.getPayMode(),
                IpUtil.getIp(request));
    }

    @ApiOperation(value = "根据订单号查询订单状态及中奖结果")
    @PostMapping("/checkOrderStatus")
    public PayResultVo checkOrderStatus(@Valid @RequestBody CheckOrderDto input) {
        return payService.checkOrderStatus(Long.parseLong(input.getOrderID()));
    }
}
