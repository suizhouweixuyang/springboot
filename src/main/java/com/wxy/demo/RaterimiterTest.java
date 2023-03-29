package com.wxy.demo;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Aspect
public class RaterimiterTest {

    private RateLimiter rateLimiter;
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();
    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(rateLimit)")
    public void raterLimitAspect(RateLimit rateLimit) {}

    public Object deBefore(ProceedingJoinPoint proceedingJoinPoint, RateLimit rateLimit) throws Throwable {
//        boolean iplimit = iplimit();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        return proceedingJoinPoint.proceed();
    }
}
