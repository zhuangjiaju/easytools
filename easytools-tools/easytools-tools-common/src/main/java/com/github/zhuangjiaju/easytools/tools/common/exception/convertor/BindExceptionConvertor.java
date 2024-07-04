package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import com.github.zhuangjiaju.easytools.tools.common.util.ExceptionConvertorUtils;
import org.springframework.validation.BindException;

/**
 * BindException
 *
 * @author Jiaju Zhuang
 */
public class BindExceptionConvertor implements ExceptionConvertor<BindException> {

    @Override
    public ActionResult convert(BindException exception) {
        String message = ExceptionConvertorUtils.buildMessage(exception.getBindingResult());
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, message);
    }
}
