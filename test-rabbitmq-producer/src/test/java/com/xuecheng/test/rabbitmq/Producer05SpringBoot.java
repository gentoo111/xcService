package com.xuecheng.test.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xuecheng.test.rabbitmq.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布订阅模式
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05SpringBoot {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendEmail(){
        String message="test send email by spring boot...";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
        System.out.println(message);
    }

    //发送站点页面
    @Test
    public void sendPostPage(){
        Map map=new HashMap();
        map.put("pageId","5b8027c76d9b3206907fd104");
        String message = JSON.toJSONString(map);
        System.out.println(message);
        //站点id就是routingkey
        String routingkey = "5a751fab6abb5044e0d19ea1";
        //交换机名称
        rabbitTemplate.convertAndSend("ex_cms_postpage",routingkey,message);
    }
}
