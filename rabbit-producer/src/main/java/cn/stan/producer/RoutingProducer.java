package cn.stan.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 定向路由模式 消息发送
 */
public class RoutingProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String routingExchange = "routing_exchange";
        channel.exchangeDeclare(routingExchange, BuiltinExchangeType.DIRECT, true, false, false, null);

        String routingQueueOrder = "routing_queue_order";
        String routingQueuePay = "routing_queue_pay";
        channel.queueDeclare(routingQueueOrder, true, false, false, null);
        channel.queueDeclare(routingQueuePay, true, false, false, null);

        String routing1 = "order_create";
        String routing2 = "order_update";
        String routing3 = "order_delete";
        String routing4 = "order_pay";
        channel.queueBind(routingQueueOrder, routingExchange, routing1);
        channel.queueBind(routingQueueOrder, routingExchange, routing2);
        channel.queueBind(routingQueueOrder, routingExchange, routing3);
        channel.queueBind(routingQueuePay, routingExchange, routing4);

        String msg1 = "订单创建A";
        channel.basicPublish(routingExchange, routing1, null, msg1.getBytes());
        String msg2 = "订单创建B";
        channel.basicPublish(routingExchange, routing1, null, msg2.getBytes());
        String msg3 = "订单更新C";
        channel.basicPublish(routingExchange, routing2, null, msg3.getBytes());
        String msg4 = "订单删除D";
        channel.basicPublish(routingExchange, routing3, null, msg4.getBytes());
        String msg5 = "订单支付E";
        channel.basicPublish(routingExchange, routing4, null, msg5.getBytes());
        String msg6 = "订单支付F";
        channel.basicPublish(routingExchange, routing4, null, msg6.getBytes());

        channel.close();
        connection.close();
    }

}
