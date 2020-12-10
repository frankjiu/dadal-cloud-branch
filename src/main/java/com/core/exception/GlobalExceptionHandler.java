/**
 * All rights Reserved, Designed By www.xcompany.com  
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.core.exception;

import com.core.result.HttpResult;
import com.core.result.RespCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: Global Exception Handler
 * @author: Frankjiu
 * @date: 2020年8月29日
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 终极异常处理
     */
    @ExceptionHandler(Exception.class)
    public Object defaultExceptionHandle(HttpServletRequest request, Exception e) {
        boolean isAjax = isAjax(request);
        if (isAjax) {
            log.info(e.getMessage(), e);
            HttpResult<?> result = HttpResult.fail(RespCode.INTERNAL_ERROR);
            return result;
        } else {
            log.info(e.getMessage(), e);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", e.getMessage()).addObject("url", request.getRequestURL())
                    .addObject("stackTrace", e.getStackTrace()).setViewName("error");
            return modelAndView;
        }
    }

    /**
     * 判断是否为ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        String contentTypeHeader = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        return (contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(CommonException.class)
    @SuppressWarnings("unchecked")
    public HttpResult<Object> commonExceptionHandle(CommonException e) {
        log.info(e.getMessage(), e);
        return HttpResult.fail(RespCode.FAIL, e.getMessage());
    }

}
