package com.modules.payment.service;

import com.modules.payment.model.entity.RechargeLog;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021/8/15 14:31
 */
public interface RechargeLogService {

    RechargeLog findById(long orderID);

    RechargeLog findByWxOrderId(String orderId);

    void updateRechargeLog(RechargeLog log);

    void deleteById(Long orderID);
}
