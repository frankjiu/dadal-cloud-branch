package com.core.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-13
 */
@Slf4j
public class CommonFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String uri = req.getRequestURI();
        // 放行静态资源
        /*if (uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".jpg") || uri.endsWith("login")) {
            chain.doFilter(request, response);
            return;
        }*/

        String header = req.getHeader("user-agent");
        String remoteAddr = req.getRemoteAddr();

        long start = System.currentTimeMillis();
        // 将请求向后传递到controller进行执行
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>>>>>>>>> header:{}; remoteAddr:{}; uri:{}, cost:{}", header, remoteAddr, uri, end-start);

    }

    @Override
    public void destroy() {

    }
}
