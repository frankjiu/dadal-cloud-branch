package com.modules.order.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @date: 2021/7/9/0009 15:53
 * @author: YuZHenBo
 */
@Data
public class OrderCreateDto {

    @NotNull(message = "orderDetail未传入")
    private String orderDetail;
}
