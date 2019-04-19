package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderDetilsMapper;
import com.qf.dao.OrdersMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersServiceImpl implements IOrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetilsMapper orderDetilsMapper;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private ICartService cartService;

    @Override
    @Transactional
    public String insertOrders(int aid, User user) {

        //根据收货地址id查询收货地址的详细信息
        Address address = addressService.queryById(aid);

        //根据用户信息获得当前用户的所有购物车数据
        List<ShopCart> shopCarts =
                cartService.queryCartsByUid(null, user);

        //计算总价
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCart shopCart : shopCarts) {
            priceall = priceall.add(shopCart.getAllprice());
        }

        //通过购物车信息封装订单和订单详情对象
        Orders orders = new Orders(
                0,
                UUID.randomUUID().toString(),
                address.getPerson(),
                address.getAddress(),
                address.getPhone(),
                address.getCode(),
                priceall,
                new Date(),
                0,
                user.getId(), null
        );

        //订单入库
        ordersMapper.insert(orders);

        //添加订单详情
        for (ShopCart shopCart : shopCarts) {
            OrderDetils orderDetils = new OrderDetils(
                    0,
                    shopCart.getGid(),
                    shopCart.getGoods().getGimage(),
                    shopCart.getGoods().getGname(),
                    shopCart.getGoods().getGprice(),
                    shopCart.getGnumber(),
                    orders.getId()
            );

            //订单详情入库
            orderDetilsMapper.insert(orderDetils);
        }

        //清空购物车信息
        cartService.deleteCartsByUid(user.getId());

        return orders.getOrderid();
    }

    @Override
    public Orders queryById(int oid) {
        Orders orders = ordersMapper.selectById(oid);
        return orders;
    }

    @Override
    public Orders queryByOrderId(String orderid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid", orderid);
        return ordersMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Orders> queryOrdersByUid(int uid) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("uid", uid);
//        List<Orders> ordersList = ordersMapper.selectList(queryWrapper);
        return ordersMapper.queryOrdersByUid(uid);
    }

    @Override
    public int updateOrders(Orders order) {
        return ordersMapper.updateById(order);
    }
}
