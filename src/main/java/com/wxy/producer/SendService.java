package com.wxy.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/producer")
public class SendService {

    @Lazy
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @GetMapping("send")
    public String send(String msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message("TestTopic", "TestTag", msg.getBytes(StandardCharsets.UTF_8));
        SendResult send = defaultMQProducer.send(message);
        SendStatus sendStatus = send.getSendStatus();
        log.info("消息发送响应{}", send.toString());
        return sendStatus.toString();
    }

    @GetMapping("tagone")
    public String tagOne(String msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message("TestTopic", "tagone", msg.getBytes(StandardCharsets.UTF_8));
        SendResult send = defaultMQProducer.send(message);
        SendStatus sendStatus = send.getSendStatus();
        log.info("消息发送响应{}", send.toString());
        return sendStatus.toString();
    }

    @GetMapping("tagtwo")
    public String tagTwo(String msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message("TestTopic", "tagtwo", msg.getBytes(StandardCharsets.UTF_8));
        SendResult send = defaultMQProducer.send(message);
        SendStatus sendStatus = send.getSendStatus();
        log.info("消息发送响应{}", send.toString());
        log.info("测试");
        return sendStatus.toString();
    }

}
