package com.github.zhuangjiaju.easytools.tools.base.excption;

import com.github.zhuangjiaju.easytools.tools.base.enums.BaseExceptionEnum;
import lombok.Getter;

/**
 * 通用的返回码定义
 *
 * @author Jiaju Zhuang
 */
@Getter
public enum CommonExceptionEnum implements BaseExceptionEnum {
    /**
     * 业务异常
     */
    BUSINESS_ERROR("当前数据存在异常"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("系统开小差啦，请尝试刷新页面或者联系管理员"),

    /**
     * 无效的参数
     */
    INVALID_PARAMETER("无效的参数"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST("数据不存在"),

    /**
     * 没有权限
     */
    PERMISSION_DENIED("权限不够"),

    ;

    CommonExceptionEnum(String description) {
        this.description = description;
    }

    final String description;

    @Override
    public String getCode() {
        return this.name();
    }
}
