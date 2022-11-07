package com.wxy.consumer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class MQConsumerConfigure {

    private String groupName;
    private String namesrvAddr;
    private String topics;
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "true")
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.subscribe("TestTopic", "TestTag||tagtwo");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                log.info("defaultMQPushConsumer消费消息=====>");
                try {
                    for(int i = 0;i < list.size();i++) {
                        MessageExt msg = list.get(i);
                        log.info(msg.getTopic() + " " + msg.getTags() + " " + new String(msg.getBody(), "utf-8"));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        log.info("======defaultMQPushConsumer启动===========");
        defaultMQPushConsumer.start();
        return defaultMQPushConsumer;
    }

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "true")
    public DefaultMQPushConsumer oneMQPushConsumer() throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("oneMQPushConsumer");
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.subscribe("TestTopic", "tagone");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                log.info("oneMQPushConsumer消费消息=====>");
                try {
                    for(int i = 0;i < list.size();i++) {
                        MessageExt msg = list.get(i);
                        log.info(msg.getTopic() + " " + msg.getTags() + " " + new String(msg.getBody(), "utf-8"));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        log.info("======oneMQPushConsumer启动===========");
        defaultMQPushConsumer.start();
        return defaultMQPushConsumer;
    }

}
