package com.github.zhuangjiaju.easytools.tools.base.wrapper.result;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import com.github.zhuangjiaju.easytools.tools.base.enums.BaseExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.Result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * action的返回对象
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult implements Serializable, Result {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;
    /**
     * 是否成功
     *
     * @mock true
     */
    @NotNull
    @Builder.Default
    private Boolean success = Boolean.TRUE;

    /**
     * 错误编码
     *
     * @see BaseExceptionEnum
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 返回成功
     *
     * @return 运行结果
     */
    public static ActionResult isSuccess() {
        return new ActionResult();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public void success(boolean success) {
        this.success = success;
    }

    @Override
    public String errorCode() {
        return errorCode;
    }

    @Override
    public void errorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public void errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 返回失败
     *
     * @param errorCode    错误编码
     * @param errorMessage 错误信息
     * @return 运行结果
     */
    public static ActionResult fail(String errorCode, String errorMessage) {
        ActionResult result = new ActionResult();
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        result.success = Boolean.FALSE;
        return result;
    }

    /**
     * 返回失败
     *
     * @param baseException 错误枚举
     * @return 运行结果
     */
    public static ActionResult fail(BaseExceptionEnum baseException) {
        return fail(baseException.getCode(), baseException.getDescription());
    }

    /**
     * 返回失败
     *
     * @param baseException 错误枚举
     * @return 运行结果
     */
    public static ActionResult fail(BaseExceptionEnum baseException, String errorMessage) {
        return fail(baseException.getCode(), errorMessage);
    }

}
