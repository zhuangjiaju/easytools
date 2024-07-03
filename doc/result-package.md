# 使用统一 `Reuslt` 对象去封装返回值

## 前言

在工作中看到很多项目都封装了一个统一的Result对象来封装返回值，但是封装的经常不太友好，是否好奇大厂是怎么来做结果封装的呢？今天我来分享一个非常完善的实战。

我们可以到很多大项目都会使用统一的Result对象来封装返回值,那使用统一的Result对象有什么好处呢？

* 提升开发效率：统一封装Result可以显著提升开发效率。开发者无需在每个方法中重复编写错误处理和数据封装的逻辑，而是可以集中精力于业务逻辑的实现。这种封装方式简化了代码结构，使得开发人员能够快速地编写和测试代码。

* 增强代码的可读性和可维护性：良好的代码结构不仅有助于新成员快速理解项目，也方便现有团队成员进行维护和扩展。统一的Result封装使得所有API的返回格式一致，这大大增强了代码的可读性，降低了维护成本。

* 优化错误处理： 在Java开发中，错误处理是一个复杂且容易出错的部分。统一封装Result可以帮助开发者标准化错误处理流程，使得错误信息更加清晰和一致，便于调试和问题追踪。

* 支持前后端分离： 随着前后端分离架构的流行，统一的Result封装为前后端通信提供了一种标准化的数据格式。这不仅简化了前端开发者的工作，也使得后端API更加灵活和可重用。

* 适应异步编程模型： 在现代Java应用中，异步编程变得越来越普遍。统一封装Result可以很好地适应这种编程模型，使得异步任务的结果处理变得更加简洁和直观。

* 促进团队协作： 统一的编码标准和实践可以促进团队成员之间的协作。当每个成员都遵循相同的Result封装规范时，代码审查和团队协作将变得更加顺畅。

## 最佳实践

最佳实践不仅仅封装了Result,而且还提供了非常友好的调用方法。

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/result/ResultWebController.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/result/ResultWebController.java)

```java
 /**
 * 创建一条数据
 *
 * @param request 创建参数
 * @return id
 */
@PostMapping("create")
public DataResult<Long> create(@Valid @RequestBody ResultCreateRequest request) {
    return DataResult.of(resultDemoService.create(resultWebConverter.request2param(request)));
}

/**
 * 查询一条数据
 *
 * @param id 主键
 * @return
 */
@GetMapping("query")
public DataResult<ResultQueryVO> query(@Valid @NotNull Long id) {
    return DataResult.of(resultWebConverter.dto2voQuery(resultDemoService.queryExistent(id, null)));
}

/**
 * 分页查询列表数据
 *
 * @param request request
 * @return
 */
@GetMapping("page-query")
public PageResult<ResultPageQueryVO> pageQuery(@Valid ResultPageQueryRequest request) {
    return resultDemoService.pageQuery(resultWebConverter.request2param(request), null)
        .map(resultWebConverter::dto2voPageQuery);
}

```

返回结果：

```json
{
  "success": true,
  "errorCode": null,
  "errorMessage": null,
  "data": 1
}
```

### ActionResult 用于封装没有任何返回值

```java
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

```

### DataResult 用于封装单个返回值

```java
/**
 * data的返回对象
 *
 *@author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DataResult<T> implements Serializable, Result<T> {
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
     * 数据信息
     */
    private T data;

    /**
     * traceId
     */
    private String traceId;

    private DataResult(T data) {
        this();
        this.data = data;
    }

    /**
     * 构建返回对象
     *
     * @param data 需要构建的对象
     * @param <T>  需要构建的对象类型
     * @return 返回的结果
     */
    public static <T> DataResult<T> of(T data) {
        return new DataResult<>(data);
    }

    /**
     * 构建空的返回对象
     *
     * @param <T> 需要构建的对象类型
     * @return 返回的结果
     */
    public static <T> DataResult<T> empty() {
        return new DataResult<>();
    }

    /**
     * 构建异常返回
     *
     * @param errorCode    错误编码
     * @param errorMessage 错误信息
     * @param <T>          需要构建的对象类型
     * @return 返回的结果
     */
    public static <T> DataResult<T> error(String errorCode, String errorMessage) {
        DataResult<T> result = new DataResult<>();
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        result.success = false;
        return result;
    }

    /**
     * 构建异常返回
     *
     * @param baseException 错误枚举
     * @param <T>       需要构建的对象类型
     * @return 返回的结果
     */
    public static <T> DataResult<T> error(BaseExceptionEnum baseException) {
        return error(baseException.getCode(), baseException.getDescription());
    }

    /**
     * 判断是否存在数据
     *
     * @param dataResult
     * @return 是否存在数据
     */
    public static boolean hasData(DataResult<?> dataResult) {
        return dataResult != null && dataResult.getSuccess() && dataResult.getData() != null;
    }

    /**
     * 将当前的类型转换成另外一个类型
     *
     * @param mapper 转换的方法
     * @param <R>    返回的类型
     * @return 返回的结果
     */
    public <R> DataResult<R> map(Function<T, R> mapper) {
        R returnData = hasData(this) ? mapper.apply(getData()) : null;
        DataResult<R> dataResult = new DataResult<>();
        dataResult.setSuccess(getSuccess());
        dataResult.setErrorCode(getErrorCode());
        dataResult.setErrorMessage(getErrorMessage());
        dataResult.setData(returnData);
        dataResult.setTraceId(getTraceId());
        return dataResult;
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


```

