package cn.stan.mq;

import cn.stan.api.mq.RabbitMQConfig;
import cn.stan.common.utils.GsonUtil;
import cn.stan.common.utils.SMSUtil;
import cn.stan.pojo.mq.SMSContentQO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsConsumer {
    @Autowired
    private SMSUtil smsUtil;

    /*@RabbitListener(queues = {RabbitMQConfig.SMS_QUEUE})
    public void listenSmsMsg(String payload, Message message) {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = {}, payload = {}", routingKey, payload);

        if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
            SMSContentQO smsContentQO = GsonUtil.stringToBean(payload, SMSContentQO.class);
            smsUtil.sendSMS(smsContentQO.getMobile(), smsContentQO.getContent(), smsContentQO.getExpireTime());
        }
    }*/

    /**
     * 消息手动ack确认
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitMQConfig.SMS_QUEUE})
    public void listenSmsMsg(Message message, Channel channel) throws Exception {

        // 消息
        String payload = new String(message.getBody());
        // 路由键
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        // 消息投递标签
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        log.info("routingKey: {}, payload: {}, deliveryTag: {}", routingKey, payload, deliveryTag);

        try {
            /*if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
                SMSContentQO smsContentQO = GsonUtil.stringToBean(payload, SMSContentQO.class);
                smsUtil.sendSMS(smsContentQO.getMobile(), smsContentQO.getContent(), smsContentQO.getExpireTime());
            }*/
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error(e.getMessage());
            channel.basicNack(deliveryTag, true, false);
        }

    }
}
