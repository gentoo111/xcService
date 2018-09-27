package com.xuecheng.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 11:53
 **/

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.xuecheng.search"})//扫描本项目下的所有类
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }
}
