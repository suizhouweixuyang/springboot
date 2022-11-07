package com.wxy.consumer;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Test3 implements Test {
    @Override
    public Integer getType() {
        return Type.TYPE3.getType();
    }

    @Override
    public String doSomeThing() {
        return this.getClass().getName();
    }

    public void set() {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set(new TestThreadLocal());
        Map map = new HashMap();
        map.put("","");

    }
    class TestThreadLocal {
        private int num;
        private String str;

    }
}
