package com.modules.sys.permission.shiro;

import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 自定义Realm
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userSerivce;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        log.info(">>>>>>>>>>>>>>>开始认证>>>>>>>>>>>>...");
        //编写shiro判断逻辑，判断用户名和密码
        //1.判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) arg0;
        User user = userSerivce.findByUserName(token.getUsername());
        if (user == null) {
            //用户名不存在
            return null;//shiro底层会抛出 UnKnowAccountException
        }
        //2.判断密码
        return new SimpleAuthenticationInfo(user, user.getPassWord(), "");
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        log.info(">>>>>>>>>>>>>>>开始授权>>>>>>>>>>>>...");
        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加资源的授权字符串
        //info.addStringPermission("user:add");
        //到数据库查询当前登录用户的授权字符串
        //获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        // 根据userId获取权限perms
        List<String> perms = userSerivce.findPermsById(user.getId());
        info.addStringPermission(perms.get(0));
        return info;
    }



}
