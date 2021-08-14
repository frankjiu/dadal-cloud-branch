/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @author: Frankjiu
 * @date: 2020年8月29日
 * @version: V1.0
 */

package com.core.exception;

import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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
        /*boolean isAjax = isAjax(request);
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
        }*/

        // 如果是前后端分离, 只需做如下处理.
        log.error(e.getMessage(), e);
        return HttpResult.fail(RespCode.INTERNAL_ERROR);

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

    /**
     * Java常见异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public HttpResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return HttpResult.fail(RespCode.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HttpResult handleJsonParseException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return HttpResult.fail(RespCode.INVALID_PARAM);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public HttpResult handleJsonParseException(MismatchedInputException e) {
        log.error(e.getMessage(), e);
        return HttpResult.fail(RespCode.INVALID_PARAM);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public HttpResult handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return HttpResult.fail(RespCode.DUPLICATED);
    }

    /**
     * 请求方法错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public HttpResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        HttpResult r = new HttpResult(RespCode.METHOD_ERROR);
        r.setSuccess(false);
        return r;
    }

    /**
     * 参数绑定验证
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public HttpResult handleBindException(BindException e) {
        HttpResult r = new HttpResult(RespCode.INVALID_PARAM);
        r.setSuccess(false);
        String msg = null;
        FieldError field = e.getBindingResult().getFieldError();
        if (field != null) {
            msg = field.getField() + field.getDefaultMessage();
        }
        if (StringUtils.isBlank(msg)) {
            if (e.getBindingResult().getGlobalError() != null) {
                msg = e.getBindingResult().getGlobalError().getDefaultMessage();
            }
        }
        r.setMessage(msg);
        return r;
    }

    /**
     * 参数绑定验证
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult handleBindException(MethodArgumentNotValidException e) {
        HttpResult r = new HttpResult(RespCode.INVALID_PARAM);
        r.setSuccess(false);
        String msg = null;
        FieldError field = e.getBindingResult().getFieldError();
        if (field != null) {
            msg = field.getField() + field.getDefaultMessage();
        }
        if (StringUtils.isBlank(msg)) {
            if (e.getBindingResult().getGlobalError() != null) {
                msg = e.getBindingResult().getGlobalError().getDefaultMessage();
            }
        }
        r.setMessage(msg);
        return r;
    }


}
