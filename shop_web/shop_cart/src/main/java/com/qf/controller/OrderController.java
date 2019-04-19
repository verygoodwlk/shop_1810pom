package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Orders;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private ICartService cartService;

    @Reference
    private IAddressService addressService;

    @Reference
    private IOrderService orderService;
    
    
    /**
     * 强行登陆
     * 跳转到订单的编译页面 （默认下单整个购物车）
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/edit")
    public String orderEdit(User user, Model model){

        //当前用户的整个购物车信息
        List<ShopCart> shopCarts = cartService.queryCartsByUid(null, user);

        //当前用户关联的收货地址信息
        List<Address> addresses = addressService.queryByUid(user.getId());

        //计算总价
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : shopCarts) {
            priceall = priceall.add(shopCart.getAllprice());
        }

        model.addAttribute("carts", shopCarts);
        model.addAttribute("addresses", addresses);
        model.addAttribute("priceall", priceall.doubleValue());

        return "orderedit";
    }

    /**
     * 下单
     * @return
     */
    @IsLogin
    @RequestMapping("/insertOrders")
    @ResponseBody
    public String insertOrders(int aid, User user){
        String orderid = orderService.insertOrders(aid, user);
        return orderid;
    }

    /**
     * 查询订单列表
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/orderlist")
    public String orderList(User user, Model model){

        //查询当前用户的所有订单信息
        List<Orders> orders = orderService.queryOrdersByUid(user.getId());
        model.addAttribute("orders", orders);

        System.out.println("订单列表：" + orders);

        return "orderlist";
    }
}
