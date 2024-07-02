package com.github.zhuangjiaju.easytools.test.temp.exception;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 异常测试
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class ExceptionTest extends SimpleBaseTest {

    private static int count = 0;
    private static final int RUN_COUNT = 10 * 10000;

    /**
     * 测试异常耗时
     * 输出异常堆栈&信息
     */
    @Test
    public void printStack() {
        try {
            Storey1.test();
            Storey1.testException();
        } catch (Exception e) {
        }
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            log.info(Storey1.test());
        }

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            try {
                Storey1.testException();
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        long end = System.currentTimeMillis();
        log.info("普通返回耗时：{},异常返回耗时:{}", start2 - start1, end - start2);
    }

    /**
     * 测试异常耗时
     * 仅仅输出信息
     */
    @Test
    public void print() {
        try {
            Storey1.test();
            Storey1.testException();
        } catch (Exception e) {
        }

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            log.info(Storey1.test());
        }

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            try {
                Storey1.testException();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        long end = System.currentTimeMillis();
        log.info("普通返回耗时：{},异常返回耗时:{}", start2 - start1, end - start2);
    }

    /**
     * 测试异常耗时
     * 不输出信息
     */
    @Test
    public void noPrint() {
        try {
            Storey1.test();
            Storey1.testException();
        } catch (Exception e) {
        }

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            Storey1.test();
        }

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            try {
                Storey1.testException();
            } catch (Exception e) {
            }
        }
        long end = System.currentTimeMillis();
        log.info("普通返回耗时：{},异常返回耗时:{}", start2 - start1, end - start2);
    }


    /**
     * 测试异常耗时
     * 关闭堆栈 并且不打印
     */
    @Test
    public void noPrintCloseStackTrace() {
        try {
            Storey1.test();
            Storey1.testException();
        } catch (Exception e) {
        }

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            Storey1.test();
        }

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < RUN_COUNT; i++) {
            try {
                Storey1.testException();
            } catch (Exception e) {
            }
        }
        long end = System.currentTimeMillis();
        log.info("普通返回耗时：{},异常返回耗时:{}", start2 - start1, end - start2);
    }


    public static class Storey1 {
        public static String test() {
            return Storey2.test();
        }

        public static String testException() {
            return Storey2.testException();
        }
    }

    public static class Storey2 {
        public static String test() {
            return Storey3.test();
        }

        public static String testException() {
            return Storey3.testException();
        }
    }

    public static class Storey3 {
        public static String test() {
            return Storey4.test();
        }

        public static String testException() {
            return Storey4.testException();
        }
    }

    public static class Storey4 {
        public static String test() {
            return Storey5.test();
        }

        public static String testException() {
            return Storey5.testException();
        }
    }

    public static class Storey5 {
        public static String test() {
            return Integer.toString(count++);
        }

        public static String testException() {
//            throw new CustomException(Integer.toString(count++));
            throw new CustomException(Integer.toString(count++), null, false, false);
        }
    }


    public static class CustomException extends RuntimeException {

        public CustomException(String message) {
            super(message);
        }

        public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


}
