package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetils implements Serializable {

    private int id;
    private int gid;
    private String gimage;
    private String gname;
    private BigDecimal gprice;
    private int gnumber;
    private int oid;
 }
