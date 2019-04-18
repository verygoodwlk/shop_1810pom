package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private ICartService cartService;

    /**
     * 自定义注解 + AOP
     *
     * 添加购物车
     * @param shopCart
     * @return
     */
    @RequestMapping("/add")
    @IsLogin
    public String addCart(
            @CookieValue(name = "cart_token", required = false) String cartToken,
            ShopCart shopCart,
            User user,
            HttpServletResponse response){

        if(cartToken == null){
            cartToken = UUID.randomUUID().toString();

            //回写cookie
            Cookie cookie = new Cookie("cart_token", cartToken);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        cartService.addCart(cartToken, shopCart, user);
        return "addsucc";
    }

    /**
     * 获取购物车信息
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    @IsLogin
    public String cartList(
            @CookieValue(name = "cart_token", required = false) String cartToken,
            User user){

        List<ShopCart> shopCarts = cartService.queryCartsByUid(cartToken, user);

        return "showcarts(" + JSON.toJSONString(shopCarts) + ")";
    }

    /**
     * 跳转到购物车列表
     * @return
     */
    @RequestMapping("/cartlist")
    @IsLogin
    public String toCartList(
            @CookieValue(name = "cart_token", required = false) String cartToken,
            User user,
            Model model){

        List<ShopCart> shopCarts = cartService.queryCartsByUid(cartToken, user);
        model.addAttribute("carts", shopCarts);

        //计算总价
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : shopCarts) {
            priceall = priceall.add(shopCart.getAllprice());
        }
        model.addAttribute("priceall", priceall.doubleValue());

        return "cartlist";
    }
}
