package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.BusinessException;
import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;

/**
 * 默认的异常处理
 * 直接抛出系统异常
 *
 * @author Jiaju Zhuang
 */
public class DefaultExceptionConvertor implements ExceptionConvertor<Throwable> {

    @Override
    public ActionResult convert(Throwable exception) {
        if (exception instanceof BusinessException businessException) {
            return ActionResult.fail(businessException.getCode(), businessException.getMessage());
        }
        return ActionResult.fail(CommonExceptionEnum.SYSTEM_ERROR);
    }
}
