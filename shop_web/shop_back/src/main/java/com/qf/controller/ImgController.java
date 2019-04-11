package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/imgs")
public class ImgController {


    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    private static final String UPLOADER_PATH = "C:\\worker\\imgs\\";

    /**
     * 图片上传
     * <p>
     * 后缀：就是告诉操作系统这是一个什么类型的文件？
     * <p>
     * 三国演义.mp4
     *
     * @return
     */
    @RequestMapping("/uploader")
    @ResponseBody
    public String uploaderImg(MultipartFile file) {

        //获得最有一个.的下标 xxxxxxx.jpg
        int index = file.getOriginalFilename().lastIndexOf(".");
        String houzui = file.getOriginalFilename().substring(index + 1);
        System.out.println("截取到的后缀：" + houzui);


        //上传到FastDFS中
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),
                    file.getSize(),
                    houzui,
                    null);

            //获取上传到FastDFS中的图片访问路径
            String storeUrl = storePath.getFullPath();

//            System.out.println("上传到FastDFS中的路径为：" + storeUrl);
            return "{\"uploadPath\":\"" + storeUrl + "\"}";

        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传到本地磁盘
//        try(
//                InputStream in = file.getInputStream();
//                OutputStream out = new FileOutputStream(UPLOADER_PATH + UUID.randomUUID().toString());
//        ){
//            IOUtils.copy(in, out);
//        } catch (IOException e){
//            e.printStackTrace();
//        }

        return null;
    }
}
