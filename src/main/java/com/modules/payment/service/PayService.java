package com.modules.payment.service;

import com.modules.payment.model.vo.PayResultVo;

/**
 * @Description:
 * @date: 2021/7/5/0005 17:21
 * @author: YuZHenBo
 */
public interface PayService {

    Object rechargeMember(Long id, Long money, String payMode, String ipAddr);

    void closeOrder(Long orderID);

    Object userPay(Long orderID, Long id);

    PayResultVo checkOrderStatus(Long orderID);
}
