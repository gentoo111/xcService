package com.xuecheng.manage_cms_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.framework","com.xuecheng.manage_cms_client"})
public class ManegeCmsClientApplication {

    @Value("${xuecheng.mq.queue}")
    private static String queue;
    public static void main(String[] args) {
        SpringApplication.run(ManegeCmsClientApplication.class, args);
        System.out.println(queue+"------------------------------------------");
    }
}