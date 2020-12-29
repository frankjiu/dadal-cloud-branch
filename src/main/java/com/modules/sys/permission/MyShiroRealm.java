/*
package com.modules.sys.permission;

import com.core.result.PageBean;
import com.core.result.PageModel;
import com.modules.sys.admin.service.UserService;
import com.sc.starry_sky.entity.PageData;
import com.sc.starry_sky.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;


@Component("MyShiroRealm")
public class MyShiroRealm extends AuthorizingRealm{

    //用于用户查询
    @Autowired
    private UserService userService;

    //添加角色权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户
        Map user = (Map) principalCollection.getPrimaryPrincipal();
        //获取权限列表
        ArrayList<Map> roles = (ArrayList<Map>) user.get("roles");
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        roles.forEach(item -> simpleAuthorizationInfo.addRole(item.get("roleName")));
        return simpleAuthorizationInfo;
    }

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();

        Map user = userService.findUserByUsername(username);
        if (user == null) {
            return null;
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getString("password"), getName());
            return simpleAuthenticationInfo;
        }
    }

}
*/
