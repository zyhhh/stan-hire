package cn.stan.mq;

import cn.stan.api.mq.RabbitMQConfig;
import cn.stan.common.utils.GsonUtil;
import cn.stan.common.utils.SMSUtil;
import cn.stan.pojo.mq.SMSContentQO;
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

    @RabbitListener(queues = {RabbitMQConfig.SMS_QUEUE})
    public void listenSmsMsg(String payload, Message message) {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = {}, payload = {}", routingKey, payload);

        /*if (routingKey.equalsIgnoreCase(RabbitMQConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
            SMSContentQO smsContentQO = GsonUtil.stringToBean(payload, SMSContentQO.class);
            smsUtil.sendSMS(smsContentQO.getMobile(), smsContentQO.getContent(), smsContentQO.getExpireTime());
        }*/
    }
}
