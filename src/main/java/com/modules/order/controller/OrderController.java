package com.modules.order.controller;

import com.core.anotation.AuthCheck;
import com.core.anotation.Logged;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.modules.order.model.dto.OrderCreateDto;
import com.modules.order.model.dto.OrderSaveDto;
import com.modules.order.model.vo.OrderParamVo;
import com.modules.order.model.vo.OrderVO;
import com.modules.order.service.OrderService;
import com.modules.order.service.TempOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description:
 * @date: 2021/5/24/0024 10:06
 * @author: YuZHenBo
 */
@RestController
@RequestMapping("/order")
@Api(tags = "04-订单")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TempOrderService tempOrderService;

    @Logged(type = Constant.Log.TYPE_ADD, module = Constant.Log.MODULE_ORDER, remark = "保存临时订单")
    @ApiOperation(value = "创建订单(设备)", notes = "设备调用")
    @AuthCheck
    @PostMapping("/createOrder")
    public HttpResult saveTempOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        OrderParamVo result = new OrderParamVo();
        Long id = null;
        try {
            id = tempOrderService.saveTempOrder(orderCreateDto.getOrderDetail());
            result.setOrderId(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return HttpResult.success(id);
    }

    @AuthCheck
    @Logged(type = Constant.Log.TYPE_ADD, module = Constant.Log.MODULE_ORDER, remark = "保存订单信息")
    @ApiOperation(value = "保存订单信息", notes = "菜品识别设备传入参数")
    @PostMapping("/save")
    public HttpResult<OrderParamVo> saveOrder(@Valid @RequestBody OrderSaveDto orderSaveDto) {
        OrderParamVo result = new OrderParamVo();
        Long id = null;
        try {
            id = orderService.saveOrder(orderSaveDto);
            if (id == null) {
                return HttpResult.fail("保存订单信息失败！");
            }
            result.setOrderId(id);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return HttpResult.fail(e.getMessage());
        }
        return HttpResult.success(result);
    }

    @Logged(type = Constant.Log.TYPE_QUERY, module = Constant.Log.MODULE_ORDER, remark = "查询订单信息")
    @AuthCheck
    @ApiOperation(value = "查询订单信息")
    @PostMapping("/findOrderVO")
    public HttpResult<OrderVO> findOrderVO(Long orderId) {
        OrderVO result = new OrderVO();
        try {
            result = orderService.findOrderVO(orderId);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return HttpResult.fail("查询订单错误！");
        }
        return HttpResult.success(result);
    }


}
