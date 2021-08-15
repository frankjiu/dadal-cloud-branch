package com.core.anotation;

import com.alibaba.fastjson.JSON;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.*;
import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.core.result.RespCode;
import com.core.utils.JWTUtils;
import com.core.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 权限检查
 * @Author: QiuQiang
 * @Date: 2021-05-25
 */
@Aspect
@Component
public class AuthAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Around("@annotation(com.core.anotation.AuthCheck)")
    public Object staffLoginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 从请求头获取 token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return HttpResult.fail(RespCode.NO_TOKEN);
        }
        // 解析 token 获取用户ID
        Claims claims;
        try {
            claims = jwtUtils.parseJWT(token);
        } catch (Exception e) {
            return HttpResult.fail(RespCode.AUTHENTICATION_FAILED);
        }
        JWTPayload payload = JSON.parseObject(claims.getSubject(), JWTPayload.class);
        String uid = String.valueOf(payload.getId());
        // 根据用户id从redis中获取 token
        Object redisToken = redisUtil.get(Constant.TOKEN_PREFIX + uid);
        if (StringUtils.isEmpty(redisToken)) {
            return HttpResult.fail(RespCode.UNAUTHORIZED);
        }
        // 比对token
        if (!redisToken.equals(token)) {
            return HttpResult.fail(RespCode.TOKEN_IS_RENEWED);
        }
        // 重置登录过期时间
        redisUtil.set(Constant.TOKEN_PREFIX + uid, redisToken, Constant.LOGIN_EXPIRE_TIME);
        Object userInfo = redisUtil.get(Constant.USER_PREFIX + uid);
        redisUtil.set(Constant.USER_PREFIX + uid, userInfo, Constant.LOGIN_EXPIRE_TIME);
        request.setAttribute(Constant.REQUEST_VERIFY_INFO, uid);
        return joinPoint.proceed();
    }

}
