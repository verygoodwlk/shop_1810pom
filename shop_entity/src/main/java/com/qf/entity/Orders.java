package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;
    private String orderid;
    private String person;
    private String address;
    private String phone;
    private String code;
    private BigDecimal allprice;
    private Date createtime;
    private int status;
    private int uid;

    @TableField(exist = false)
    private List<OrderDetils> orderDetils;
}
