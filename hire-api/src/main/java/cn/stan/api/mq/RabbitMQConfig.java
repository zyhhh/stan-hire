package cn.stan.api.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 短信发送相关
    public static final String SMS_EXCHANGE = "sms_exchange";
    public static final String SMS_QUEUE = "sms_queue";
    public static final String SMS_ROUTING_KEY = "stan.sms.#";

    public static final String ROUTING_KEY_SMS_SEND_LOGIN = "stan.sms.send.login";


    @Bean(SMS_EXCHANGE)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(SMS_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean(SMS_QUEUE)
    public Queue queue() {
        return QueueBuilder
                .durable(SMS_QUEUE)
                .build();
    }

    @Bean
    public Binding smsBinding(@Qualifier(SMS_EXCHANGE) Exchange exchange,
                              @Qualifier(SMS_QUEUE) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(SMS_ROUTING_KEY)
                .noargs();
    }

}
