package com.github.zhuangjiaju.easytools.tools.base.wrapper.result;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import com.github.zhuangjiaju.easytools.tools.base.enums.BaseErrorEnum;
import com.github.zhuangjiaju.easytools.tools.base.excption.CommonErrorEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.Result;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.param.PageQueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * data的返回对象
 *
 * @author JiaJu Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable, Result<List<T>> {
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
     * 异常编码
     */
    private String errorCode;
    /**
     * 异常信息
     */
    private String errorMessage;
    /**
     * 数据信息
     */
    @Builder.Default
    private Page<T> data = new Page<>();
    /**
     * traceId
     */
    private String traceId;

    private PageResult(List<T> data, Long total, Long pageNum, Long pageSize) {
        this.success = Boolean.TRUE;
        this.data = new Page<>(data, total, pageNum, pageSize);
    }

    private PageResult(List<T> data, Long total, Integer pageNum, Integer pageSize) {
        this.success = Boolean.TRUE;
        this.data = new Page<>(data, total, pageNum, pageSize);
    }

    /**
     * 构建分页返回对象
     *
     * @param data     返回的对象
     * @param total    总的条数
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param <T>      返回的对象类型
     * @return 分页返回对象
     */
    public static <T> PageResult<T> of(List<T> data, Long total, Long pageNum, Long pageSize) {
        return new PageResult<>(data, total, pageNum, pageSize);
    }

    /**
     * 构建分页返回对象
     *
     * @param data     返回的对象
     * @param total    总的条数
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param <T>      返回的对象类型
     * @return 分页返回对象
     */
    public static <T> PageResult<T> of(List<T> data, Long total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(data, total, pageNum, pageSize);
    }

    /**
     * 构建分页返回对象
     *
     * @param data  返回的对象
     * @param total 总的条数
     * @param param 分页参数
     * @param <T>   返回的对象类型
     * @return 分页返回对象
     */
    public static <T> PageResult<T> of(List<T> data, Long total, PageQueryParam param) {
        return new PageResult<>(data, total, param.getPageNum(), param.getPageSize());
    }

    /**
     * 构建空的返回对象
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param <T>      返回的对象类型
     * @return 分页返回对象
     */
    public static <T> PageResult<T> empty(Long pageNum, Long pageSize) {
        return of(Collections.emptyList(), 0L, pageNum, pageSize);
    }

    /**
     * 构建空的返回对象
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param <T>      返回的对象类型
     * @return 分页返回对象
     */
    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return of(Collections.emptyList(), 0L, pageNum, pageSize);
    }

    /**
     * 判断是否还有下一页
     * 根据分页大小来计算 防止total为空
     *
     * @return 是否还有下一页
     * @deprecated 使用 {@link #getHasNextPage()} ()}
     */
    @Deprecated
    public boolean hasNextPage() {
        return getHasNextPage();
    }

    public Boolean getHasNextPage() {
        if (data == null) {
            return Boolean.FALSE;
        }
        return data.getHasNextPage();
    }

    /**
     * 返回查询异常信息
     *
     * @param errorCode    错误编码
     * @param errorMessage 错误信息
     * @param <T>          返回的对象
     * @return 分页返回对象
     */
    public static <T> PageResult<T> error(String errorCode, String errorMessage) {
        PageResult<T> result = new PageResult<>();
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        result.success = Boolean.FALSE;
        return result;
    }

    /**
     * 返回查询异常信息
     *
     * @param errorEnum 错误枚举
     * @param <T>       返回的对象
     * @return 分页返回对象
     */
    public static <T> PageResult<T> error(BaseErrorEnum errorEnum) {
        return error(errorEnum.getCode(), errorEnum.getDescription());
    }

    /**
     * 判断是否存在数据
     *
     * @param pageResult
     * @return 是否存在数据
     */
    public static boolean hasData(PageResult<?> pageResult) {
        return pageResult != null && pageResult.getSuccess() && pageResult.getData() != null
            && pageResult.getData().getData() != null && !pageResult.getData().getData().isEmpty();
    }

    /**
     * 将当前的类型转换成另外一个类型
     *
     * @param mapper 转换的方法
     * @param <R>    返回的类型
     * @return 分页返回对象
     */
    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> returnData = hasData(this) ? getData().getData().stream().map(mapper).collect(Collectors.toList())
            : Collections.emptyList();
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setSuccess(getSuccess());
        pageResult.setErrorCode(getErrorCode());
        pageResult.setErrorMessage(getErrorMessage());
        pageResult.setTraceId(getTraceId());
        // 重新设置一个分页信息
        Page<R> page = new Page<>();
        pageResult.setData(page);
        page.setData(returnData);
        page.setPageNum(data.getPageNum());
        page.setPageSize(data.getPageSize());
        page.setTotal(data.getTotal());
        return pageResult;
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

}
