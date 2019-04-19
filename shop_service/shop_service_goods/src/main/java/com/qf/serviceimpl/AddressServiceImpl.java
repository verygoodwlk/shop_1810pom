package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> queryByUid(int uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        return  addressMapper.selectList(queryWrapper);
    }

    @Override
    public int insertAddress(Address address) {
        System.out.println("调用存储过程！！！！");
        return addressMapper.insertAddress(address);
    }

    @Override
    public Address queryById(int aid) {
        return addressMapper.selectById(aid);
    }
}
