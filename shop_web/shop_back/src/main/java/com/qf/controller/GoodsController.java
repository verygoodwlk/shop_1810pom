package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商品管理的controller
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @Value("${server.ip}")
    private String serverIp;

    /**
     * 查询商品列表
     *
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(Model model) {

        List<Goods> goods = goodsService.queryAll();
        model.addAttribute("goods", goods);
        model.addAttribute("serverip", serverIp);

        return "goodslist";
    }

    /**
     * 添加商品
     * <p>
     * 商品描述的处理 - 周末作业
     * 通常商品描述是一个富文本信息，页面上需要使用一些富文本的编辑器，这些编辑器都有现成的插件。
     * 数据库中只需要保存这些富文本的html值，显示的时候，直接在页面上输出这些html标签，浏览器就会自动解析这些富文本信息。
     * <p>
     * - 商品描述
     * - 发送邮件
     * <p>
     * 商品图片的上传
     * <p>
     * - 上传到哪里？
     * 单体架构直接上传到本地硬盘空间（D:/xxxx）
     * 分布式集群项目，通常会上传到一个共享的分布式文件系统
     *
     * @return
     */
    @RequestMapping("/insert")
    public String insert(Goods goods) {
        System.out.println("添加商品的信息：" + goods);
        goodsService.insert(goods);
        return "redirect:/goods/list";
    }
}
