package com.wxy.demo;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    private String content;
    public MessageEvent(String content) {
        super(content);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
