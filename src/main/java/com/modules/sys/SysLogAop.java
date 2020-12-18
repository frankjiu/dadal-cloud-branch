/*
package com.modules.sys;

import com.csair.rms.ramp.back.core.models.entity.RedisSysUser;
import com.csair.rms.ramp.back.core.utils.HttpContextUtils;
import com.csair.rms.ramp.back.core.utils.IPUtils;
import com.csair.rms.ramp.back.modules.sys.admin.model.entity.SysLog;
import com.csair.rms.ramp.back.modules.sys.admin.service.SysLogService;
import com.csair.rms.ramp.back.modules.sys.admin.service.SysMenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 系统日志，切面处理类
 *
 * @author chenshun
 */
/*
@Aspect
@Component
public class SysLogAop {
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${com.csair.build.version}")
    private String version;

    @Pointcut("@annotation(com.csair.rms.ramp.back.core.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        com.csair.rms.ramp.back.core.annotation.SysLog syslog = method.getAnnotation(com.csair.rms.ramp.back.core.annotation.SysLog.class);
        if(syslog != null){
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            try{
                String params = objectMapper.writeValueAsString(args[0]);
                sysLog.setParams(params);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        sysLog.setUrl(request.getRequestURI());// 请求URL
        if (StringUtils.startsWith(sysLog.getUrl(), "//")) {
            sysLog.setUrl(StringUtils.substring(sysLog.getUrl(), 1));
        }

        // URL对应的菜单名称
        sysLog.setName(sysMenuService.findNameByUrl(sysLog.getUrl()));

        //用户名
        String username = ((RedisSysUser) SecurityUtils.getSubject().getPrincipal()).getSysUser().getUsername();
        sysLog.setUsername(username);

        sysLog.setTime(time);
        sysLog.setCreateDate(System.currentTimeMillis()/1000);
        sysLog.setVersion(version);
        //保存系统日志
        sysLogService.save(sysLog);
    }
}
*/
