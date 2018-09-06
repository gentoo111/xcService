package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer01 {
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

            //创建消息消费者
            DefaultConsumer consumer=new DefaultConsumer(channel){
                /**
                 * 消费者接收到消息后悔调用此方法
                 * @param consumerTag 消费者标签,用来标识消费,如果不指定,默认一个名称
                 * @param envelope 消息内容包
                 * @param properties
                 * @param body 消息体
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String routingKey = envelope.getRoutingKey();
                    String message = new String(body, "utf-8");
                    System.out.println(message);

                }
            };

            /**
             * 监听队列,接收消息
             * String queue, boolean autoAck, Consumer callback
             * autoAck为自动回复,如果设置为false,需要程序员在代码中手动回复(channel.basicAck())
             */
            channel.basicConsume(QUEUE,true,consumer);



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
