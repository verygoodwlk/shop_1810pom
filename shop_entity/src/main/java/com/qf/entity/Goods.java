package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {

    private int id;
    private String gname;
    private BigDecimal gprice;
    private int gsave;
    private String ginfo;
    private String gimage;
    private int status;
    private Date createtime;
    private int tid;


//    public static void main(String[] args) {
//        //十进制没办法精准表示1/3
//        //二进制 - 1/10 - 二进制不能精准表示0.1
//        System.out.println(5.0 - 4.9);
//
//
//        BigDecimal a = BigDecimal.valueOf(5.0);
//        BigDecimal b = BigDecimal.valueOf(4.9);
//        BigDecimal divide = a.subtract(b);
//        System.out.println(divide.doubleValue());
//    }
}
