package com.github.zhuangjiaju.easytools.tools.base.constant;

/**
 * 常量
 *
 * @author Jiaju Zhuang
 */
public interface EasyToolsConstant {
    /**
     * 最大分页大小
     */
    int MAX_PAGE_SIZE = 500;

    /**
     * 序列化id
     */
    long SERIAL_VERSION_UID = 1L;

    /**
     * 最大循环次数 防止很多循环进入死循环
     */
    int MAXIMUM_ITERATIONS = 10 * 1000;

}
