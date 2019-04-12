package com.qf.shop_item;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopItemApplicationTests {


    @Autowired
    private Configuration configuration;

    @Test
    public void contextLoads() throws Exception {

        //准备一个静态页面输出的位置
        String outPath = "C:\\Users\\ken\\Desktop\\hello.html";
        Writer writer = new FileWriter(outPath);

        //通过配置对象读取模板
        Template template = configuration.getTemplate("hello.ftl");
        //准备数据部分
        List<Goods> goodsList = new ArrayList<>();
        Goods goods = new Goods(1, "电饭煲1", null, 100, "xxxx", "xxxx", 1, new Date(), 0);
        Goods goods2 = new Goods(2, "电饭煲2", null, 100, "xxxx", "xxxx", 1, new Date(), 0);
        Goods goods3 = new Goods(3, "电饭煲3", null, 100, "xxxx", "xxxx", 1, new Date(), 0);
        goodsList.add(goods);
        goodsList.add(goods2);
        goodsList.add(goods3);

        Map<String, Object> data = new HashMap<>();
        data.put("key", goods);
        data.put("age", 18);
        data.put("goodslist", goodsList);
        data.put("now", new Date());

        //将模板和数据进行静态化合并
        template.process(data, new FileWriter(outPath));

        //关闭流
        writer.close();
    }

}
