package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    /**
     * 自定义注解 + AOP
     *
     * 添加购物车
     * @param shopCart
     * @return
     */
    @RequestMapping("/add")
    @IsLogin(mustLogin = true)
    public String addCart(ShopCart shopCart, User user){

        System.out.println("添加购物车");

        return "addsucc";
    }

}
