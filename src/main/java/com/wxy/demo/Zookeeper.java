package com.wxy.demo;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.admin.ZooKeeperAdmin;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Zookeeper {

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("10.46.3.223:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, "/zk-lock");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AtomicInteger j = new AtomicInteger(0);
        Runablet r = new Runablet();
//        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
//        for (int i = 0;i < 100;i++) {
//            executorService2.submit(() -> {
////                j.incrementAndGet();
//                r.add();
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService2.shutdown();
//        while (true) {
//            if (executorService2.awaitTermination(10, TimeUnit.SECONDS)) {
//                log.info("=========== " + r.i);
//                break;
//            }
//        }
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<?> submit = listeningExecutorService.submit(() -> {
            log.info(Thread.currentThread().getName());
            return true;
        });
        ListenableFuture<?> submit1 = listeningExecutorService.submit(() -> {
            log.info(Thread.currentThread().getName());
            return 2;
        });
        ListenableFuture<?> submit2 = listeningExecutorService.submit(() -> {
            log.info(Thread.currentThread().getName());
            return false;
        });
        ListenableFuture<? extends List<?>> listListenableFuture = Futures.allAsList(submit, submit1, submit2);
        try {
            List<?> list = listListenableFuture.get();
            list.forEach(System.out::println);
        } catch (Exception e) {

        }
        DelayQueue delayQueue = new DelayQueue();
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(1, 2, 3, TimeUnit.SECONDS, delayQueue);
        for(int i = 0;i < 100;i++) {
            threadPoolExecutor.submit(() -> {
                j.incrementAndGet();
                System.out.println("r = " + j.getAndIncrement());
            });
        }
        threadPoolExecutor.shutdown();
        Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });
        new Thread(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("callable-future");
                return "123";
            }
        })).start();
        threadPoolExecutor.submit(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        }));
        for (int i = 0;i < 10;i++) {
            executorService.submit(() -> {
                boolean acquire = false;
                try {
                    while (true) {
                        acquire = interProcessMutex.acquire(5, TimeUnit.SECONDS);
                        if (acquire) {
                            log.info("{}获取锁成功", Thread.currentThread().getName());
//                            j.incrementAndGet();
                            r.add();
                            break;
//                        Thread.sleep(3000);
                        } else {
                            log.info("{}获取锁失败", Thread.currentThread().getName());
                        }
                    }

                } catch (Exception e) {
                    log.error("加锁失败", e);
                } finally {
                    if (acquire) {
                        try {
                            interProcessMutex.release();
                        } catch (Exception e) {
                            log.error("释放锁失败", e);
                        }
                    }
                }

            });
        }
        executorService.shutdown();
        while (true) {
            if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                log.info("=========== " + r.i);
                break;
            }
        }
    }
    static class Runablet {

        public int i = 0;

        public void add() {
            i++;
        }
    }
}
