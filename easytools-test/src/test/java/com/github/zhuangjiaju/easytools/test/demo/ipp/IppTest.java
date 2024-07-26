package com.github.zhuangjiaju.easytools.test.demo.ipp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * i++ 线程安全
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class IppTest extends SimpleBaseTest {

    private static int i = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 用户 100个虚拟线程
     * 给 i 做一万次 i++
     */
    @Test
    public void demo() throws Exception {
        i = 0;
        // 用来等待所有线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int j = 0; j < 100; j++) {
            // 启动虚拟线程 性能比线程好
            Thread.startVirtualThread(() -> {
                for (int k = 0; k < 10000; k++) {
                    i++;
                }
                countDownLatch.countDown();
            });
        }
        // 等待所有线程执行完毕
        countDownLatch.await();
        // 如果线程安全 则会输出 1000000，实际上会小于 1000000
        log.info("结果是：{}", i);
        Assertions.assertTrue(i < 1000000);
    }

    /**
     * 用户 100个虚拟线程
     * 给 i 做一万次 i++
     * 加上synchronized 锁
     */
    @Test
    public void threadSafeSynchronized() throws Exception {
        i = 0;
        // 用来等待所有线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int j = 0; j < 100; j++) {
            // 启动虚拟线程 性能比线程好
            Thread.startVirtualThread(() -> {
                for (int k = 0; k < 10000; k++) {
                    synchronized (IppTest.class) {
                        i++;
                    }
                }
                countDownLatch.countDown();
            });
        }
        // 等待所有线程执行完毕
        countDownLatch.await();
        // 加了锁以后可以正常的输出 1000000
        log.info("结果是：{}", i);
        Assertions.assertEquals(1000000, i);
    }

    /**
     * 用户 100个虚拟线程
     * 给 atomicInteger 做一万次 累加
     */
    @Test
    public void threadSafeAtomicInteger() throws Exception {
        atomicInteger = new AtomicInteger(0);
        // 用来等待所有线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int j = 0; j < 100; j++) {
            // 启动虚拟线程 性能比线程好
            Thread.startVirtualThread(() -> {
                for (int k = 0; k < 10000; k++) {
                    atomicInteger.incrementAndGet();
                }
                countDownLatch.countDown();
            });
        }
        // 等待所有线程执行完毕
        countDownLatch.await();
        // 加了锁以后可以正常的输出 1000000
        log.info("结果是：{}", atomicInteger.get());
        Assertions.assertEquals(1000000, atomicInteger.get());
    }

}
