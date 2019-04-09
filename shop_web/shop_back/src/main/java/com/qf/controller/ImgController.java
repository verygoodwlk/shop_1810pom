package com.qf.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Controller
@RequestMapping("/imgs")
public class ImgController {


    private static final String UPLOADER_PATH = "C:\\worker\\imgs\\";

    /**
     * 图片上传
     *
     * 后缀：就是告诉操作系统这是一个什么类型的文件？
     *
     * 三国演义.mp4
     *
     *
     * @return
     */
    @RequestMapping("/uploader")
    @ResponseBody
    public String uploaderImg(MultipartFile file)  {

        //上传到FastDFS中
        try(
                InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(UPLOADER_PATH + UUID.randomUUID().toString());
        ){
            IOUtils.copy(in, out);
        } catch (IOException e){
            e.printStackTrace();
        }

        return "succ";
    }
}
