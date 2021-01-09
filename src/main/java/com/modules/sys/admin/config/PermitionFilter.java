package com.modules.sys.admin.config;

import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.HttpContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限过滤器
 */
@Slf4j
public class PermitionFilter extends AuthenticatingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return new PermitionToken(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            String accept = ((HttpServletRequest) request).getHeader("Accept");
            HttpResult result = HttpResult.fail(RespCode.TOKEN_ERROR);
            String body = objectMapper.writeValueAsString(result);
            String contentType = "application/json;charset=utf-8";
            //只支持xml和json的响应格式，默认为json格式
            if ("application/xml".equalsIgnoreCase(accept)) {
                XmlMapper xmlMapper = new XmlMapper();
                body = xmlMapper.writeValueAsString(result);
                contentType = "application/xml;charset=utf-8";
            }
            httpResponse.setHeader("Content-Type", contentType);
            httpResponse.getWriter().print(body);
            return false;
        }
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            HttpResult result = HttpResult.fail(RespCode.AUTHORIZATION_ERROR);
            String json = objectMapper.writeValueAsString(result);
            httpResponse.getWriter().print(json);
        } catch (IOException ex) {
        }

        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;
    }

    public static void main(String[] args) {
        System.out.println(259198 / 60 / 60 / 24);
    }

}
