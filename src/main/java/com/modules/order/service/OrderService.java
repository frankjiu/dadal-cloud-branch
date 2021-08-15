package com.modules.order.service;

import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.entity.Order;
import com.modules.order.model.vo.OrderVO;

/**
 * @Description:
 * @date: 2021/5/24/0024 10:35
 * @author: YuZHenBo
 */
public interface OrderService {

    Long saveOrder(OrderSaveDto orderSaveDto);

    OrderVO findOrderVO(Long orderId) throws Exception;

    void insertOrder(Order order);

}
