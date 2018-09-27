package com.xuecheng.base_id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@EnableDiscoveryClient//从注册中心查找服务客户端
@EnableFeignClients//服务调用客户端
@SpringBootApplication
public class IdGeneratorApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IdGeneratorApplication.class, args);
    }


}