package com.github.zhuangjiaju.easytools.tools.base.excption;

import com.github.zhuangjiaju.easytools.tools.base.enums.BaseExceptionEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常。不需要人工介入的叫做业务异常。
 *
 * @author Jiaju Zhuang
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {
    /**
     * 异常的编码
     */
    private String code;

    public BusinessException(String message) {
        this(CommonExceptionEnum.BUSINESS_ERROR, message);
    }

    public BusinessException(String message, Throwable throwable) {
        this(CommonExceptionEnum.BUSINESS_ERROR, message, throwable);
    }

    public BusinessException(BaseExceptionEnum exceptionEnum, String message) {
        this(exceptionEnum.getCode(), message);
    }

    public BusinessException(BaseExceptionEnum exceptionEnum, String message, Throwable throwable) {
        super(message, throwable);
        this.code = exceptionEnum.getCode();
    }

    public BusinessException(BaseExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getDescription());
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    public static BusinessException of(String code, String message) {
        return new BusinessException(code, message);
    }

    public static BusinessException of(BaseExceptionEnum exceptionEnum, String message, Throwable throwable) {
        return new BusinessException(exceptionEnum, message, throwable);
    }

    public static BusinessException of(BaseExceptionEnum exceptionEnum) {
        return new BusinessException(exceptionEnum);
    }

    public static BusinessException of(BaseExceptionEnum exceptionEnum, String message) {
        return new BusinessException(exceptionEnum, message);
    }
}
