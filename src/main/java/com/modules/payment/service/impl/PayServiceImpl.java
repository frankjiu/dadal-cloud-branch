package com.modules.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.utils.*;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.modules.gen.dao.UserMapper;
import com.modules.gen.model.entity.User;
import com.modules.order.dao.TempOrderDao;
import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.entity.Order;
import com.modules.order.model.entity.TempOrder;
import com.modules.order.service.OrderService;
import com.modules.order.service.TempOrderService;
import com.modules.payment.model.entity.RechargeLog;
import com.modules.payment.model.vo.PayResultVo;
import com.modules.payment.model.vo.WxUnifiedResultVo;
import com.modules.payment.service.PayService;
import com.modules.payment.service.PaymentService;
import com.modules.payment.service.RechargeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @date: 2021/7/5/0005 17:22
 * @author: YuZHenBo
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Resource
    private RechargeLogService rechargeLogService;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private AliPayUtil alipayUtils;
    @Autowired
    private WxPayService wxService;
    @Autowired
    private TempOrderService tempOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisLockUtils redisLockUtils;
    @Resource
    private TempOrderDao tempOrderDao;
    @Resource
    private PaymentService paymentService;

    private Long learnExpireTime = 86400000L;

    @Override
    public Object rechargeMember(Long id, Long money, String payMode, String ipAddr) {
        //检查充值金额
        if (!ServiceCmnUtil.priceLimit(money)) {
            throw new CommonException("充值金额超出上限!");
        }
        //查询会员卡信息
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new CommonException("用户不存在!");
        }
        //检查支付渠道
        if (!Constant.ChargeType.PAYMENT_MODE_ALI.equals(payMode) &&
                !Constant.ChargeType.PAYMENT_MODE_WX.equals(payMode)) {
            throw new CommonException("充值渠道只能是支付宝或微信!");
        }
        //根据支付渠道分别处理
        RechargeLog rechargeRecord = insertMemberRechargeRecord(user, money, payMode);
        switch (payMode) {
            case Constant.ChargeType.PAYMENT_MODE_ALI:
                return resolveAliRechargeMember(rechargeRecord, user);
            case Constant.ChargeType.PAYMENT_MODE_WX:
                return resolveWxRechargeMember(rechargeRecord, user, ipAddr);
        }
        return false;
    }

    @Override
    public void closeOrder(Long orderID) {
        //删除订单信息
        TempOrder order = tempOrderService.findById(orderID);
        if (order != null) {
            tempOrderService.deleteTempOrder(orderID);
        }
        //删除充值记录
        RechargeLog rechargeLog = rechargeLogService.findById(orderID);
        if (rechargeLog != null) {
            rechargeLogService.deleteById(orderID);
        }
    }

    private RechargeLog insertMemberRechargeRecord(User user, Long money, String payMode) {
        //得到充值记录
        /*RechargeLog record = new RechargeLog();
        record.setCreateTime(DBUtils.getSystemTime());
        record.setId(DBUtils.nextId());
        record.setPaymentMode(payMode);
        record.setRechargeAmount(new BigDecimal(money));
        record.setRechargeMemberId(memberCard.getId());
        record.setStatus(CommonConst.ORDER_STATUS_CREATE);
        record.setWxOrderID(CmnUtils.generateUUID());
        Long staffID = Long.valueOf(String.valueOf(request.getAttribute(CommonConst.REQUEST_VERIFY_INFO)));
        User user = userMapper.selectByPrimaryKey(staffID);
        record.setRestaurantId(staff.getRestaurantId());
        //查询充值折扣
        List<CardRechargeDiscount> memberRechargeDiscounts =
                rechargeDiscountDao.findCardDiscount(restaurant.getChainId());
        List<Long> discountReason = new ArrayList<>();
        //获得充值奖励
        Long reward = getReward(memberRechargeDiscounts, discountReason, money);
        //插入db
        record.setRechargeReward(new BigDecimal(reward));
        record.setRewardReason(StrUtil.join(",", discountReason));
        cardRechargeLogDao.saveRechargeLog(record);
        return record;*/
        return null;
    }

    /**
     * 支付宝充值
     */
    private Object resolveAliRechargeMember(RechargeLog rechargeRecord, User user) {
        if (user == null) {
            throw new CommonException("未设置支付宝收款账号, 请联系运维人员!");
        }
        try {
            //TODO 支付回调函数
            AlipayTradePrecreateResponse response = alipayUtils.tradePrecreate(String.valueOf(rechargeRecord.getId()),
                    CmnUtils.computeCost(rechargeRecord.getRechargeAmount().longValue()),
                    getMemberRechargeSubject(rechargeRecord, user), Constant.ALiPay.MEMBER_NOTIFY_URL,
                    user.getAuthToken());
            return response;
        } catch (AlipayApiException e) {
            log.error("会员充值服务支付宝二维码生成错误", e);
            throw new CommonException("支付宝服务调用异常, 请联系管理员!");
        }
    }

    /**
     * 使用微信渠道充值会员
     *
     * @param rechargeRecord
     * @param user
     * @param ipAddr
     * @return
     */
    private Object resolveWxRechargeMember(RechargeLog rechargeRecord, User user, String ipAddr) {
        try {
            WxPayUnifiedOrderResult response = generateWxMemberOrder(rechargeRecord, ipAddr, user.getMchId(), user);
            WxUnifiedResultVo wxUnifiedResult = new WxUnifiedResultVo();
            wxUnifiedResult.setOrderID(rechargeRecord.getId());
            wxUnifiedResult.setWxOrderID(rechargeRecord.getWxOrderId());
            wxUnifiedResult.setWxPayUnifiedOrderResult(response);
            return wxUnifiedResult;
        } catch (WxPayException e) {
            log.error("充值服务微信二维码生成错误", e);
            throw new CommonException("充值服务微信二维码生成错误");
        }
    }

    /**
     * 得到会员卡充值标题
     */
    private String getMemberRechargeSubject(RechargeLog rechargeRecord, User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getNickname());
        sb.append("充值 ");
        sb.append(CmnUtils.computeCost(rechargeRecord.getRechargeAmount().longValue()));
        sb.append(" 元");
        sb.append("奖励 ");
        sb.append(CmnUtils.computeCost(rechargeRecord.getRechargeReward().longValue()));
        sb.append(" 元");
        return sb.toString();
    }

    /**
     * 生成微信充值会员订单
     *
     * @param record
     * @param ipAddr
     * @param mch_id
     * @param user
     * @return
     * @throws WxPayException
     */
    private WxPayUnifiedOrderResult generateWxMemberOrder(RechargeLog record, String ipAddr, String mch_id,
                                                          User user) throws WxPayException {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(record.getWxOrderId());
        request.setTotalFee(record.getRechargeAmount().intValue());
        request.setBody(getMemberRechargeSubject(record, user));
        request.setSpbillCreateIp(ipAddr);
        request.setNotifyUrl(Constant.WxPay.MEMBER_NOTIFY_URL);
        request.setTradeType("NATIVE");
        request.setProductId(record.getWxOrderId());
        request.setSubMchId(mch_id);
        request.setAttach(Constant.WX_PAY_TYPE_MEMBER_RECHARGE);
        return wxService.unifiedOrder(request);
    }

    @Override
    public Object userPay(Long orderID, Long id) {
        //查询订单信息
        TempOrder tempOrder = tempOrderService.findById(orderID);
        if (tempOrder == null) {
            throw new CommonException("临时订单不存在!");
        }
        Order order = null;
        try {
            order = orderService.findOrderVO(orderID);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>校验订单状态时查询订单发生错误！");
        }
        //检查订单状态
        if (order != null) {
            throw new CommonException("订单状态异常!");
        }
        //查询用户
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new CommonException("用户不存在!");
        }
        //检查用户账户余额
        if (user.getBalance().compareTo(tempOrder.getTotalCost()) < 0) {
            throw new CommonException("用户账户约不足!");
        }
        String lockTime = String.valueOf(DBUtils.getSystemTime() + learnExpireTime);
        String lockKey = getMemberCardLockKey(String.valueOf(id));
        try {
            //加锁
            if (!redisLockUtils.lock(lockKey, lockTime)) {
                throw new CommonException("锁定操作失败!");
            }
            //扣钱
            //2.扣款
            user.setBalance(user.getBalance().subtract(tempOrder.getTotalCost()));
            if (userMapper.updateByPrimaryKey(user) != 1) {
                throw new CommonException("当前会员卡扣款失败!");
            }
            //3.生成正式订单，生成充值记录
            OrderSaveDto orderSaveDto = new OrderSaveDto();
            BeanUtils.copyProperties(order, orderSaveDto);
            //支付信息
            orderSaveDto.setPayMemberId(user.getId());
            if (orderService.saveOrder(orderSaveDto) == null) {
                throw new CommonException("保存订单失败！");
            }
            // 记录充值信息
            // todo..
            //5.删除临时订单
            tempOrderDao.deleteTempOrder(orderID);
        } catch (Exception e) {
            throw new CommonException("支付失败");
        } finally {
            redisLockUtils.unlock(lockKey, lockTime);
        }
        return true;
    }

    @Override
    public PayResultVo checkOrderStatus(Long orderID) {
        Order order = null;
        PayResultVo response = null;
        try {
            order = orderService.findOrderVO(orderID);
        } catch (Exception e) {
            log.error("查询订单支付结果时发生错误！");
        }
        if (order == null) {
            throw new CommonException("订单状态异常!");
        } else {
            response = paymentService.drawReward(orderID);
        }
        return response;
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
