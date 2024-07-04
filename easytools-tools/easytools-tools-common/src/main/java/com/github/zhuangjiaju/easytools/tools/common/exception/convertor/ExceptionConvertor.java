package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;

/**
 * 异常转换器
 *
 * @author Jiaju Zhuang
 */
public interface ExceptionConvertor<T extends Throwable> {

    /**
     * 转换异常
     *
     * @param exception
     * @return
     */
    ActionResult convert(T exception);
}
