package com.wxy.demo;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/rocket")
public class RocketTest {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    private RateLimiter rateLimiter = RateLimiter.create(1);


    @PostMapping("/send")
    @ApiOperation(value = "rocketsend")
    public Object send(String msg) {
        Message message = new Message();
        message.setBody("test".getBytes(StandardCharsets.UTF_8));
        rocketMQTemplate.asyncSend("topic2:tags2", message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功tags result = {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("发送tags异常", throwable);
            }
        });
        SendResult sendResult = rocketMQTemplate.syncSend("wxy-topic", msg);
        rocketMQTemplate.asyncSend("wxy-topic", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功 result = {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("发送异常", throwable);
            }
        });
        log.info(ObjectUtil.toString(sendResult));
        ApplicationContextUtil.getContext().publishEvent(new MessageEvent("发布事件"));
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setLocation(new ClassPathResource("test.lua"));
        defaultRedisScript.setResultType(Boolean.class);
        boolean test = (boolean) redisTemplate.execute(defaultRedisScript, Lists.newArrayList("test"), 3);
        if (!test) {
            log.info("====lua限流了");
        }
//        RateLimiter rateLimiter = RateLimiter.create(1);
        boolean b = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
        if(!b) {
            log.info("guava限流了");
        }
        BloomFilter<String> integerBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 100000l, 0.01);

        return sendResult;
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(5);
        for(int i = 0;i<20;i++) {
            new Thread(() -> {
//                rateLimiter.acquire(1);
                log.info(Thread.currentThread().getName()+"获取令牌开始时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                boolean b = rateLimiter.tryAcquire(3, TimeUnit.SECONDS);
                log.info(Thread.currentThread().getName()+"获取令牌时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+b);
//                System.out.println(Thread.currentThread().getName() + "获取令牌时间=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }).start();
        }
        BloomFilter<Integer> integerBloomFilter = BloomFilter.create(Funnels.integerFunnel(), 1000000l, 0.0000001);
        int count = 0;
        for(int i = 0;i < 1000000;i++) {
            integerBloomFilter.put(i);
        }
        for(int i = 2000000;i < 3000000;i++) {
            if(integerBloomFilter.mightContain(i)) {
                count += 1;
            }
        }
        BloomFilter<String> charSequenceBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 100l, 0.01);
        charSequenceBloomFilter.put("test");
        charSequenceBloomFilter.put("zhangsan");
        if(charSequenceBloomFilter.mightContain("test")) {
            System.out.println("存在test");
        }
        System.out.println("误判个数 ：" + count);
    }
}
