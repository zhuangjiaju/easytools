package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import com.github.zhuangjiaju.easytools.tools.common.util.ExceptionConvertorUtils;
import jakarta.validation.ConstraintViolationException;

/**
 * ConstraintViolationException
 *
 * @author Jiaju Zhuang
 */
public class ConstraintViolationExceptionConvertor implements ExceptionConvertor<ConstraintViolationException> {

    @Override
    public ActionResult convert(ConstraintViolationException exception) {
        String message = ExceptionConvertorUtils.buildMessage(exception);
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, message);
    }
}
