package com.modules.sys.syslog.controller;

import com.core.anotation.SysLogged;
import com.core.utils.IPUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modules.sys.syslog.model.entity.SysLog;
import com.modules.sys.syslog.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志处理
 */
@Aspect
@Component
@Slf4j
public class SysLogComponent {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.version}")
    private String version;

    @Pointcut("@annotation(com.core.anotation.SysLogged)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行时间
        long beginTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executeTime = System.currentTimeMillis() - beginTime;
        save(joinPoint, executeTime);
        return proceed;
    }

    private void save(ProceedingJoinPoint joinPoint, long executeTime) throws Exception {
        SysLog sysLog = new SysLog();

        // 注解上操作描述
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SysLogged annotation = method.getAnnotation(SysLogged.class);
        if (annotation != null) {
            sysLog.setDescription(annotation.description());
        }

        // 请求方法
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysLog.setMethod(className + "." + methodName + "()");

        // 请求参数
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                String params = objectMapper.writeValueAsString(args[0]);
                sysLog.setParams(params);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }

        // 获取request, 设置ip,url
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setIp(IPUtils.getIpAddr(request));
        String url = request.getRequestURI();
        if (StringUtils.startsWith(url, "//")) {
            url = StringUtils.substring(sysLog.getUrl(), 1);
        }
        sysLog.setUrl(url);

        // userName
        //String userName = ((RedisSysUser) SecurityUtils.getSubject().getPrincipal()).getSysUser().getUserName();
        //sysLog.setUserName(userName);

        sysLog.setCreateTime(new Date());
        sysLog.setTime(executeTime);
        sysLog.setVersion(version);

        // 记录系统日志
        sysLogService.save(sysLog);
    }

}
