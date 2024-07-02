package cn.easytools.tools.base.excption;

import cn.easytools.tools.base.enums.BaseErrorEnum;

import lombok.Getter;

/**
 * 业务异常。不需要人工介入的叫做业务异常。
 *
 * @author Jiaju Zhuang
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 异常的编码
     */
    private String code;

    public BusinessException(String message) {
        this(CommonErrorEnum.COMMON_BUSINESS_ERROR, message);
    }

    public BusinessException(String message, Throwable throwable) {
        this(CommonErrorEnum.COMMON_BUSINESS_ERROR, message, throwable);
    }

    public BusinessException(BaseErrorEnum errorEnum, String message) {
        this(errorEnum.getCode(), message);
    }

    public BusinessException(BaseErrorEnum errorEnum, String message, Throwable throwable) {
        super(message, throwable);
        this.code = errorEnum.getCode();
    }

    public BusinessException(BaseErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getDescription());
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
