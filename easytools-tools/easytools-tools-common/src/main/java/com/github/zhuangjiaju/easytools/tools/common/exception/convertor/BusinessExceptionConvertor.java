package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.BusinessException;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;

/**
 * BusinessException
 *
 * @author Jiaju Zhuang
 */
public class BusinessExceptionConvertor implements ExceptionConvertor<BusinessException> {

    @Override
    public ActionResult convert(BusinessException exception) {
        return ActionResult.fail(exception.getCode(), exception.getMessage());
    }
}
