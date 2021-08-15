package com.core.anotation;

import com.core.log.service.LogService;
import com.core.utils.IPUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.core.log.model.entity.Log;
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
public class LogComponent {

    @Autowired
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.version}")
    private String version;

    @Pointcut("@annotation(com.core.anotation.Logged)")
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
        Log newLog = new Log();

        // 注解上操作描述
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Logged annotation = method.getAnnotation(Logged.class);
        if (annotation != null) {
            newLog.setDescription(annotation.remark());
        }

        // 请求方法
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        newLog.setMethod(className + "." + methodName + "()");

        // 请求参数
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                String params = objectMapper.writeValueAsString(args[0]);
                newLog.setParams(params);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }

        // 获取request, 设置ip,url
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        newLog.setIp(IPUtils.getIpAddr(request));
        String url = request.getRequestURI();
        if (StringUtils.startsWith(url, "//")) {
            url = StringUtils.substring(newLog.getUrl(), 1);
        }
        newLog.setUrl(url);

        // userName
        //String userName = ((RedisSysUser) SecurityUtils.getSubject().getPrincipal()).getSysUser().getUserName();
        //sysLog.setUserName(userName);

        newLog.setCreateTime(new Date());
        newLog.setTime(executeTime);
        newLog.setVersion(version);

        // 记录系统日志
        logService.save(newLog);
    }

}
