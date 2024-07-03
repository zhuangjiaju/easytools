package com.github.zhuangjiaju.easytools.tools.base.excption;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常。不需要人工介入的叫做业务异常。
 *
 * @author Jiaju Zhuang
 */
@Getter
@Setter
public class DataNotExistException extends BusinessException {

    public DataNotExistException() {
        super(CommonExceptionEnum.DATA_NOT_EXIST, "数据不存在");
    }

    public DataNotExistException(String message) {
        super(CommonExceptionEnum.DATA_NOT_EXIST, message);
    }

    public static DataNotExistException of(String message) {
        return new DataNotExistException(message);
    }

    public static DataNotExistException newInstance() {
        return new DataNotExistException();
    }
}
