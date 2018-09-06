package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String EX_CMS_POSTPAGE = "ex_cms_postpage";

    //从配置文件注入队列名称
    @Value("${xuecheng.mq.queue}")
    private String queue_cms_postpage;

    //得到routingkey
    @Value("${xuecheng.mq.routingKey}")
    private String routingkey;

    //声明交换机
    @Bean(EX_CMS_POSTPAGE)
    public Exchange EX_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_CMS_POSTPAGE).durable(true).build();
    }

    //声明queue
    @Bean("${xuecheng.mq.queue}")
    public Queue QUEUE_CMS_POSTPAGE(){
        return new Queue(queue_cms_postpage);
    }

    //绑定交换机
    @Bean
    public Binding binding_queue_cms_postpage(@Qualifier("${xuecheng.mq.queue}")Queue queue,
                                              @Qualifier(EX_CMS_POSTPAGE)Exchange exchange){
        //路由key就是站点id，从配置文件中取
        return BindingBuilder.bind(queue).to(exchange).with(routingkey).noargs();
    }

}
