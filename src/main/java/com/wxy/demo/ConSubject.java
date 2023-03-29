package com.wxy.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

public class ConSubject implements Subject {
    private List<Observer> observerList = new ArrayList<Observer>();
    @Override
    public void registerObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void sendMsg(String msg) {
        observerList.forEach(observer -> {
            observer.recvMessage(msg);
        });
    }
}

@Slf4j
class AObserver implements Observer {

    AObserver(Subject subject) {
        subject.registerObserver(this);
    }

    @Override
    public void recvMessage(String msg) {
        log.info("观察者A收到消息：" + msg);
    }
}
