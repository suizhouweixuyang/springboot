package com.wxy.consumer;

import org.springframework.stereotype.Component;

@Component
public class Test1 implements Test {
    @Override
    public Integer getType() {
        return Type.TYPE1.getType();
    }

    @Override
    public String doSomeThing() {
        return this.getClass().getName();
    }
}
