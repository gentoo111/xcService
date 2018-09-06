package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发布订阅模式
 */
public class Producer04Topic {
    private static final String QUEUE_INFORM_EAMIL = "queue_inform_eamil";
    private static final String ROUTINGKEY_EAMIL = "inform.#.email.#";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

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

            /**
             *声明交换机
             *
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM,BuiltinExchangeType.TOPIC);
            //声明队列
            channel.queueDeclare(QUEUE_INFORM_EAMIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            //将队列绑定到交换机中
            channel.queueBind(QUEUE_INFORM_EAMIL, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_EAMIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_SMS);

            //1.exchange 交换机名称,不指定交换机设置为空,rabbitmq会使用默认的交换机
            //2.routingKey routing路由模式需要指定
            //3.消息属性
            //4.消息内容
            //发送email
//            for (int i = 0; i < 2; i++) {
//                String message="send email:"+System.currentTimeMillis();
//                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.email",null,message.getBytes());
//                System.out.println("路由模式消息发送成功...");
//            }
            //发送sms
//            for (int i = 0; i < 3; i++) {
//                String message="send sms:"+System.currentTimeMillis();
//                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms",null,message.getBytes());
//                System.out.println("路由模式消息发送成功...");
//            }
            //发送email和sms
            for (int i = 0; i < 3; i++) {
                String message="send email and sms:"+System.currentTimeMillis();
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms.email",null,message.getBytes());
                System.out.println("路由模式消息发送成功...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channel.close();
            connection.close();
        }

    }
}
