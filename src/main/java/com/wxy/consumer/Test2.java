package com.wxy.consumer;

import org.springframework.stereotype.Component;

@Component
public class Test2 implements Test {
    @Override
    public Integer getType() {
        return Type.TYPE2.getType();
    }

    @Override
    public String doSomeThing() {
        return this.getClass().getName();
    }
}
