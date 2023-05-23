package cn.stan.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列模式 消息发送
 */
public class WorkQueuesProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 1.创建连接工厂及相关配置
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("imooc");
        factory.setPassword("imooc");

        // 2.通过工厂创建连接
        Connection connection = factory.newConnection();

        // 3.创建管道
        Channel channel = connection.createChannel();

        // 4.创建队列（简单模式不需要交换机）
        /*
            queue – 队列的名称
            durable – 如果我们声明一个持久队列，则为true（该队列将在服务器重新启动后幸存下来）
            exclusive – 如果我们声明一个排他性队列（仅限于此连接），则为true，一般设置为false
            autoDelete – 如果我们声明一个自动删除队列，则为true（服务器将在不再使用时将其删除）
            arguments – 队列的其他属性（构造参数）
         */
        channel.queueDeclare("work_queue", true, false, false, null);

        // 5.向队列发送消息，若无交换机则路由键和队列保持一致即可
        for (int i = 0; i < 10; i++) {
            String msg = "开启任务[" + i + "]";
            channel.basicPublish("", "work_queue", null, msg.getBytes());
        }

        // 6.释放资源
        channel.close();
        connection.close();
    }

}
