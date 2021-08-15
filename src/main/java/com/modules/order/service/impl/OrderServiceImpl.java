package com.modules.order.service.impl;

import com.core.exception.CommonException;
import com.modules.order.dao.OrderDetailDao;
import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.entity.Order;
import com.modules.order.model.entity.OrderDetail;
import com.modules.order.model.vo.OrderVO;
import com.modules.order.service.OrderDetailService;
import com.modules.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDetailDao orderDetailDao;

    @Override
    public Long saveOrder(OrderSaveDto orderSaveDto) {
        return null;
    }

    @Override
    public OrderVO findOrderVO(Long orderId) throws Exception {
        return null;
    }

    @Override
    public void insertOrder(Order order) {
        int result = 0;
        try {
            result = orderDetailDao.insert(order);
        } catch (Exception e) {
            log.info("保存订单详情时SQL出现异常："+e.getMessage(), e);
            throw new CommonException("保存订单详情时SQL出现异常!");
        }
        if (result != 1) {
            throw new CommonException("保存订单详情信息失败！");
        }
    }
}
