package com.wxy.producer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
@ConfigurationProperties(prefix = "rocketmq.producer")
public class MQProducerConfigure {

    private String groupName;
    private String namesrvAddr;
    private Integer maxMessageSize;
    private Integer sendMsgTimeOut;
    private Integer retryTimesWhenSendFailed;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "isOnOff", havingValue = "true")
    public DefaultMQProducer defaultMQProducer () throws MQClientException {
        log.info("========= mqproducer 正在创建 ==========");
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setMaxMessageSize(maxMessageSize);
        defaultMQProducer.setSendMsgTimeout(sendMsgTimeOut);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.start();
        log.info("========= mqproducer 启动 ==========");
        return defaultMQProducer;
    }

}
