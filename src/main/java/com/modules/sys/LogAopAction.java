/*
package com.modules.sys;

import ch.qos.logback.classic.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-18
 */

/*
@Aspect
@Component
public class LogAopAction {
    private final Logger logger = LoggerFactory.getLogger(LogAopAction.class);
    @Autowired
    private OperatorLogService operatorLogService;
    @Autowired
    private OperationUserService userService;

    @Pointcut("@annotation(com.yitao.cms.config.aopLog.LogAnnotation)")//配置切点
    private void pointCutMethod() {
    }

    //around可获取请求和返回参数!
    @AfterReturning(value = "pointCutMethod()",returning = "rtv")  //执行后操作
    public void after(JoinPoint joinPoint, Object rtv) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        OperationUserCmsModel userSession = UserSession.getUserSession(request);
        if (userSession != null) {
            //获取请求参数
            String beanName = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String url = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String requestMethod = request.getMethod();
            try {
                String targetName = joinPoint.getTarget().getClass().getName();
                Class targetClass = Class.forName(targetName);
                Method[] methods = targetClass.getMethods();
                OperatorLogDto optLog = new OperatorLogDto();
                //请求参数
                if (request.getParameterMap()!=null && request.getParameterMap().isEmpty()!=true) {//dan
                    optLog.setRequestPara(JSON.toJSONString(request.getParameterMap()));
                }else {
                    Object[] object = joinPoint.getArgs();
                    if (object.length>1){
                        optLog.setRequestPara(JSON.toJSONString(object[1]));
                    }
                }
                optLog.setResponsePara(JSON.toJSONString(rtv));//返回参数
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
                        if (logAnnotation != null) {
                            optLog.setDescription(logAnnotation.remark());
                            optLog.setTargetType(logAnnotation.targetType());

                        }
                    }
                }
                optLog.setOperatorUrl(url);
                optLog.setMethodName(methodName);
                optLog.setRemoteAddr(remoteAddr);
                operatorLogService.operatorLogAdd(optLog, request);
            } catch (Exception e) {
                logger.error("***操作请求日志记录失败doBefore()***", e);
            }
        }
    }*/
