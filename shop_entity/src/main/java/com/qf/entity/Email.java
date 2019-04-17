package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    //标题
    private String subject;
    //发送方
    private String from;
    //目标方
    private String to;
    //内容
    private String content;
    //创建时间
    private Date createtime;
    //附件
    private File file;
}
