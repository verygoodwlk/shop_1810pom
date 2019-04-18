package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCart implements Serializable {

    private int id;
    private int gid;
    private int uid;
    private int gnumber;//商品的数量
    private BigDecimal allprice;//这个商品的小计

}
