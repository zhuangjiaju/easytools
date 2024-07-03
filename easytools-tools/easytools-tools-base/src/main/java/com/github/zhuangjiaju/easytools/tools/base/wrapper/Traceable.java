package com.github.zhuangjiaju.easytools.tools.base.wrapper;

/**
 * 是否可以跟踪
 *
 * @author Jiaju Zhuang
 */
public interface Traceable {
    /**
     * 获取 traceId
     *
     * @return traceId
     */
    String getTraceId();

    /**
     * 设置traceId
     *
     * @param traceId
     */
    void setTraceId(String traceId);
}
