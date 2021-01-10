package com.modules.sys.admin.controller;

import com.core.constant.Constant;
import com.core.result.HttpResult;
import com.core.utils.TokenGenerator;
import com.modules.sys.admin.model.dto.LoginDto;
import com.modules.sys.admin.model.entity.RedisUser;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.MenuService;
import com.modules.sys.admin.service.UserRoleService;
import com.modules.sys.admin.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-02
 */
@Controller
public class LoginController extends AbstractController{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisTemplate<String, RedisUser> redisTemplate;

    /**
     * 访问项目根路径
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
        RedisUser redisUser = (RedisUser) SecurityUtils.getSubject().getPrincipal();
        if (redisUser == null) {
            return "redirect:/login";
        } else {
            return "redirect:/index";
        }
    }

    /**
     * 跳转login页
     */
    @GetMapping("/login")
    public String login(Model model) {
        RedisUser redisUser = (RedisUser) SecurityUtils.getSubject().getPrincipal();
        if (redisUser == null) {
            return "login";
        } else {
            return "redirect:index"; //重定向到index方法
        }
    }

    /**
     * 跳转index页
     */
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        RedisUser redisUser = (RedisUser) SecurityUtils.getSubject().getPrincipal();
        if (redisUser == null) {
            return "login";
        } else {
            model.addAttribute("user", redisUser.getUser());
            return "index";
        }
    }

    /**
     * 登录
     */
    // public String loginUser(HttpServletRequest request, Model model, HttpSession session) {
    @PostMapping("/login")
    public HttpResult loginUser(@RequestBody LoginDto dto) throws Exception {
        String sessionCode = (String) getSession().getAttribute(Constant.SESSION_RANDOM_CODE);
        /*if (dto.getCheckCode() == null || !dto.getCheckCode().equalsIgnoreCase(sessionCode)) {
            return HttpResult.fail("验证码错误!");
        }*/
        // 查询当前登录用户
        User loginUser = userService.findByUserName(dto.getUserName());
        if (loginUser == null) {
            throw new UnknownAccountException("账户不存在!");
        }
        if (0 == loginUser.getStatus()) {
            throw new LockedAccountException("账号已被禁用,请联系管理员!");
        }
        // 创建token
        UsernamePasswordToken newToken = new UsernamePasswordToken(dto.getUserName(),
                new Sha256Hash(dto.getPassWord(), loginUser.getSalt()).toHex(), dto.getRememberMe());
        // 执行登陆
        Subject subject = SecurityUtils.getSubject();
        subject.login(newToken);
        RedisUser user = (RedisUser) subject.getPrincipal();

        // 查询用户信息
        RedisUser redisUser = new RedisUser();
        String token = TokenGenerator.generateValue(loginUser.getUserName());
        redisUser.setToken(token);
        redisUser.setUser(loginUser);
        redisUser.setRoleId(userRoleService.findByUserId(loginUser.getId()).getRoleId());
        redisUser.setMenus(menuService.findMenuByRoleId(redisUser.getRoleId()));
        redisUser.setPermissions(menuService.findPermsByRoleId(redisUser.getRoleId()));
        // 将用户信息加入缓存
        redisTemplate.opsForValue().set(Constant.REDIS_USER_PREFIX + loginUser.getUserName(), redisUser, Constant.LOGIN_EXPIRE, TimeUnit.SECONDS);
        return HttpResult.success(token);
    }

    /**
     * 登出(使用shiro默认的logout将会忽略此方法)
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session, Model model) {
        // 删除缓存(这里放在一起全部删了; 可以只删除认证缓存,通过Spring-Cache存储权限缓存,因为授权缓存在用户再次登录后在有效期内仍可使用)
        RedisUser redisUser = redisTemplate.opsForValue().get(Constant.REDIS_USER_PREFIX + getUser().getUserName());
        if (redisUser != null) {
            redisTemplate.delete(Constant.REDIS_USER_PREFIX + getUser().getUserName());
        }

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        session.invalidate();
        model.addAttribute("msg", "安全退出!");
        return "login";
    }

    /**
     * 提示页
     */
    @RequestMapping("/noAuth")
    public String noAuth() {
        return "noAuth";
    }


}