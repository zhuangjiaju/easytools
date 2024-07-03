# 使用统一 `Reuslt` 对象去封装返回值

## 前言

在工作中看到很多项目都封装了一个统一的Result对象来封装返回值，但是总感觉封装的差点意思，今天我来分享一个非常完善的实战。

我们可以到很多大项目都会使用统一的Result对象来封装返回值,那使用统一的Result对象有什么好处呢？

* 提升开发效率：统一封装Result可以显著提升开发效率。开发者无需在每个方法中重复编写错误处理和数据封装的逻辑，而是可以集中精力于业务逻辑的实现。这种封装方式简化了代码结构，使得开发人员能够快速地编写和测试代码。

* 增强代码的可读性和可维护性：良好的代码结构不仅有助于新成员快速理解项目，也方便现有团队成员进行维护和扩展。统一的Result封装使得所有API的返回格式一致，这大大增强了代码的可读性，降低了维护成本。

* 优化错误处理： 在Java开发中，错误处理是一个复杂且容易出错的部分。统一封装Result可以帮助开发者标准化错误处理流程，使得错误信息更加清晰和一致，便于调试和问题追踪。

* 支持前后端分离： 随着前后端分离架构的流行，统一的Result封装为前后端通信提供了一种标准化的数据格式。这不仅简化了前端开发者的工作，也使得后端API更加灵活和可重用。

* 适应异步编程模型： 在现代Java应用中，异步编程变得越来越普遍。统一封装Result可以很好地适应这种编程模型，使得异步任务的结果处理变得更加简洁和直观。

* 促进团队协作： 统一的编码标准和实践可以促进团队成员之间的协作。当每个成员都遵循相同的Result封装规范时，代码审查和团队协作将变得更加顺畅。

## 最佳实践
### 
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

## 写在最后
你是否在为找不到完整的项目搭建的最佳实践而烦恼呢？这里给你推荐一个非常完成的开源模板：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)