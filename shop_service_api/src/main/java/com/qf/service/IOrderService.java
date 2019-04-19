package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {

    String insertOrders(int aid, User user);

    Orders queryById(int oid);

    Orders queryByOrderId(String orderid);

    List<Orders> queryOrdersByUid(int uid);

    int updateOrders(Orders order);

}
