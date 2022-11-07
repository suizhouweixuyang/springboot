package com.wxy.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class NettyNIOServer {

    @Value("${netty.ws.port}")
    private int port;
    @Resource
    private WebSocketServerInitialize webSocketServerInitialize;

    @PostConstruct
    public void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            log.info("nettynioserver初始化开始 >>>>>>>>>>>>> ");
            //主线程组，主要负责接收客户端连接，接受请求
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            //工作线程组，主要负责网络io，处理请求
            EventLoopGroup workGroup = new NioEventLoopGroup();
            //启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            try {
                serverBootstrap.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.DEBUG))
                        //自定义通道初始化器，用于入站操作
                        .childHandler(webSocketServerInitialize);
                ChannelFuture channel = serverBootstrap.bind(port).sync();
                channel.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("netty启动异常", e);
            } catch (Exception e) {
                log.error("netty初始化异常", e);
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });
    }
}
