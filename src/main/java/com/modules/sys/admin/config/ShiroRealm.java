package com.modules.sys.admin.config;

import com.core.constant.Constant;
import com.modules.sys.admin.model.entity.RedisUser;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.MenuService;
import com.modules.sys.admin.service.UserRoleService;
import com.modules.sys.admin.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;


/**
 * 自定义Realm: 获取用户,角色及权限信息
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private RedisTemplate<String, RedisUser> redisTemplate;

    @Autowired
    private UserService userSerivce;

    @Autowired
    private MenuService menuSerivce;

    @Autowired
    private UserRoleService userRoleSerivce;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken newToken) throws AuthenticationException {
        UsernamePasswordToken account = (UsernamePasswordToken) newToken;
        String userName = account.getUsername();

        RedisUser redisUser = redisTemplate.opsForValue().get(Constant.REDIS_USER_PREFIX + userName);
        if (redisUser == null) {
            throw new IncorrectCredentialsException("token已过期, 请重新登陆!");
        }

        String passWord = new String(account.getPassword());
        return new SimpleAuthenticationInfo(redisUser, passWord, getName());
    }

    /**
     * 授权:
     * 检查用户拥有的perms是否包含标签 '<shiro:hasPermission name=''></shiro:hasPermission>' 的name值来进行显示与否.
     * add添加单个权限, set添加一组权限;
     * 在shiro配置中添加filterChainDefinitionMap.put("/add", "perms[user:add]"); 说明访问/add需要有"user:add"权限.
     */
    @SneakyThrows
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        /*SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登录用户
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        // 获取登录用户的角色
        UserRole userRole = userRoleSerivce.findByUserId(user.getId());
        // 根据userId获取用户权限perms, 如"user:add","user:delete"等; 如果用户为超管角色, 则查询所有菜单.
        List<Menu> menus = menuSerivce.findMenuByRoleId(userRole.getRoleId());
        List<String> permList = menus.stream().map(e -> e.getPerm()).collect(Collectors.toList());
        Set<String> perms = CollectionUtils.isEmpty(permList) ? new HashSet<>() : new HashSet<>(permList);
        // 资源授权
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;*/

        RedisUser redisUser = (RedisUser) pc.getPrimaryPrincipal();
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        authInfo.setStringPermissions(new HashSet<>(redisUser.getPermissions()));
        return authInfo;
    }

}
