package com.modules.order.service.impl;

import com.core.exception.CommonException;
import com.core.utils.DBUtils;
import com.core.utils.RandomStrUtil;
import com.core.utils.ServiceUtil;
import com.modules.gen.dao.UserMapper;
import com.modules.gen.model.entity.User;
import com.modules.order.dao.OrderDao;
import com.modules.order.dao.TempOrderDao;
import com.modules.order.model.entity.TempOrder;
import com.modules.order.service.TempOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TempOrderServiceImpl implements TempOrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private TempOrderDao tempOrderDao;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserMapper userMapper;

    @Override
    public Long saveTempOrder(String orderDetail) throws Exception {
        Long userId = ServiceUtil.getUserInfo(request);
        if (StringUtils.isEmpty(userId)) {
            throw new CommonException("员工未登录！");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new CommonException("当前员工不存在！");
        }
        TempOrder to = new TempOrder();
        Long id = DBUtils.nextId();
        to.setId(id);
        to.setCreateTime(DBUtils.getSystemTime());
        to.setOrderDetail(orderDetail);
        to.setWxOrderID(RandomStrUtil.generateStr(32));
        //1.计算订单总金额
        Map<Long, Integer> map = new HashMap<>();
        String[] strings = orderDetail.split(";");
        for(String str : strings) {
            String[] split = str.split(",");
            map.put(Long.valueOf(split[0]), Integer.valueOf(split[1]));
        }
        to.setTotalCost(user.getBalance().divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));

        //2.保存订单信息
        if (tempOrderDao.insert(to) != 1) {
            return null;
        }
        return id;
    }

    @Override
    public TempOrder findByWxId(String wxOrderId) {
        TempOrder order = null;
        try {
            order = tempOrderDao.findOrderForWx(wxOrderId);
        } catch (Exception e) {
            log.error("查询临时订单发生错误："+e.getMessage(), e);
            throw new CommonException("通过微信订单号查询临时订单发生错误！");
        }
        return order;
    }

    @Override
    public TempOrder findById(Long orderId) {
        TempOrder order = tempOrderDao.findTempOrder(orderId);
        return order;
    }

    @Override
    public void deleteTempOrder(Long orderId) {
        tempOrderDao.deleteTempOrder(orderId);
    }
}
