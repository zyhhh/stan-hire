package cn.stan.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 定向路由模式 消息接收
 */
public class RoutingConsumerB {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String routingQueuePay = "routing_queue_pay";
        channel.queueDeclare(routingQueuePay, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                System.out.println("body = " + new String(body));
            }
        };

        channel.basicConsume(routingQueuePay, true, consumer);
    }

}
