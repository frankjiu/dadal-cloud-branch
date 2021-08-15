package com.modules.order.model.vo;

import com.modules.order.model.entity.Order;
import com.modules.order.model.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO extends Order {

    private List<OrderDetail> orderDetailList;

    public OrderVO(Order order, List<OrderDetail> detailList) {
        setId(order.getId());
        setTotalCost(order.getTotalCost());
        setCreateTime(order.getCreateTime());
        setDeleteTime(order.getDeleteTime());
        setPaymentMode(order.getPaymentMode());
        setPayMemberId(order.getPayMemberId());
        setAlipayId(order.getAlipayId());
        setWxOpenID(order.getWxOpenID());
        setWxOrderID(order.getWxOrderID());
        setStatus(order.getStatus());
        setOrderDetailList(detailList);
    }

}
