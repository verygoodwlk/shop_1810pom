package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Reference
    private IAddressService addressService;

    @RequestMapping("/insert")
    @ResponseBody
    @IsLogin(mustLogin = true)
    public int insertAddress(Address address, User user){
        address.setUid(user.getId());
        int result = addressService.insertAddress(address);
        return result;
    }
}
