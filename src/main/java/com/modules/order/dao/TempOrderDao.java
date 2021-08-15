package com.modules.order.dao;

import com.modules.order.model.entity.TempOrder;
import org.apache.ibatis.annotations.*;

/**
 * @Description:
 * @date: 2021/5/28/0028 13:17
 * @author: YuZHenBo
 */
@Mapper
public interface TempOrderDao {

    @Insert("INSERT INTO temp_order(id, order_detail, total_cost, create_time) VALUES(" +
            "#{tempOrder.id}, #{tempOrder.orderDetail}, #{tempOrder.totalCost}, #{tempOrder.createTime})")
    int saveTempOrder(@Param("tempOrder") TempOrder tempOrder);

    @Delete("delete from temp_order where id = #{id}")
    int deleteTempOrder(Long id);

    @Select("select * from temp_order where id = #{orderId}")
    TempOrder findTempOrder(Long orderId);

    @Select("select * from temp_order where wx_orderID = #{wxOrderId}")
    TempOrder findOrderForWx(String wxOrderId) throws Exception;

    int insert(TempOrder tempOrder);

    int update(TempOrder tempOrder);
}
