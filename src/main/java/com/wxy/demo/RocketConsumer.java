package com.wxy.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "my-group", topic = "topic2", consumeMode = ConsumeMode.ORDERLY)
public class RocketConsumer implements RocketMQListener<Message> {
    @Override
    public void onMessage(Message o) {
        log.info("消费者1消费消息 = " + o);
    }

}
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "my-group2", topic = "wxy-topic")
class RocketConsumer2 implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("消费者2消费消息 = " + s);
    }
}
