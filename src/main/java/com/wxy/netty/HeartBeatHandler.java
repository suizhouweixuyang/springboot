package com.wxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * 心跳处理器，如果服务器一段时间没收到客户端心跳，主动断开连接，避免浪费资源
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private WebSocketServerHandler webSocketServerHandler;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断是否是IdleStateEvent
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            if(state == IdleState.READER_IDLE) {
                log.info("进入读空闲 >>>>>>>>>>>>> ");
            } else if(state == IdleState.WRITER_IDLE) {
                log.info("进入写空闲 >>>>>>>>>>>>>");
            } else if(state == IdleState.ALL_IDLE) {
                log.info("关闭无用的Channel，以防资源浪费。Channel Id：{}", ctx.channel().id());
                Channel channel = ctx.channel();
                channel.close();
                ChannelGroup clients = webSocketServerHandler.getClients();
                clients.remove(channel);
                log.info("Channel关闭后，client的数量为:{}", clients.size());
            }
        }
    }
}
