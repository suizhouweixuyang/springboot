package com.wxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 客户端发送服务器消息
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //接受客户端消息
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        log.info("收到客户端消息：" + textWebSocketFrame.text());
        Channel channel = channelHandlerContext.channel();
        channel.writeAndFlush(new TextWebSocketFrame("服务器推送消息 >>> " + sdf.format(System.currentTimeMillis())));
    }
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.add(channel);
        log.info("客户端加入链接：" + ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.remove(channel);
        log.info("客户端断开连接：" + ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive >>>>>>>>>> ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive >>>>>>>>> ");
    }



    public void sendAll(String msg) {
        clients.writeAndFlush(new TextWebSocketFrame("服务器群发消息，msg = " + msg));
    }
    public ChannelGroup getClients() {
        return clients;
    }
}
