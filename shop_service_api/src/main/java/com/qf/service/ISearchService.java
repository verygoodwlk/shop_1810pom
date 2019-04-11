package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {

    /**
     * 根据关键字，搜索商品列表
     * @param keyword
     * @return
     */
    List<Goods> searchGoods(String keyword);

    /**
     * 将商品信息同步到索引库中
     * @param goods
     * @return
     */
    int insertGoods(Goods goods);

}
