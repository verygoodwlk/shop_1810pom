package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {

    List<Goods> queryAll();

    int insert(Goods goods);

    Goods queryById(int gid);
}
