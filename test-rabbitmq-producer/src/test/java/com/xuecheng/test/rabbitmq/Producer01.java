package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.sql.Blob;
import java.util.concurrent.TimeoutException;

public class Producer01 {
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) throws Exception {
        Connection connection=null;
        Channel channel =null;

        try {
            ConnectionFactory connectionFactory=new ConnectionFactory();

            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");

            connectionFactory.setVirtualHost("/");
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            //声明一个队列
            /**
             * String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             *1.队列名称
             * 2.是否持久化
             * 3.是否独占此通道
             * 4.自动删除
             * 5.队列参数列表
             */
            channel.queueDeclare(QUEUE,true,false,false,null);

            //1.exchange 交换机名称,不指定交换机设置为空,rabbitmq会使用默认的交换机
            //2.routingKey 交换机跟陆路由key进行转发,因为未指定交换机,要设置为队列的名称
            //3.消息熟悉
            //4.消息内容
            String message="hello world"+System.currentTimeMillis();
            channel.basicPublish("",QUEUE,null,message.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channel.close();
            connection.close();
        }

    }
}
