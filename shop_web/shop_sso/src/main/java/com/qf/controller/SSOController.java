package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;
    
    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/tologin")
    public String toLogin(String returnUrl, Model model){
        model.addAttribute("returnUrl", returnUrl);
        return "login";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/toregister")
    public String toRegister(){
        return "register";
    }


    /**
     * 登录用户
     * @return
     */
    @RequestMapping("/login")
    public String login(String username, String password, Model model, HttpServletResponse response, String returnUrl){

        User user = userService.loginUser(username, password);
        if(user == null){
            model.addAttribute("error", "0");
            //登录失败
            return "login";
        }

        //如果没有设置登录成功的url，则默认跳转回首页
        if(returnUrl == null){
            returnUrl = "http://localhost:8081/";
        }

        //用户信息存放到redis中
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, user);
        redisTemplate.expire(token, 10, TimeUnit.DAYS);

        //将uuid回写到cookie中
        Cookie cookie = new Cookie("login_token", token);
        cookie.setMaxAge(60 * 60 * 24 * 10);
        response.addCookie(cookie);

        //登录成功跳转到首页
        return "redirect:" + returnUrl;
    }

    /**
     * 注册用户
     * @return
     */
    @RequestMapping("/register")
    public String register(User user, Model model){

        //调用注册服务
        int result = userService.insertUser(user);
        if(result <= 0){
            //注册失败
            model.addAttribute("error", "0");
            return "register";
        }

        return "login";
    }

    /**
     * 判断当前的浏览器是否登录
     * @return
     */
    @RequestMapping("/islogin")
    @ResponseBody
    public String isLogin(@CookieValue(name = "login_token", required = false) String loginToken){

        //获取浏览器cookie中的login_token
        System.out.println("获得浏览器发送过来的请求：" + loginToken);

        //通过token去redis中验证是否登录
        User user = null;
        if(loginToken != null){
            user = (User) redisTemplate.opsForValue().get(loginToken);
        }

        return user == null ? "islogin(null)" : "islogin('" + JSON.toJSONString(user) + "')";
    }

    /**
     * 注销
     * @return
     */
    @RequestMapping("/logout")
    public String logout(@CookieValue(name = "login_token", required = false) String loginToken, HttpServletResponse response){

        //清空redis
        redisTemplate.delete(loginToken);

        //请求cookie
        Cookie cookie = new Cookie("login_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "login";
    }
}
