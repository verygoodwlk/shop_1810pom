package com.qf.shop_service_search;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
@DubboComponentScan("com.qf.serviceimpl")
public class ShopServiceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServiceSearchApplication.class, args);
    }

}
