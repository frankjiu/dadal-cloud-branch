package com.modules.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.*;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.utils.*;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.modules.gen.dao.UserMapper;
import com.modules.gen.model.entity.User;
import com.modules.order.dao.TempOrderDao;
import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.entity.TempOrder;
import com.modules.order.service.OrderService;
import com.modules.payment.model.dto.ChainCardRechargeDto;
import com.modules.payment.model.dto.PaymentCodeDto;
import com.modules.payment.model.dto.PaymentParamDto;
import com.modules.payment.model.entity.RechargeLog;
import com.modules.payment.model.vo.CardQrCodeVo;
import com.modules.payment.model.vo.PayResultVo;
import com.modules.payment.model.vo.WxPreOrderVo;
import com.modules.payment.model.vo.WxUnifiedResultVo;
import com.modules.payment.service.PaymentService;
import com.modules.payment.service.RechargeLogService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private OrderService orderService;
    @Resource
    private TempOrderDao tempOrderDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AliPayUtil aliPayUtil;
    @Resource
    private WxPayService wxPayService;
    @Resource
    private RechargeLogService rechargeLogService;
    @Resource
    private RedisLockUtils redisLockUtils;
    @Resource
    private UserMapper userMapper;

    private Long learnExpireTime = 86400000L;

    @Override
    public CardQrCodeVo generateQrCodeStr(PaymentCodeDto paymentCodeDto, String userId) {
        CardQrCodeVo result = new CardQrCodeVo();
        String content = "";
        String password = Constant.QRCODE_PASSWORD;
        User user = userMapper.selectByPrimaryKey(Long.parseLong(userId));
        if (user == null) {
            throw new CommonException("用户不存在!");
        }
        result.setBalance(user.getBalance());
        String qrCode = Constant.CROSS_QRCODE_PREFIX + AESUtil.encrypt(content, password);
        result.setQrCode(qrCode);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayResultVo pay(PaymentParamDto paramDto) throws Exception {
        //1.查询账户余额
        User user = userMapper.selectByPrimaryKey(paramDto.getUserId());
        if (user == null) {
        }

        TempOrder order = tempOrderDao.findTempOrder(paramDto.getOrderId());
        if (order == null) {
        }
        BigDecimal realCost = new BigDecimal("0");

        if (user.getBalance().compareTo(realCost) < 0) {
            throw new CommonException("账户余额不足!");
        }
        //2.扣款
        user.setBalance(user.getBalance().subtract(realCost));
        if (userMapper.updateByPrimaryKey(user) != 1) {
            throw new CommonException("当前会员卡扣款失败!");
        }

        //4.生成正式订单
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        BeanUtils.copyProperties(order, orderSaveDto);
        //支付信息
        orderSaveDto.setPaymentMode(String.valueOf(Constant.PaymentMode.CHAIN_CARD));
        orderSaveDto.setPayMemberId(user.getId());

        if (orderService.saveOrder(orderSaveDto) == null) {
            throw new CommonException("保存订单失败！");
        }
        // 记录充值信息
        //5.删除临时订单
        tempOrderDao.deleteTempOrder(paramDto.getOrderId());
        return null;
    }

    @Override
    public AlipayTradePrecreateResponse generateAliPayQr(Long orderID) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        TempOrder order = tempOrderDao.findTempOrder(orderID);
        //查询ali收款账户
        //AliAccount aliAccount = aliAccountDao.findAlAccount(Long.valueOf(staff.getRestaurantId()));
        if (user.getAliAccount() == null) {
            throw new CommonException("当前用户未绑定支付宝收款账号!");
        }
        try {
            //发送请求
            AlipayTradePrecreateResponse response =
                    aliPayUtil.tradePrecreate(String.valueOf(orderID),
                            user.getAppId(),
                            order.getTotalCost().toString(),
                            getOrderSubject(order, user),
                            String.valueOf(user.getId()),
                            String.valueOf(user.getId()),
                            Constant.ALiPay.NOTIFY_URL,
                            user.getAuthToken());
            order.setPaymentMode(Integer.valueOf(Constant.PaymentMode.ALI));
            tempOrderDao.update(order);
            return response;
        } catch (AlipayApiException e) {
            log.error("生成支付宝支付二维码出现错误", e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public AlipayTradePayResponse aliScanPay(Long orderId, String authCode) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        TempOrder order = tempOrderDao.findTempOrder(orderId);
        //查询ali收款账户
        // AliAccount aliAccount = aliAccountDao.findAlAccount(Long.valueOf(staff.getRestaurantId()));
        if (user.getAliAccount() == null) {
            throw new CommonException("当前用户未绑定支付宝付款账号!");
        }
        try {
            //发送交易请求
            AlipayTradePayResponse response =
                    aliPayUtil.scanPay(String.valueOf(orderId),
                            order.getTotalCost().toString(),
                            getOrderSubject(order, user),
                            user.getAuthToken(),
                            authCode);
            order.setPaymentMode(Integer.valueOf(Constant.PaymentMode.ALI));
            order.setAlipayId(response.getBuyerLogonId());
            afterPayOperation(order, user.getId());
            return response;
        } catch (AlipayApiException e) {
            log.error("支付宝扫码出现错误", e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public AlipayTradeQueryResponse queryAlipayOrder(Long orderID, Boolean isMerchantOrder) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        try {
            //得到授权码
            String auth = getAuthCode(user.getId(), isMerchantOrder);
            //发送查询支付宝订单请求
            AlipayTradeQueryResponse query = aliPayUtil.query(String.valueOf(orderID), auth);
            return query;
        } catch (AlipayApiException e) {
            log.error("查询支付宝订单出现错误", e);
            throw new CommonException("支付宝服务调用异常, 请联系管理员");
        }
    }

    @Override
    public AlipayTradeCloseResponse closeAliPayOrder(Long orderID, Long operatorID, Boolean isMerchantOrder) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        try {
            //得到授权码
            String authCode = getAuthCode(user.getId(), isMerchantOrder);
            //发送关闭订单请求
            AlipayTradeCloseResponse response = aliPayUtil.close(String.valueOf(orderID),
                    String.valueOf(operatorID), authCode);
            return response;
        } catch (AlipayApiException e) {
            log.error("关闭支付宝订单出现错误", e);
            throw new CommonException("支付宝服务调用异常, 请联系管理员");
        }
    }

    @Override
    public WxPayMicropayResult wxScanPay(Long orderID, String authCode, String ipAddr) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        TempOrder order = tempOrderDao.findTempOrder(orderID);
        //查询ali收款账户
        // WxAccount wxAccount = wxAccountDao.findById(Long.valueOf(staff.getRestaurantId()));
        if (user.getWxAccount() == null) {
            throw new CommonException("当前用户未绑定微信收款账号!");
        }
        try {
            //发送请求
            WxPayMicropayResult micropay = wxPayService.micropay(getWxScanPayRequest(order, user, ipAddr,
                    user.getMchId(), authCode));
            //设置用户标识存到数据库中 openID:用户在商户appid 下的唯一标识
            order.setWxOpenID(micropay.getOpenid());
            order.setPaymentMode(Integer.valueOf(Constant.PaymentMode.WX));
            tempOrderDao.update(order);
            return micropay;
        } catch (WxPayException e) {
            log.error("扫描微信收款二维码出现错误", e);
            order.setPaymentMode(Integer.valueOf(Constant.PaymentMode.WX));
            tempOrderDao.update(order);
            WxPayMicropayResult wxResult = new WxPayMicropayResult();
            wxResult.setOutTradeNo(order.getWxOrderID());
            wxResult.setErrCode(e.getErrCode());
            wxResult.setErrCodeDes(e.getErrCodeDes());
            return wxResult;
        }
    }

    @Override
    public WxUnifiedResultVo generateWxQr(Long orderID, String ipAddr) {
        User user = getUserInfo();
        if (user == null) {
            throw new CommonException("该员工不存在!");
        }
        TempOrder order = tempOrderDao.findTempOrder(orderID);
        //查询ali收款账户
        //WxAccount wxAccount = wxAccountDao.findById(Long.valueOf(staff.getRestaurantId()));
        if (user.getWxAccount() == null) {
            throw new CommonException("当前用户未绑定微信收款账号!");
        }
        try {
            //发送请求
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayService.unifiedOrder(getWxrequest(order, user, ipAddr, user.getMchId()));
            WxUnifiedResultVo wxUnifiedResult = new WxUnifiedResultVo();
            wxUnifiedResult.setOrderID(order.getId());
            wxUnifiedResult.setWxOrderID(order.getWxOrderID());
            wxUnifiedResult.setWxPayUnifiedOrderResult(wxPayUnifiedOrderResult);
            order.setPaymentMode(Integer.valueOf(Constant.PaymentMode.WX));
            tempOrderDao.update(order);
            return wxUnifiedResult;
        } catch (WxPayException e) {
            log.error("生成微信收款二维码出现错误", e);
            throw new CommonException("生成微信收款二维码出现错误!");
        }
    }

    @Override
    public WxPayOrderCloseResult closeWxOrder(String wxOrderId) {
        try {
            //根据微信订单ID关闭微信订单
            WxPayOrderCloseResult wxPayOrderCloseResult = wxPayService.closeOrder(wxOrderId);
            return wxPayOrderCloseResult;
        } catch (WxPayException e) {
            log.error("关闭微信订单出现错误", e);
            throw new CommonException("关闭微信订单出现错误!");
        }
    }

    @Override
    public WxPayOrderQueryResult queryWxOrder(String wxOrderId) {
        try {
            //根据微信订单ID查询订单
            WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, wxOrderId);
            return wxPayOrderQueryResult;
        } catch (WxPayException e) {
            log.error("查询微信收款订单出现错误", e);
            throw new CommonException("查询微信收款订单出现错误");
        }
    }

    @Override
    public String queryFKMOrder(String orderID) throws Exception {
        WXConfigUtil wxConfig = new WXConfigUtil();
        WXPay wxPay = new WXPay(wxConfig);
        Map<String, String> map = new HashMap<>();
        map.put("appid", wxConfig.getAppID());
        map.put("mch_id", wxConfig.getMchID());
        //TODO 修改为微信开放平台的参数
        map.put("sub_mch_id", "1603009731");
        map.put("out_trade_no", orderID);
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("sign", WXPayUtil.generateSignature(map, wxConfig.getKey(),
                WXPayConstants.SignType.MD5));
        WXPay wxpay = new WXPay(wxConfig, WXPayConstants.SignType.MD5, false);//查询订单
        Map<String, String> wxResult = new HashMap<>();
        wxResult = wxpay.orderQuery(map);//查询订单结果
        String result = wxResult.toString();
        return result;
    }

    private WxPreOrderVo copeWxResult(Map<String, String> wxResponse) {
        WxPreOrderVo vo = new WxPreOrderVo();
        WxPreOrderVo.Wechat wechat = new WxPreOrderVo.Wechat();
        wechat.setTimeStamp(wxResponse.get("timeStamp"));
        wechat.setPartnerId(wxResponse.get("mchId"));
        wechat.setPrepayId(wxResponse.get("prepayId"));
        wechat.setNonceStr(wxResponse.get("nonceStr"));
        wechat.setWpackage("Sign=WXPay");
        wechat.setSign(wxResponse.get("sign"));
        wechat.setAppId(wxResponse.get("appid"));
        vo.setWechat(wechat);
        return vo;
    }


    @Override
    public Object rechargeCrossCard(ChainCardRechargeDto param, Long userId, String ipAddr) {
        Map<String, Object> result = new HashMap<>();
        Long memberCardId = param.getCardId();
        //折扣金额获取
        //CardRechargeDiscount discount = discountService.findById(param.getId());
        //记录充值记录
        RechargeLog rechargeLog = saveCardRechargeLog(memberCardId, param.getType(), 0);
        //根据不同的方式调用不同的方法
        if ("0".equals(param.getType())) {
            AlipayTradeAppPayResponse aliResponse = resolveAliRechargeCross(rechargeLog);
            result.put("alipay", aliResponse.getBody());
            return result;
        }
        if ("1".equals(param.getType())) {
            Map<String, String> wxResponse = null;
//                Map<String, String> wxResponse = resolveWxRechargeMember(rechargeLog, card, ipAddr);
//                return copeWxResult(wxResponse);
            try {
                wxResponse = resolveWxRechargeCross(rechargeLog, ipAddr);
            } catch (WxPayException e) {
                log.error("微信充值跨店卡时发生错误!", e);
                throw new CommonException("微信充值跨店卡失败！");
            }
            return copeWxResult(wxResponse);
        }

        return null;
    }

    @Override
    public PayResultVo drawReward(Long orderId) {
        return null;
    }

    private RechargeLog saveCardRechargeLog(Long memberCardId, String payMode, Integer cardType) {
        RechargeLog param = new RechargeLog();
        /*param.setId(DBUtils.nextId());
        param.setRechargeAmount(discount.getThreshold());
        param.setRechargeReward(discount.getReward());
        param.setRewardReason(discount.getTitle());
        param.setCardType(cardType);
        param.setPaymentMode(payMode);
        param.setWxOrderID(RandomStrUtil.generateStr(32));
        param.setCreateTime(DBUtils.getSystemTime());
        param.setStatus(0);
        if (rechargeLogService.saveRechargeLog(param) != 1) {
            throw new CommonException("保存充值记录时发生错误！");
        }*/
        return param;
    }

    /**
     * 支付宝充值会员卡
     *
     * @param rechargeLog 充值记录
     * @param user        充值会员卡
     * @return
     */
    private AlipayTradeAppPayResponse resolveAliRechargeMember(RechargeLog rechargeLog, User user) {

        try {
            AlipayTradeAppPayResponse response =
                    aliPayUtil.appPay(String.valueOf(rechargeLog.getId()),
                            getMemberRechargeSubject(rechargeLog, user),
                            rechargeLog.getRechargeAmount().toString());
            return response;
        } catch (AlipayApiException e) {
            log.error("生成支付宝支付二维码出现错误", e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 支付宝充值跨店卡
     *
     * @param rechargeLog
     * @return
     */
    private AlipayTradeAppPayResponse resolveAliRechargeCross(RechargeLog rechargeLog) {

        try {
            AlipayTradeAppPayResponse response =
                    aliPayUtil.appPay(String.valueOf(rechargeLog.getId()),
                            getCrossRechargeSubject(rechargeLog),
                            rechargeLog.getRechargeAmount().toString());
            return response;
        } catch (AlipayApiException e) {
            log.error("生成支付宝支付二维码出现错误", e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 微信充值会员卡
     *
     * @param rechargeLog 充值记录
     * @param user        充值会员卡
     * @param ipAddr      终端IP
     * @return
     * @throws WxPayException
     */
    private Map<String, String> resolveWxRechargeMember(RechargeLog rechargeLog, User user, String ipAddr) {
        String nonStr = RandomStrUtil.generateStr(32);
        //订单总金额单位为分
//        BigDecimal total = rechargeLog.getRechargeAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN);
        //TODO 测试充值设置为1分钱
        BigDecimal total = new BigDecimal(1);
        Integer totalFee = Integer.valueOf(total.toString());
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setAppid(Constant.WxPay.APPID);
        //TODO 常量中的mchId是微亮的商户,如果需要可改为从表中查询mchId
        request.setMchId(Constant.WxPay.MCH_ID);
        request.setNonceStr(nonStr);
        //商品描述
        request.setBody(getMemberRechargeSubject(rechargeLog, user));
        request.setOutTradeNo(rechargeLog.getWxOrderId());
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(ipAddr);
        request.setNotifyUrl(Constant.WxPay.APP_NOTIFY_URL);
        request.setTradeType("APP");
        //调用接口
        WxPayUnifiedOrderResult response = null;
        try {
            response = wxPayService.unifiedOrder(request);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        Map<String, String> result = JSON.parseObject(JSON.toJSONString(response), Map.class);
        Map<String, String> map = new HashMap<>();
        String time = String.valueOf(DBUtils.getSystemTimeSecond());
        map.put("appid", result.get("appid"));
        map.put("partnerid", result.get("mchId"));
        map.put("prepayid", result.get("prepayId"));
        map.put("package", "Sign=WXPay");
        map.put("noncestr", result.get("nonceStr"));
        map.put("timestamp", time);
        String paySign = "";
        try {
            paySign = WXPayUtil.generateSignature(map, Constant.WxPay.KEY,
                    WXPayConstants.SignType.MD5);
        } catch (Exception e) {
            throw new CommonException("签名生成失败！");
        }
        result.put("timeStamp", time);
        result.put("sign", paySign);
        return result;
    }

    private Map<String, String> resolveWxRechargeCross(RechargeLog log, String ipAddr) throws WxPayException {
        String nonStr = RandomStrUtil.generateStr(32);
        //订单总金额单位为分
        BigDecimal total = log.getRechargeAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN);
        Integer totalFee = Integer.valueOf(total.toString());
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setAppid(Constant.WxPay.APPID);
        //TODO 常量中的mchId是微亮的商户,如果需要可改为从表中查询mchId
        request.setMchId(Constant.WxPay.MCH_ID);
        request.setNonceStr(nonStr);
        //通过证书生成sign
//        request.setSign(sign);
        //商品描述
        request.setBody(getCrossRechargeSubject(log));
        request.setOutTradeNo(log.getWxOrderId());
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(ipAddr);
        request.setNotifyUrl(Constant.WxPay.APP_NOTIFY_URL);
        request.setTradeType("APP");
        //调用接口
        WxPayUnifiedOrderResult response = null;
        try {
            response = wxPayService.unifiedOrder(request);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        Map<String, String> result = JSON.parseObject(JSON.toJSONString(response), Map.class);
        //返回结果
        Map<String, String> map = new HashMap<>();
        String time = String.valueOf(DBUtils.getSystemTimeSecond());
        map.put("appid", result.get("appid"));
        map.put("partnerid", result.get("mchId"));
        map.put("prepayid", result.get("prepayId"));
        map.put("package", "Sign=WXPay");
        map.put("noncestr", result.get("nonceStr"));
        map.put("timestamp", time);
        String paySign = "";
        try {
            paySign = WXPayUtil.generateSignature(map, Constant.WxPay.KEY,
                    WXPayConstants.SignType.MD5);
        } catch (Exception e) {
            throw new CommonException("签名生成失败！");
        }
        result.put("timeStamp", time);
        result.put("sign", paySign);
        return result;
    }

    private String getMemberRechargeSubject(RechargeLog rechargeLog, User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getNickname() == null ? "" : user.getNickname());
        sb.append("充值 ");
        sb.append(rechargeLog.getRechargeAmount());
        sb.append(" 元");
        sb.append("奖励 ");
        sb.append(rechargeLog.getRechargeReward());
        sb.append(" 元");
        return sb.toString();
    }

    private String getCrossRechargeSubject(RechargeLog rechargeLog) {
        StringBuilder sb = new StringBuilder();
        sb.append("跨店卡");
        sb.append("充值 ");
        sb.append(rechargeLog.getRechargeAmount());
        sb.append(" 元");
        sb.append("奖励 ");
        sb.append(rechargeLog.getRechargeReward());
        sb.append(" 元");
        return sb.toString();
    }

    private User getUserInfo() {
        Long userId = ServiceUtil.getUserInfo(request);
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 得到订单主题
     *
     * @param order
     * @param user
     * @return
     */
    private String getOrderSubject(TempOrder order, User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getNickname());
        sb.append("在");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(order.getCreateTime());
        sb.append(sdf.format(instance.getTime()));
        sb.append("的订单");
        return sb.toString();
    }

    /**
     * 获取AuthCode
     *
     * @param restID
     * @param restAuth 用于区分是否是自用账号
     * @return
     */
    private String getAuthCode(Long restID, Boolean restAuth) {
        /*if (restAuth) {
            AliAccount aliAccount = userMapper.findAliAccount(restID);
            return aliAccount.getAuthToken();
        } else {
            //TODO 此处值应为自用authcode
            return "";
        }*/
        return null;
    }

    /**
     * 根据传入参数组装微信请求
     *
     * @param order
     * @param user
     * @param ipAddr
     * @param subMchID
     * @param authCode
     * @return
     */
    private WxPayMicropayRequest getWxScanPayRequest(TempOrder order, User user,
                                                     String ipAddr, String subMchID, String authCode) {
        WxPayMicropayRequest request = new WxPayMicropayRequest();
        request.setSubMchId(subMchID);
        request.setOutTradeNo(order.getWxOrderID());
        request.setBody(getOrderSubject(order, user));
        request.setTotalFee((order.getTotalCost().intValue()));
        request.setSpbillCreateIp(ipAddr);
        request.setReceipt("Y");
        request.setAttach(Constant.WxPay.WX_PAY_TYPE_CONSUMPTION);
        request.setAuthCode(authCode);
        OrderSeller orderSeller = new OrderSeller();
        orderSeller.setSellerStaffID(user.getId());
        request.setSceneInfo(JSON.toJSONString(orderSeller));
        return request;
    }

    @Data
    private class OrderSeller {
        private Long sellerStaffID;
    }

    /**
     * 根据传入信息获得微信支付请求
     *
     * @param order
     * @param user
     * @param ipAddr
     * @param subMchID
     * @return
     */
    private WxPayUnifiedOrderRequest getWxrequest(TempOrder order, User user, String ipAddr, String subMchID) {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        //普通商户不允许传sub_mch_id
//        request.setSubMchId(subMchID);
        request.setOutTradeNo(order.getWxOrderID());
        request.setBody(getOrderSubject(order, user));
        request.setTotalFee((order.getTotalCost().intValue()));
        request.setNotifyUrl(Constant.WxPay.NOTIFY_URL);
        request.setTradeType("NATIVE");
        request.setProductId(order.getWxOrderID());
        request.setSpbillCreateIp(ipAddr);
        request.setAttach(Constant.WxPay.WX_PAY_TYPE_CONSUMPTION);
        OrderSeller orderSeller = new OrderSeller();
        orderSeller.setSellerStaffID(user.getId());
        request.setSceneInfo(JSON.toJSONString(orderSeller));
        return request;
    }

    /**
     * 支付成功后的操作
     * 包括保存正式订单信息，删除临时订单信息，保存就餐记录操作
     *
     * @param order
     * @param restaurantId
     */
    private void afterPayOperation(TempOrder order, Long restaurantId) {
        //1.生成正式订单
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        BeanUtils.copyProperties(order, orderSaveDto);

        if (orderService.saveOrder(orderSaveDto) == null) {
            throw new CommonException("保存订单失败！");
        }
        //2.生成充值记录
        // todo..
        //3.删除临时订单
        tempOrderDao.deleteTempOrder(order.getId());
    }

    /**
     * 微信获取沙箱key方法(测试微信支付用)
     *
     * @param nonStr
     * @return
     */
    private String getSandBoxSignKey(String nonStr) {
        Map<String, String> map = new HashMap<>();
        map.put("mch_id", Constant.WxPay.MCH_ID);
        map.put("nonce_str", nonStr);
        String xmlParam = null;
        String url = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
        try {
            xmlParam = WXPayUtil.generateSignedXml(map, Constant.WxPay.KEY,
                    WXPayConstants.SignType.MD5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = null;
        try {
            response = wxPayService.post(url, xmlParam, false);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        System.out.println("******************************" + response);
        Map<String, String> result = null;
        try {
            result = WXPayUtil.xmlToMap(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        String res = result.get("sandbox_signkey");
        return res;
    }


    /**
     * 得到会员卡加锁的Key
     *
     * @param memberID
     * @return
     */
    private String getMemberCardLockKey(String memberID) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.MEMBER_CARD_LOCK_);
        sb.append(memberID);
        return sb.toString();
    }
}
