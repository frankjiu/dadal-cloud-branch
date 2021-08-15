package com.modules.payment.service.impl;

import com.modules.payment.model.entity.RechargeLog;
import com.modules.payment.service.RechargeLogService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-15
 */
@Service
public class RechargeLogServiceImpl implements RechargeLogService {

    @Override
    public RechargeLog findById(long orderID) {
        return null;
    }

    @Override
    public RechargeLog findByWxOrderId(String orderId) {
        return null;
    }

    @Override
    public void updateRechargeLog(RechargeLog log) {

    }

    @Override
    public void deleteById(Long orderID) {

    }

}
