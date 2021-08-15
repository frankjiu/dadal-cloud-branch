package com.modules.order.dao;

import com.modules.order.model.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @date: 2021/5/24/0024 11:21
 * @author: YuZHenBo
 */
@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `order`(id, total_cost, create_time, delete_time, payment_mode, " +
            "pay_member_id, alipay_id, wx_openID, wx_orderID, status) VALUES(" +
            "#{order.id}, #{order.totalCost}, #{order.createTime}, #{order.deleteTime}, " +
            "#{order.paymentMode}, #{order.payMemberId}, #{order.alipayId}, #{order.wxOpenID}, " +
            "#{order.wxOrderID}, #{order.status})")
    int saveOrder(@Param("order") Order order);

    int updateOrder(@Param("order") Order order);

    @Select("select * from `order` where id = #{id}")
    Order findOrder(Long id);

    int insertOrder(Order order) throws Exception;

    @Select("select * from `order` where restaurant_id = #{restaurantId} order by create_time desc")
    List<Order> findOrderByRestId(@Param("restaurantId") Long restaurantId);
}
