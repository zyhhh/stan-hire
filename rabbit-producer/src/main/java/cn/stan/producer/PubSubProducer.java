package cn.stan.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式 消息发送
 */
public class PubSubProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /*
        exchange – 交换机名称
        type – 交换机类型
        durable – 如果我们声明持久化交换机，则为 true（交换将在服务器重新启动后幸存下来）
        autoDelete – 如果服务器在不再使用时应自动删除交换机，则为 true
        internal – 如果交换机是内部的，即不能由客户直接发布，则为 true。
        arguments – 交换机的其他属性（构造参数）
         */
        String fanoutExchange = "fanout_exchange";
        channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT, true, false, false, null);

        // 定义2个队列来展示广播效果
        String fanoutQueueA = "fanout_queue_a";
        String fanoutQueueB = "fanout_queue_b";
        channel.queueDeclare(fanoutQueueA, true, false, false, null);
        channel.queueDeclare(fanoutQueueB, true, false, false, null);

        // 绑定关系
        channel.queueBind(fanoutQueueA, fanoutExchange, "");
        channel.queueBind(fanoutQueueB, fanoutExchange, "");

        for (int i = 0; i < 10; i++) {
            String msg = "开启任务[" + i + "]";
            channel.basicPublish(fanoutExchange, "", null, msg.getBytes());
        }

        channel.close();
        connection.close();
    }

}
