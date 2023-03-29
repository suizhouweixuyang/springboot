package com.wxy.demo;

public interface Subject {

    void registerObserver(Observer observer);
    void sendMsg(String msg);
}

interface Observer {
    void recvMessage(String msg);
}
