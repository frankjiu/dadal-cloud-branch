package com.modules.sys.permission;

/*import com.sc.starry_sky.entity.PageData;
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


@Component("MyShiroRealm")
public class MyShiroRealm extends AuthorizingRealm{

    //用于用户查询
    @Autowired
    private UserService userService;

    //添加角色权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户
        PageData user = (PageData) principalCollection.getPrimaryPrincipal();
        //获取权限列表
        ArrayList<PageData> roles = (ArrayList<PageData>) user.get("roles");
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        roles.forEach(item -> simpleAuthorizationInfo.addRole(item.getString("roleName")));
        return simpleAuthorizationInfo;
    }

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();

        PageData user = userService.findUserByUsername(username);
        if (user == null) {
            return null;
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getString("password"), getName());
            return simpleAuthenticationInfo;
        }
    }

}*/
