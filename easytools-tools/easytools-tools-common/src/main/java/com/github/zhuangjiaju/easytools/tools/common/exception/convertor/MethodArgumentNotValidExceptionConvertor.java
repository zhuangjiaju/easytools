package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import com.github.zhuangjiaju.easytools.tools.common.util.ExceptionConvertorUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * MethodArgumentNotValidException
 *
 * @author Jiaju Zhuang
 */
public class MethodArgumentNotValidExceptionConvertor implements ExceptionConvertor<MethodArgumentNotValidException> {

    @Override
    public ActionResult convert(MethodArgumentNotValidException exception) {
        String message = ExceptionConvertorUtils.buildMessage(exception.getBindingResult());
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, message);
    }
}
