package com.modules.payment.controller;

import com.core.anotation.Logged;
import com.core.constant.Constant;
import com.modules.payment.service.NotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/notify")
@Api(tags = "0502-三方支付回调")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @ApiOperation(value = "微信支付回调", notes = "")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "微信支付回调")
    @PostMapping("/wxPayNotify")
    public void wxPayNotify(@RequestBody String xmlData, HttpServletResponse response) {
        notifyService.wxPayNotify(xmlData, response);
    }

    @ApiOperation(value = "支付宝支付回调", notes = "")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "支付宝支付回调")
    @PostMapping("/aliPayNotify")
    public void aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        notifyService.aliPayNotify(parameterMap, response);
    }
    @ApiOperation(value = "支付宝会员卡充值回调", notes = "")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "支付宝会员卡充值回调")
    @PostMapping("/aliRechargeNotify")
    public void aliRechargeNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        notifyService.aliRechargeNotify(parameterMap, response);
    }

    @ApiOperation(value = "微信会员卡充值回调", notes = "")
    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_PAY, remark = "微信会员卡充值回调")
    @PostMapping("/wxRechargeNotify")
    public void wxRechargeNotify(@RequestBody String xmlData, HttpServletResponse response) {
        notifyService.wxRechargeNotify(xmlData, response);
    }

}
