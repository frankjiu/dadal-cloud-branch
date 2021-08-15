package com.modules.order.dao;

import com.modules.order.model.entity.Order;
import com.modules.order.model.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailDao {

    @Insert("INSERT INTO order(id, food_id, number, total_price, create_time)" +
            "VALUES(#{order.id}, #{order.foodId}, #{order.number}, " +
            "#{order.totalPrice}, #{order.createTime})")
    int saveOrder(@Param("order") Order order);

    @Select("select * from order where id = #{orderId}")
    List<OrderDetail> listOrder(Long orderId);

    int insert(Order order) throws Exception;
}
