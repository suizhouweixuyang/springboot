package com.wxy.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通道初始化器
 */

@Slf4j
@Component
public class WebSocketServerInitialize extends ChannelInitializer<SocketChannel> {

    @Value("${netty.ws.url}")
    private String url;
    @Resource
    private WebSocketServerHandler webSocketServerHandler;
    @Resource
    private HeartBeatHandler heartBeatHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //定义管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //http编解码处理器
        pipeline.addLast(new HttpServerCodec());
        //支持大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //使用聚合器、
        pipeline.addLast(new HttpObjectAggregator(65536));
        /**
         * 心跳支持：
         * 读空闲4秒
         * 写空闲8秒
         * 读写空闲10秒
         */
        pipeline.addLast(new IdleStateHandler(4, 8, 100));
        //websocket处理器
        pipeline.addLast(new WebSocketServerProtocolHandler(url));
        //自定义心跳处理器
        pipeline.addLast(heartBeatHandler);
        //自定义handler,处理业务逻辑
        pipeline.addLast(webSocketServerHandler);
    }
}