### ListResult 用于返回一个列表

```java
/**
 * 列表的返回对象
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ListResult<T> implements Serializable, Result<T> {
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
     * 异常信息
     */
    private String errorMessage;
    /**
     * 数据信息
     */
    private List<T> data;
    /**
     * traceId
     */
    private String traceId;

    private ListResult(List<T> data) {
        this();
        this.data = data;
    }

    /**
     * 构建列表返回对象
     *
     * @param data 需要构建的对象
     * @param <T>  需要构建的对象类型
     * @return 返回的列表
     */
    public static <T> ListResult<T> of(List<T> data) {
        return new ListResult<>(data);
    }

    /**
     * 构建空的列表返回对象
     *
     * @param <T> 需要构建的对象类型
     * @return 返回的列表
     */
    public static <T> ListResult<T> empty() {
        return of(Collections.emptyList());
    }

    /**
     * 构建异常返回列表
     *
     * @param errorCode    错误编码
     * @param errorMessage 错误信息
     * @param <T>          需要构建的对象类型
     * @return 返回的列表
     */
    public static <T> ListResult<T> error(String errorCode, String errorMessage) {
        ListResult<T> result = new ListResult<>();
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        result.success = Boolean.TRUE;
        return result;
    }

    /**
     * 构建异常返回列表
     *
     * @param baseException 错误枚举
     * @param <T>       需要构建的对象类型
     * @return 返回的列表
     */
    public static <T> ListResult<T> error(BaseExceptionEnum baseException) {
        return error(baseException.getCode(), baseException.getDescription());
    }

    /**
     * 判断是否存在数据
     *
     * @param listResult
     * @return 是否存在数据
     */
    public static boolean hasData(ListResult<?> listResult) {
        return listResult != null && listResult.getSuccess() && listResult.getData() != null && !listResult.getData()
            .isEmpty();
    }

    /**
     * 将当前的类型转换成另外一个类型
     *
     * @param mapper 转换的方法
     * @param <R>    返回的类型
     * @return 分页返回对象
     */
    public <R> ListResult<R> map(Function<T, R> mapper) {
        List<R> returnData = hasData(this) ? getData().stream().map(mapper).collect(Collectors.toList())
            : Collections.emptyList();
        ListResult<R> listResult = new ListResult<>();
        listResult.setSuccess(getSuccess());
        listResult.setErrorCode(getErrorCode());
        listResult.setErrorMessage(getErrorMessage());
        listResult.setData(returnData);
        listResult.setTraceId(getTraceId());
        return listResult;
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


```

### PageResult 用于封装分页返回

```java
/**
 * 分页的返回对象
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
     * @param baseException 错误枚举
     * @param <T>       返回的对象
     * @return 分页返回对象
     */
    public static <T> PageResult<T> error(BaseExceptionEnum baseException) {
        return error(baseException.getCode(), baseException.getDescription());
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


```

### 总结
今天分享了4个返回的封装类，分别是ActionResult、DataResult、ListResult、PageResult，这4个类可以满足大部分的返回值封装，如果有特殊的返回值，可以根据这4个类进行扩展。相信一定会对你的工作有帮助，具体源码可以直接在最开始的案例里面复制。

## 写在最后

你是否在为找不到完整的项目搭建的最佳实践而烦恼呢？这里给你推荐一个非常完成的开源模板：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)