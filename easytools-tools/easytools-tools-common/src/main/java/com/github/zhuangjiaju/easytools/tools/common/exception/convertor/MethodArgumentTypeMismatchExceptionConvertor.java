package com.github.zhuangjiaju.easytools.tools.common.exception.convertor;

import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

;

/**
 * MethodArgumentTypeMismatchException
 *
 * @author Jiaju Zhuang
 */
public class MethodArgumentTypeMismatchExceptionConvertor
    implements ExceptionConvertor<MethodArgumentTypeMismatchException> {

    @Override
    public ActionResult convert(MethodArgumentTypeMismatchException exception) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "请输入正确的数据格式");
    }
}
