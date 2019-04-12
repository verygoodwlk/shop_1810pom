package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private Configuration configuration;

    /**
     * 生成静态页面
     * @return
     */
    @RequestMapping("/createHtml")
    public String createHtml(int gid, HttpServletRequest request){
        //通过商品id获得商品详细信息
        Goods goods = goodsService.queryById(gid);
        String gimage = goods.getGimage();
        String[] images = gimage.split("\\|");

        //通过模板生成Html页面
        try {
            //获得商品详情的模板对象
            Template template = configuration.getTemplate("goodsitem.ftl");

            //准备商品数据
            Map<String, Object> map = new HashMap<>();
            map.put("goods", goods);
            map.put("images", images);
            map.put("context", request.getContextPath());

            //生成静态页
            //获得classpath路径
            //静态页的名称必须和商品有所关联，最简单的做法就是用商品id作为页面的名字
            String path = this.getClass().getResource("/static/page/").getPath() + goods.getId() + ".html";
            template.process(map, new FileWriter(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
