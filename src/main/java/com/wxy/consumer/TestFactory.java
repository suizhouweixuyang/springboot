package com.wxy.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TestFactory implements ApplicationContextAware {
    public static Map<Integer, Test> map = new ConcurrentHashMap<Integer, Test>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Test> bean = applicationContext.getBeansOfType(Test.class);
        bean.forEach((s, test) -> {
            map.put(test.getType(), test);
        });
        log.info("bean = {}", JSONObject.toJSONString(map));
    }
}
