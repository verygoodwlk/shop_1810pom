package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.Email;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @Reference
    private ICartService cartService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
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
    public String login(
            @CookieValue(name = "cart_token", required = false)
            String cartToken,
            String username,
            String password,
            Model model,
            HttpServletResponse response,
            String returnUrl){

        User user = userService.loginUser(username, password);
        if(user == null){
            model.addAttribute("error", "0");
            //登录失败
            return "login";
        } else if(user.getStatus() == 0){
            //未激活
            model.addAttribute("error", "1");

            String mail = user.getEmail();
            int index = mail.indexOf("@");
            String tomail = "http://mail." + mail.substring(index + 1);

            model.addAttribute("tomail", tomail);
            return "login";
        }

        //如果没有设置登录成功的url，则默认跳转回首页
        System.out.println(returnUrl);
        if(returnUrl == null || returnUrl.equals("")){
            returnUrl = "http://localhost:8081/";
        }

        //用户信息存放到redis中
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, user);
        redisTemplate.expire(token, 10, TimeUnit.DAYS);

        //将uuid回写到cookie中
        Cookie cookie = new Cookie("login_token", token);
        //设置cookie的过期时间
        cookie.setMaxAge(60 * 60 * 24 * 10);
//        //设置cookie的安全性,表示当前这个cookie不能通过前端脚本获取，只能通过http请求获取
//        cookie.setHttpOnly(true);
//        //设置cookie的所属域名
//        cookie.setDomain("a.jd.com");
//        //如果设置为true，表示当前的cookie只有在https协议时，才会传给服务器，如果是http协议不会传给服务器
//        cookie.setSecure(true);
        //设置cookie的有效路径
        cookie.setPath("/");

        //调用购物车合并的服务（临时购物车 - 永久购物车）
        cartService.mergeCarts(cartToken, user);


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

        //注册成功

        //发送激活邮件 怎么发送邮件？邮件内容是什么？

        Email email = new Email();
        email.setTo(user.getEmail());//设置发送给谁
        email.setSubject("暴雪官网激活邮件");

        String uuid = UUID.randomUUID().toString();

        //将uuid写入redis
        redisTemplate.opsForValue().set("email_token_" + user.getUsername(), uuid);
        redisTemplate.expire("email_token_" + user.getUsername(), 5, TimeUnit.MINUTES);

        String url = "http://localhost:8084/sso/jihuo?username=" + user.getUsername() + "&token=" + uuid;
        email.setContent("暴雪账号激活链接地址：<a href='" + url + "'>" + url +"</a>");//链接
        email.setCreatetime(new Date());

        rabbitTemplate.convertAndSend("email_queue", email);

        return "login";
    }

    /**
     * 激活请求
     * @return
     */
    @RequestMapping("/jihuo")
    public String jihuoUser(String username, String token){

        //验证token是否有效
        String redisToken = (String) redisTemplate.opsForValue().get("email_token_" + username);
        //验证token
        if(redisToken == null || !redisToken.equals(token)){
            //认证失败
            return "jihuoerror";
        }

        //认证成功，进行激活
        userService.jihuoUser(username);

        //激活完成跳转到登录页
        return "redirect:/sso/tologin";
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
