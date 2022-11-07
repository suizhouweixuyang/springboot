package com.wxy.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String test(Integer type) {
        return TestFactory.map.get(type).doSomeThing();
    }
}
