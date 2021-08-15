package com.modules.order.service;


import com.modules.order.model.entity.TempOrder;

public interface TempOrderService {

    Long saveTempOrder(String orderDetail) throws Exception;

    TempOrder findByWxId(String wxOrderId);

    TempOrder findById(Long orderId);

    void deleteTempOrder(Long orderId);

}
