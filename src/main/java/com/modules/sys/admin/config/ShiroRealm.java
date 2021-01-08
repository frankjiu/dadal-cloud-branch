package com.modules.sys.admin.config;

import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


/**
 * 自定义Realm: 获取用户,角色及权限信息
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userSerivce;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info(">>>>>>>>>>>>>>>开始认证>>>>>>>>>>>>..");
        // 获取用户名密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String passWord = new String(usernamePasswordToken.getPassword());

        // 查询数据库比对账号信息
        User user = this.userSerivce.findByUserName(userName);
        if (user == null) {
            throw new UnknownAccountException("账户不存在!");
        }
        if (!passWord.equals(user.getPassWord())) {
            throw new IncorrectCredentialsException("用户名或密码错误!");
        }
        if ("0".equals(user.getStatus().toString())) {
            throw new LockedAccountException("账号已被禁用,请联系管理员!");
        }
        return new SimpleAuthenticationInfo(user, user.getPassWord(), getName());
    }

    /**
     * 授权:
     * 检查用户拥有的perms是否包含标签 '<shiro:hasPermission name=''></shiro:hasPermission>' 的name值来进行显示与否.
     * add添加单个权限, set添加一组权限;
     * 在shiro配置中添加filterChainDefinitionMap.put("/add", "perms[user:add]"); 说明访问/add需要有"user:add"权限.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        log.info(">>>>>>>>>>>>>>>开始授权>>>>>>>>>>>>..");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登录用户
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        // 根据userId获取用户权限perms, 如"user:add","user:delete"等
        Set<String> menu_ids_perms = userSerivce.findPermsByUserId(user.getId());
        // 资源授权
        authorizationInfo.setStringPermissions(menu_ids_perms);
        return authorizationInfo;
    }

}
