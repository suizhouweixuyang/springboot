package com.wxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelGroup {

    private static volatile ChannelGroup channelGroup = null;
    private static volatile ConcurrentHashMap<String, Channel> map = null;

    private static final Object channelLock = new Object();
    private static final Object mapLock = new Object();

    public static ChannelGroup getChannelGroup() {
        if (null == channelGroup) {
            synchronized (channelLock) {
                if (null == channelGroup) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
            }
        }
        return channelGroup;
    }

    public static ConcurrentHashMap getConcurrentHashMap() {
        if (null == map) {
            synchronized (mapLock) {
                if (null == map) {
                    map = new ConcurrentHashMap<String, Channel>();
                }
            }
        }
        return map;
    }

    public static Channel getChannel(String token) {
        if(null == map) {
            return (Channel) getConcurrentHashMap().get(token);
        }
        return map.get(token);
    }


}
