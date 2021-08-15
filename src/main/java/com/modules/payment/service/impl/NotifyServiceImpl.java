package com.modules.payment.service.impl;

import cn.jpush.api.JPushClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.utils.DBUtils;
import com.core.utils.RedisLockUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.modules.gen.model.entity.User;
import com.modules.gen.service.UserService;
import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.entity.Order;
import com.modules.order.model.entity.TempOrder;
import com.modules.order.service.OrderService;
import com.modules.order.service.TempOrderService;
import com.modules.payment.model.entity.RechargeLog;
import com.modules.payment.service.NotifyService;
import com.modules.payment.service.RechargeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @date: 2021/6/18/0018 16:39
 * @author: YuZHenBo
 */
@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private RedisLockUtils redisLockUtils;
    @Resource
    private TempOrderService tempOrderService;
    @Resource
    private OrderService orderService;
    @Resource
    private RechargeLogService rechargeLogService;
    
    @Resource
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JPushClient jPushClient;

    //微信成功返回的值
    private static final String WX_SUC_RECEIVE = "<xml>\n" +
            "\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";

    private static final String ALI_SUC_RECEIVE = "SUCCESS";

    @Override
    public void wxPayNotify(String xmlData, HttpServletResponse response) {
        final WxPayOrderNotifyResult result;
        PrintWriter writer = null;
        String lockTime = null;
        String lockKey = null;
        try {
            writer = response.getWriter();
            result = this.wxPayService.parseOrderNotifyResult(xmlData);

            //官方建议采用数据锁进行并发控制，以避免函数重入造成的数据混乱
            String wxOrderID = result.getOutTradeNo();
            lockTime = String.valueOf(DBUtils.getSystemTime() + Constant.ORDER_NOTIFY_LOCK_TIME);
            lockKey = getOrderLockKey(wxOrderID);
            if (!redisLockUtils.lock(lockKey, lockTime)) {
                return;
            }

            String openID = result.getOpenid();
            //根据微信订单类型进行分类处理
            if ("SUCCESS".equals(result.getResultCode())) {
                switch (result.getAttach()) {
                    case Constant.WxPay.WX_PAY_TYPE_CONSUMPTION:
                        wxConsumptionResolve(wxOrderID, openID);
                        break;
                    //TODO 可以根据不同的类型进行差异化处理
                }
                writer.write(WX_SUC_RECEIVE);
            }
        } catch (WxPayException | IOException e) {
            log.error("微信支付回调出现未知错误", e);
        } finally {
            //刷新输出流，解锁
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            //释放锁
            redisLockUtils.unlock(lockKey, lockTime);
        }
    }

    @Override
    public void aliPayNotify(Map<String, String[]> input, HttpServletResponse response) {
        PrintWriter writer = null;
        String lockTime = null;
        String lockKey = null;

        //检查传入参数
        try {
            writer = response.getWriter();
            if (input == null || input.size() == 0) {
                writer.write("no parameter");
                return;
            }
            //验签
            Map<String, String> map = transferSingleValMap(input);
            if (!AlipaySignature.rsaCheckV1(map, Constant.ALiPay.APP_PUBLIC_KEY, Constant.ALiPay.CHARSET, "RSA2")) {
                log.warn("支付宝充值服务回调验签失败， 请检查参数 : {}", map);
                writer.write("signature failure");
                return;
            }
            //加锁
            String out_trade_no = map.get("out_trade_no");
            lockTime = String.valueOf(DBUtils.getSystemTime() + Constant.ORDER_NOTIFY_LOCK_TIME);
            lockKey = getOrderLockKey(out_trade_no);
            if (!redisLockUtils.lock(lockKey, lockTime)) {
                return;
            }
            String aliPayId = map.get("buyer_id");
            //查询充值订单
            long orderID = Long.parseLong(out_trade_no);
            Order record = orderService.findOrderVO(orderID);
            //存在订单信息，说明已经支付成功，不需要再次回调
            if (record != null) {
                writer.write(ALI_SUC_RECEIVE);
                return;
            }

            //根据状态分类处理
            switch (map.get("trade_status")) {
                case "TRADE_SUCCESS":
                    //保存订单信息，记录用餐记录等
                    aliConsumptionResolve(orderID, aliPayId);
                    writer.write("success");
                    break;
                case "TRADE_CLOSED":
                    //订单关闭不作处理，返回success终止阿里回调函数
                    writer.write("success");
                    break;
            }
            // 支付状态消息推送 todo: 待前端接入测试
            //pushMsg(aliPayId, out_trade_no);

        } catch (Exception e) {
            log.error("支付宝回调方法验签失败！");
            e.printStackTrace();
        } finally {
            //刷新输出流，解锁
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            redisLockUtils.unlock(lockKey, lockTime);
        }

    }

    @Override
    public void aliRechargeNotify(Map<String, String[]> input, HttpServletResponse response) {
        PrintWriter writer = null;
        String lockTime = null;
        String lockKey = null;
        log.info("支付宝回调参数:" + input);
        log.info("支付宝回调参数个数:" + input.size());
        //检查传入参数
        try {
            writer = response.getWriter();
            if (input == null || input.size() == 0) {
                writer.write("no parameter");
                return;
            }
            //验签
            Map<String, String> map = transferSingleValMap(input);
            log.warn("支付宝回调参数Map:" + map);
            if (!AlipaySignature.rsaCheckV1(map, Constant.ALiPay.ALIPAY_PUBLIC_KEY, Constant.ALiPay.CHARSET, "RSA2")) {
                log.warn("支付宝充值服务回调验签失败， 请检查参数 : {}", map);
                writer.write("signature failure");
                return;
            }
            //加锁
            String out_trade_no = map.get("out_trade_no");
            String buyer_pay_amount = map.get("buyer_pay_amount");
            String trade_status = map.get("trade_status");
            lockTime = String.valueOf(DBUtils.getSystemTime() + Constant.ORDER_NOTIFY_LOCK_TIME);
            lockKey = getOrderLockKey(out_trade_no);
            if (!redisLockUtils.lock(lockKey, lockTime)) {
                return;
            }
            //查询充值订单
            long orderID = Long.parseLong(out_trade_no);
            BigDecimal amount = new BigDecimal(buyer_pay_amount);
            RechargeLog rechargeLog = rechargeLogService.findById(orderID);
            if (rechargeLog == null) {
                return;
            }
            //存在订单信息，说明已经支付成功，不需要再次回调
            if (rechargeLog != null && "1".equals(rechargeLog.getStatus())) {
                writer.write(ALI_SUC_RECEIVE);
                return;
            }
            if (amount.compareTo(rechargeLog.getRechargeAmount()) != 0) {
                log.error("付款金额不符！");
                return;
            }
            //保存订单信息，充值记录等
            if ("TRADE_SUCCESS".equals(trade_status)) {
                aliRechargeResolve(orderID);
                writer.write("success");
            } else {
                return;
            }


        } catch (Exception e) {
            log.error("支付宝回调方法验签失败！");
            e.printStackTrace();
        } finally {
            //刷新输出流，解锁
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            redisLockUtils.unlock(lockKey, lockTime);
        }
    }

    @Override
    public void wxRechargeNotify(String xmlData, HttpServletResponse response) {
        final WxPayOrderNotifyResult result;
        PrintWriter writer = null;
        String lockTime = null;
        String lockKey = null;
        try {
            writer = response.getWriter();
            result = this.wxPayService.parseOrderNotifyResult(xmlData);

            //官方建议采用数据锁进行并发控制，以避免函数重入造成的数据混乱
            String wxOrderID = result.getOutTradeNo();
            lockTime = String.valueOf(DBUtils.getSystemTime() + Constant.ORDER_NOTIFY_LOCK_TIME);
            lockKey = getOrderLockKey(wxOrderID);
            if (!redisLockUtils.lock(lockKey, lockTime)) {
                return;
            }
            //TODO （目前参数获取不到）签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
            //根据微信订单类型进行分类处理
            if ("SUCCESS".equals(result.getResultCode())) {
                wxRechargeResolve(wxOrderID);
                writer.write(WX_SUC_RECEIVE);
            }

        } catch (WxPayException | IOException e) {
            log.error("微信支付回调出现未知错误", e);
        } finally {
            //刷新输出流，解锁
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            //释放锁
            redisLockUtils.unlock(lockKey, lockTime);
        }
    }

    /**
     * 组装Key
     *
     * @param memberID
     * @return
     */
    private String getOrderLockKey(String memberID) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.ORDER_WXPAY_LOCK_);
        sb.append(memberID);
        return sb.toString();
    }

    /**
     * 转为单值的Map
     *
     * @param map
     * @return
     */
    public Map<String, String> transferSingleValMap(Map<String, String[]> map) {
        Map<String, String> ret = new HashMap<>();
        Set<String> keys = map.keySet();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String key = iter.next();

            ret.put(key, map.get(key)[0]);
        }
        return ret;
    }

    /**
     * 微信消费处理
     *
     * @param wxOrderID
     */
    private void wxConsumptionResolve(String wxOrderID, String openId) {
        //得到消费订单
        TempOrder order = tempOrderService.findByWxId(wxOrderID);
        if (order == null) {
            throw new CommonException("未查询到订单");
        }
        //1.生成正式订单
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        BeanUtils.copyProperties(order, orderSaveDto);

        if (orderService.saveOrder(orderSaveDto) == null) {
            throw new CommonException("保存订单失败！");
        }
        //2.充值记录
        // todo...
        //3.删除临时订单
        tempOrderService.deleteTempOrder(order.getId());
    }

    private void aliConsumptionResolve(Long orderId, String aliPayId) {
        //得到消费订单
        TempOrder order = tempOrderService.findById(orderId);
        if (order == null) {
            throw new CommonException("未查询到订单");
        }
        //1.生成正式订单
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        BeanUtils.copyProperties(order, orderSaveDto);

        if (orderService.saveOrder(orderSaveDto) == null) {
            throw new CommonException("保存订单失败！");
        }
        //2.充值记录
        // todo...
        //3.删除临时订单
        tempOrderService.deleteTempOrder(order.getId());
    }

    private void aliRechargeResolve(Long orderId) {
        //获取充值记录
        RechargeLog rechargeLog = rechargeLogService.findById(orderId);
        //修改会员卡余额(此时卡一定存在,在调用支付接口时已经创建了新卡)
        User user = userService.findById(rechargeLog.getUserId());
        user.setBalance(user.getBalance().add(rechargeLog.getRechargeAmount()).add(rechargeLog.getRechargeReward()));
        //userService.rechargeCardNoLog(param);
        //修改充值记录状态
        RechargeLog log = new RechargeLog();
        log.setStatus(1);
        log.setId(rechargeLog.getId());
        rechargeLogService.updateRechargeLog(log);
    }

    private void wxRechargeResolve(String orderId) {
        //获取充值记录
        RechargeLog rechargeLog = rechargeLogService.findByWxOrderId(orderId);
        //修改用户账户余额
        User user = userService.findById(rechargeLog.getUserId());
        user.setBalance(user.getBalance().add(rechargeLog.getRechargeAmount()).add(rechargeLog.getRechargeReward()));
        //userService.rechargeCardNoLog(param);
        //修改充值记录状态
        RechargeLog log = new RechargeLog();
        log.setStatus(1);
        log.setId(rechargeLog.getId());
        rechargeLogService.updateRechargeLog(log);
    }

}
