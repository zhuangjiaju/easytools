package com.github.zhuangjiaju.easytools.tools.base.excption;

import com.github.zhuangjiaju.easytools.tools.base.enums.BaseExceptionEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常。简单的说就是需要人工介入的异常叫做系统异常。
 *
 * @author Jiaju Zhuang
 */
@Getter
@Setter
public class SystemException extends RuntimeException {
    /**
     * 异常的编码
     */
    private String code;

    public SystemException(String message) {
        this(CommonExceptionEnum.SYSTEM_ERROR, message);
    }

    public SystemException(String message, Throwable throwable) {
        this(CommonExceptionEnum.SYSTEM_ERROR, message, throwable);
    }

    public SystemException(BaseExceptionEnum exceptionEnum, String message) {
        this(exceptionEnum.getCode(), message);
    }

    public SystemException(BaseExceptionEnum exceptionEnum, String message, Throwable throwable) {
        super(message, throwable);
        this.code = exceptionEnum.getCode();
    }

    public SystemException(BaseExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getDescription());
    }

    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static SystemException of(String message) {
        return new SystemException(message);
    }

    public static SystemException of(String code, String message) {
        return new SystemException(code, message);
    }

    public static SystemException of(BaseExceptionEnum exceptionEnum, String message, Throwable throwable) {
        return new SystemException(exceptionEnum, message, throwable);
    }

    public static SystemException of(BaseExceptionEnum exceptionEnum) {
        return new SystemException(exceptionEnum);
    }

    public static SystemException of(BaseExceptionEnum exceptionEnum, String message) {
        return new SystemException(exceptionEnum, message);
    }
}
