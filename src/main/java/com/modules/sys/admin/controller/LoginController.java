package com.modules.sys.admin.controller;

import com.core.constant.Constant;
import com.core.exception.CommonException;
import com.core.result.HttpResult;
import com.modules.sys.admin.model.dto.LoginDto;
import com.modules.sys.admin.model.entity.RedisUser;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.MenuService;
import com.modules.sys.admin.service.UserRoleService;
import com.modules.sys.admin.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
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
    @PostMapping("/login")
    public HttpResult loginUser(@RequestBody LoginDto loginForm) throws Exception {
        /*// 从redis获取
        RedisUser redisUser = redisTemplate.opsForValue().get(Constant.REDIS_USER_PREFIX + loginForm.getUserName());
        if (redisUser != null) {
            // 更新缓存
            redisTemplate.opsForValue().set(Constant.REDIS_USER_PREFIX + loginForm.getUserName(), redisUser, Constant.LOGIN_EXPIRE, TimeUnit.SECONDS);
            return HttpResult.success(redisUser.getToken());
        }*/
        String sessionCode = (String) getSession().getAttribute(Constant.SESSION_RANDOM_CODE);
        if (loginForm.getCheckCode() == null || !loginForm.getCheckCode().equalsIgnoreCase(sessionCode)) {
            return HttpResult.fail("验证码错误!");
        }
        // 创建token, 加密后执行登陆验证
        User queryUser = this.userService.findByUserName(loginForm.getUserName());
        if (queryUser == null) {
            throw new CommonException("用户名不存在!");
        }
        String encodePassWord = new Sha256Hash(loginForm.getPassWord(), queryUser.getSalt()).toHex();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginForm.getUserName(), encodePassWord);
        Subject subject = SecurityUtils.getSubject();
        subject.login(usernamePasswordToken);
        if (!subject.isAuthenticated()) {
            throw new AuthenticationException("认证失败!");
        }
        // 认证成功, 将用户信息加入缓存
        RedisUser redisUser = getRedisUser();
        redisTemplate.opsForValue().set(Constant.REDIS_USER_PREFIX + queryUser.getUserName(), redisUser, Constant.LOGIN_EXPIRE, TimeUnit.SECONDS);
        return HttpResult.success(getToken());
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
        // session.invalidate();
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