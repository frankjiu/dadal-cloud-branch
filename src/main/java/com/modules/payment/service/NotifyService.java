package com.modules.payment.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description:
 * @date: 2021/6/18/0018 16:39
 * @author: YuZHenBo
 */
public interface NotifyService {

    void wxPayNotify(String xmlData, HttpServletResponse response);

    void aliPayNotify(Map<String, String[]> input, HttpServletResponse response);

    void aliRechargeNotify(Map<String, String[]> input, HttpServletResponse response);

    void wxRechargeNotify(String xmlData, HttpServletResponse response);
}
