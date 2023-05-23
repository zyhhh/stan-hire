package cn.stan.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由通配符模式 消息接收
 */
public class TopicConsumerB {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String topicQueuePay = "topic_queue_pay";
        channel.queueDeclare(topicQueuePay, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                System.out.println("body = " + new String(body));
            }
        };

        channel.basicConsume(topicQueuePay, true, consumer);
    }

}
