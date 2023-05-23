package cn.stan.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由通配符模式 消息发送
 */
public class TopicProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String topicExchange = "topic_exchange";
        channel.exchangeDeclare(topicExchange, BuiltinExchangeType.TOPIC, true, false, false, null);

        String topicQueueOrder = "topic_queue_order";
        String topicQueuePay = "topic_queue_pay";
        channel.queueDeclare(topicQueueOrder, true, false, false, null);
        channel.queueDeclare(topicQueuePay, true, false, false, null);

        // #代表0或多个，*代表有且仅有一个
        channel.queueBind(topicQueueOrder, topicExchange, "order.*");
        channel.queueBind(topicQueuePay, topicExchange, "*.pay.#");

        String msg1 = "订单创建A";
        channel.basicPublish(topicExchange, "order.create", null, msg1.getBytes());
        String msg2 = "订单创建B";
        channel.basicPublish(topicExchange, "order.create", null, msg2.getBytes());
        String msg3 = "订单更新C";
        channel.basicPublish(topicExchange, "order.update", null, msg3.getBytes());
        String msg4 = "订单删除D";
        channel.basicPublish(topicExchange, "order.delete", null, msg4.getBytes());
        String msg5 = "订单支付E";
        channel.basicPublish(topicExchange, "order.pay", null, msg5.getBytes());
        String msg6 = "do订单支付F";
        channel.basicPublish(topicExchange, "stan.pay.haha.dodo", null, msg6.getBytes());
        String msg7 = "ha订单支付G";
        channel.basicPublish(topicExchange, "stan.pay.haha", null, msg7.getBytes());

        channel.close();
        connection.close();
    }

}
