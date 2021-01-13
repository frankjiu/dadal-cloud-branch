package com.modules.sys.admin.config;

import com.core.constant.Constant;
import com.core.utils.TokenGenerator;
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

import java.util.HashSet;


/**
 * 自定义Realm: 获取用户,角色及权限信息
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisTemplate<String, RedisUser> redisTemplate;

    /**
     * 认证
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken newToken) throws AuthenticationException {
        // 获取账号信息
        UsernamePasswordToken account = (UsernamePasswordToken) newToken;
        String formUserName = account.getUsername();
        String formPassWord = new String(account.getPassword());
        // 查询缓存
        RedisUser redisUser = redisTemplate.opsForValue().get(Constant.REDIS_USER_PREFIX + formUserName);
        if (redisUser == null) {
            // 查询数据库比对账号信息,密码校验交由Authentication认证
            User user = this.userService.findByUserName(formUserName);
            if (user == null) {
                throw new UnknownAccountException("账户不存在!");
            }
            if (0 == user.getStatus()) {
                throw new LockedAccountException("账号已被禁用,请联系管理员!");
            }
            // 查询用户相关信息
            redisUser = new RedisUser();
            redisUser.setToken(TokenGenerator.generateValue(user.getUserName()));
            redisUser.setUser(user);
            redisUser.setRoleId(userRoleService.findByUserId(user.getId()).getRoleId());
            redisUser.setMenus(menuService.findMenuByRoleId(redisUser.getRoleId()));
            redisUser.setPermissions(menuService.findPermsByRoleId(redisUser.getRoleId()));
        }
        return new SimpleAuthenticationInfo(redisUser, formPassWord, getName());
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
        RedisUser redisUser = (RedisUser) pc.getPrimaryPrincipal();
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        authInfo.addRole(redisUser.getRoleId().toString());
        authInfo.setStringPermissions(new HashSet<>(redisUser.getPermissions()));
        return authInfo;
    }

}
