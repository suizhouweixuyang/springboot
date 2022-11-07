package com.wxy.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Resource
    private WebSocketServerHandler webSocketServerHandler;

    @GetMapping("")
    public String nettyTest(String msg) {
        log.info("controller >>>>>>>>>>> ");
        webSocketServerHandler.sendAll(msg);
        return "success";
    }
}
