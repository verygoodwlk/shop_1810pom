package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

    private int id;
    private String person;
    private String address;
    private String phone;
    private String code;
    private int uid;
    private int isdefault;
}
