package com.wxy.consumer;

public enum Type {
    TYPE1(1), TYPE2(2), TYPE3(3);

    private Integer type;
    Type(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
