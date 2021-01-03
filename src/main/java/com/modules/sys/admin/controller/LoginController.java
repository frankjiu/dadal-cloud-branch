package com.modules.sys.admin.controller;

import com.modules.sys.admin.model.dto.LoginDto;
import com.modules.sys.admin.model.entity.User;
import com.modules.sys.admin.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-02
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 访问项目根路径
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
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
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
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
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return "login";
        } else {
            model.addAttribute("user", user);
            return "index";
        }
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(@RequestBody LoginDto loginDto, HttpServletRequest request, Model model, HttpSession session) {
        if (loginDto.getRemeberMe() == null) {
            loginDto.setRemeberMe(false);
        }
        // 查询当前登录用户的盐
        String salt = userService.findByUserName(loginDto.getUserName()).getSalt();
        // 密码加密
        loginDto.setPassWord(new Sha256Hash(loginDto.getPassWord(), salt).toHex());
        // 创建token
        UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getUserName(), loginDto.getPassWord(), loginDto.getRemeberMe());
        Subject subject = SecurityUtils.getSubject();
        try {
            // 登录成功
            subject.login(token);
            User user = (User) subject.getPrincipal();
            session.setAttribute("user", user);
            model.addAttribute("user", user);
            return "index";
        } catch (Exception e) {
            model.addAttribute("msg", e.getMessage());
            return "login";
        }
    }

    /**
     * 登出(使用shiro默认的logout将会忽略此方法)
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session, Model model) {
        Subject subject = SecurityUtils.getSubject();
        session.invalidate();
        subject.logout();
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