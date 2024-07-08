# 大厂面试必备系列：一文彻底搞懂 Cglib 代理

## 前言

大家在面试中经常被问到 Cglib 和 JDK动态代理有啥区别？ 然后每次回答都是 Cglib 通过创建目标类的子类来实现代理。

这个回答当然是对的，但是太敷衍了，没得加分，今天我带大家深入了解下。

## 最佳实践

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/exception/ExceptionWebController.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/exception/ExceptionWebController.java)

这里需要配合 "统一 `Reuslt` 对象去封装返回值"
一起使用,可以打开 [https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools) 找到案例

### 业务代码中直接抛出异常即可

```java
 /**
 * demo/异常模板
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/result")
public class ExceptionWebController {

    /**
     * 测试业务异常
     *
     * @return
     */
    @GetMapping("business-exception")
    public ActionResult businessException() {
        // 直接抛出异常，不用返回ActionResult
        throw BusinessException.of("业务异常");
    }

    /**
     * 测试系统异常
     *
     * @return
     */
    @GetMapping("system-exception")
    public ActionResult systemException() {
        // 直接抛出异常，不用返回ActionResult
        throw SystemException.of("系统异常");
    }

}

```

返回的结果：

```json
{
  "success": false,
  "errorCode": "BUSINESS_ERROR",
  "errorMessage": "业务异常",
  "traceId": null
}

{
  "success": false,
  "errorCode": "SYSTEM_ERROR",
  "errorMessage": "系统开小差啦，请尝试刷新页面或者联系管理员",
  "traceId": null
}
```

输出的异常日志：

```java
2024-07-04T20:54:40.903+08:00INFO 17360---[nio-8080-exec-1]c.g.z.e.w.c.h.ControllerExceptionHandler :发生业务异常/api/web/result/business-exception:

ActionResult(success=false, errorCode=BUSINESS_ERROR, errorMessage=业务异常, traceId=null)

com.github.zhuangjiaju.easytools.tools.base.excption.BusinessException:业务异常
at com.github.zhuangjiaju.easytools.tools.base.excption.BusinessException.

of(BusinessException.java:47)

at com.github.zhuangjiaju.easytools.web.web.contoller.exception.ExceptionWebController.

businessException(ExceptionWebController.java:30)

at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.

invoke(DirectMethodHandleAccessor.java:103)

at java.base/java.lang.reflect.Method.

invoke(Method.java:580)

at org.springframework.web.method.support.InvocableHandlerMethod.

doInvoke(InvocableHandlerMethod.java:255)

at org.springframework.web.method.support.InvocableHandlerMethod.

invokeForRequest(InvocableHandlerMethod.java:188)

at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.

invokeAndHandle(ServletInvocableHandlerMethod.java:118)

at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.

invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)

at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.

handleInternal(RequestMappingHandlerAdapter.java:831)

at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.

handle(AbstractHandlerMethodAdapter.java:87)

at org.springframework.web.servlet.DispatcherServlet.

doDispatch(DispatcherServlet.java:1089)

at org.springframework.web.servlet.DispatcherServlet.

doService(DispatcherServlet.java:979)

at org.springframework.web.servlet.FrameworkServlet.

processRequest(FrameworkServlet.java:1014)

at org.springframework.web.servlet.FrameworkServlet.

doGet(FrameworkServlet.java:903)

at jakarta.servlet.http.HttpServlet.

service(HttpServlet.java:564)

at org.springframework.web.servlet.FrameworkServlet.

service(FrameworkServlet.java:885)

at jakarta.servlet.http.HttpServlet.

service(HttpServlet.java:658)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:195)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.apache.tomcat.websocket.server.WsFilter.

doFilter(WsFilter.java:51)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.RequestContextFilter.

doFilterInternal(RequestContextFilter.java:100)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.FormContentFilter.

doFilterInternal(FormContentFilter.java:93)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.CharacterEncodingFilter.

doFilterInternal(CharacterEncodingFilter.java:201)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.apache.catalina.core.StandardWrapperValve.

invoke(StandardWrapperValve.java:167)

at org.apache.catalina.core.StandardContextValve.

invoke(StandardContextValve.java:90)

at org.apache.catalina.authenticator.AuthenticatorBase.

invoke(AuthenticatorBase.java:482)

at org.apache.catalina.core.StandardHostValve.

invoke(StandardHostValve.java:115)

at org.apache.catalina.valves.ErrorReportValve.

invoke(ErrorReportValve.java:93)

at org.apache.catalina.core.StandardEngineValve.

invoke(StandardEngineValve.java:74)

at org.apache.catalina.connector.CoyoteAdapter.

service(CoyoteAdapter.java:344)

at org.apache.coyote.http11.Http11Processor.

service(Http11Processor.java:389)

at org.apache.coyote.AbstractProcessorLight.

process(AbstractProcessorLight.java:63)

at org.apache.coyote.AbstractProtocol$ConnectionHandler.

process(AbstractProtocol.java:904)

at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.

doRun(NioEndpoint.java:1741)

at org.apache.tomcat.util.net.SocketProcessorBase.

run(SocketProcessorBase.java:52)

at org.apache.tomcat.util.threads.ThreadPoolExecutor.

runWorker(ThreadPoolExecutor.java:1190)

at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.

run(ThreadPoolExecutor.java:659)

at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.

run(TaskThread.java:63)

at java.base/java.lang.Thread.

run(Thread.java:1583)

2024-07-04T20:54:47.361+08:00ERROR 17360---[nio-8080-exec-2]c.g.z.e.w.c.h.ControllerExceptionHandler :发生业务异常/api/web/result/system-exception:

ActionResult(success=false, errorCode=SYSTEM_ERROR, errorMessage=系统开小差啦，请尝试刷新页面或者联系管理员,
    traceId=null)

com.github.zhuangjiaju.easytools.tools.base.excption.SystemException:系统异常
at com.github.zhuangjiaju.easytools.tools.base.excption.SystemException.

of(SystemException.java:47)

at com.github.zhuangjiaju.easytools.web.web.contoller.exception.ExceptionWebController.

systemException(ExceptionWebController.java:40)

at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.

invoke(DirectMethodHandleAccessor.java:103)

at java.base/java.lang.reflect.Method.

invoke(Method.java:580)

at org.springframework.web.method.support.InvocableHandlerMethod.

doInvoke(InvocableHandlerMethod.java:255)

at org.springframework.web.method.support.InvocableHandlerMethod.

invokeForRequest(InvocableHandlerMethod.java:188)

at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.

invokeAndHandle(ServletInvocableHandlerMethod.java:118)

at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.

invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)

at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.

handleInternal(RequestMappingHandlerAdapter.java:831)

at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.

handle(AbstractHandlerMethodAdapter.java:87)

at org.springframework.web.servlet.DispatcherServlet.

doDispatch(DispatcherServlet.java:1089)

at org.springframework.web.servlet.DispatcherServlet.

doService(DispatcherServlet.java:979)

at org.springframework.web.servlet.FrameworkServlet.

processRequest(FrameworkServlet.java:1014)

at org.springframework.web.servlet.FrameworkServlet.

doGet(FrameworkServlet.java:903)

at jakarta.servlet.http.HttpServlet.

service(HttpServlet.java:564)

at org.springframework.web.servlet.FrameworkServlet.

service(FrameworkServlet.java:885)

at jakarta.servlet.http.HttpServlet.

service(HttpServlet.java:658)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:195)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.apache.tomcat.websocket.server.WsFilter.

doFilter(WsFilter.java:51)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.RequestContextFilter.

doFilterInternal(RequestContextFilter.java:100)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.FormContentFilter.

doFilterInternal(FormContentFilter.java:93)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.springframework.web.filter.CharacterEncodingFilter.

doFilterInternal(CharacterEncodingFilter.java:201)

at org.springframework.web.filter.OncePerRequestFilter.

doFilter(OncePerRequestFilter.java:116)

at org.apache.catalina.core.ApplicationFilterChain.

internalDoFilter(ApplicationFilterChain.java:164)

at org.apache.catalina.core.ApplicationFilterChain.

doFilter(ApplicationFilterChain.java:140)

at org.apache.catalina.core.StandardWrapperValve.

invoke(StandardWrapperValve.java:167)

at org.apache.catalina.core.StandardContextValve.

invoke(StandardContextValve.java:90)

at org.apache.catalina.authenticator.AuthenticatorBase.

invoke(AuthenticatorBase.java:482)

at org.apache.catalina.core.StandardHostValve.

invoke(StandardHostValve.java:115)

at org.apache.catalina.valves.ErrorReportValve.

invoke(ErrorReportValve.java:93)

at org.apache.catalina.core.StandardEngineValve.

invoke(StandardEngineValve.java:74)

at org.apache.catalina.connector.CoyoteAdapter.

service(CoyoteAdapter.java:344)

at org.apache.coyote.http11.Http11Processor.

service(Http11Processor.java:389)

at org.apache.coyote.AbstractProcessorLight.

process(AbstractProcessorLight.java:63)

at org.apache.coyote.AbstractProtocol$ConnectionHandler.

process(AbstractProtocol.java:904)

at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.

doRun(NioEndpoint.java:1741)

at org.apache.tomcat.util.net.SocketProcessorBase.

run(SocketProcessorBase.java:52)

at org.apache.tomcat.util.threads.ThreadPoolExecutor.

runWorker(ThreadPoolExecutor.java:1190)

at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.

run(ThreadPoolExecutor.java:659)

at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.

run(TaskThread.java:63)

at java.base/java.lang.Thread.

run(Thread.java:1583)

```

### 使用 ExceptionHandler 拦截业务中抛出异常

所有的异常转换 由`ExceptionConvertorUtils` 统一转换成输出 `ActionResult` 对象，并返回给前端

```java
/**
 * 拦截Controller异常
 *
 * @author Jiaju Zhuang
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 业务异常
     * 这里整合了spring常见的异常
     *
     * @param request   request
     * @param exception exception
     * @return return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class,
        MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
        BusinessException.class, MaxUploadSizeExceededException.class, ClientAbortException.class,
        HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotAcceptableException.class,
        MultipartException.class, MissingRequestHeaderException.class, HttpMediaTypeNotSupportedException.class,
        ConstraintViolationException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ActionResult handleBusinessException(HttpServletRequest request, Exception exception) {
        ActionResult result = ExceptionConvertorUtils.convert(exception);
        log.info("发生业务异常{}:{}", request.getRequestURI(), result, exception);
        return result;
    }

    /**
     * 系统异常
     *
     * @param request   request
     * @param exception exception
     * @return return
     */
    @ExceptionHandler({SystemException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ActionResult handleSystemException(HttpServletRequest request, Exception exception) {
        ActionResult result = ExceptionConvertorUtils.convert(exception);
        log.error("发生业务异常{}:{}", request.getRequestURI(), result, exception);
        return result;
    }

    /**
     * 未知异常 需要人工介入查看日志
     *
     * @param request   request
     * @param exception exception
     * @return return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ActionResult handledException(HttpServletRequest request, Exception exception) {
        ActionResult result = ExceptionConvertorUtils.convert(exception);
        log.error("发生未知异常{}:{}:{},请求参数:{}", request.getRequestURI(),
            ExceptionConvertorUtils.buildHeaderString(request), result,
            JSON.toJSONString(request.getParameterMap()), exception);
        return result;
    }

}

```

### ExceptionConvertorUtils 根据不同的异常来转换异常信息

自己可以在 `EXCEPTION_CONVERTOR_MAP` 中加入更多的自定义异常，可以无限扩展

```java
/**
 * 转换异常工具类
 *
 * @author Jiaju Zhuang
 */
public class ExceptionConvertorUtils {

    /**
     * 所有的异常处理转换器
     */
    public static final Map<Class<?>, ExceptionConvertor> EXCEPTION_CONVERTOR_MAP = Maps.newHashMap();

    static {
        EXCEPTION_CONVERTOR_MAP.put(MethodArgumentNotValidException.class,
            new MethodArgumentNotValidExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(BindException.class, new BindExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(BusinessException.class, new BusinessExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(MissingServletRequestParameterException.class, new ParamExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(IllegalArgumentException.class, new ParamExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(MethodArgumentTypeMismatchException.class,
            new MethodArgumentTypeMismatchExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(MaxUploadSizeExceededException.class,
            new MaxUploadSizeExceededExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(HttpRequestMethodNotSupportedException.class, new BusinessExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(ConstraintViolationException.class, new ConstraintViolationExceptionConvertor());
        EXCEPTION_CONVERTOR_MAP.put(HttpMessageNotReadableException.class,
            new ParamExceptionConvertor());
    }

    /**
     * 默认转换器
     */
    public static ExceptionConvertor DEFAULT_EXCEPTION_CONVERTOR = new DefaultExceptionConvertor();

    /**
     * 提取ConstraintViolationException中的错误消息
     *
     * @param e
     * @return
     */
    public static String buildMessage(ConstraintViolationException e) {
        if (e == null || CollectionUtils.isEmpty(e.getConstraintViolations())) {
            return null;
        }
        int index = 1;
        StringBuilder msg = new StringBuilder();
        msg.append("请检查以下信息：");
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            msg.append(index++);
            // 得到错误消息
            msg.append(SymbolConstant.DOT);
            msg.append(" 字段(");
            msg.append(constraintViolation.getPropertyPath());
            msg.append(")传入的值为：\"");
            msg.append(constraintViolation.getInvalidValue());
            msg.append("\"，校验失败,原因是：");
            msg.append(constraintViolation.getMessage());
            msg.append(SymbolConstant.SEMICOLON);
        }
        return msg.toString();
    }

    /**
     * 提取BindingResult中的错误消息
     *
     * @param result
     * @return
     */
    public static String buildMessage(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        if (CollectionUtils.isEmpty(errors)) {
            return null;
        }

        int index = 1;
        StringBuilder msg = new StringBuilder();
        msg.append("请检查以下信息：");
        for (ObjectError e : errors) {
            msg.append(index++);
            // 得到错误消息
            msg.append(SymbolConstant.DOT);
            msg.append(" ");
            msg.append("字段(");
            msg.append(e.getObjectName());
            if (e instanceof FieldError) {
                FieldError fieldError = (FieldError)e;
                msg.append(SymbolConstant.DOT);
                msg.append(fieldError.getField());
            }
            msg.append(")");
            if (e instanceof FieldError) {
                FieldError fieldError = (FieldError)e;
                msg.append("传入的值为：\"");
                msg.append(fieldError.getRejectedValue());
                msg.append("\",");
            }
            msg.append("校验失败,原因是：");
            msg.append(e.getDefaultMessage());
            msg.append(SymbolConstant.SEMICOLON);
        }
        return msg.toString();
    }

    /**
     * 拼接头的日志信息
     *
     * @param request
     * @return
     */
    public static String buildHeaderString(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            stringBuilder.append(headName);
            stringBuilder.append(SymbolConstant.COLON);
            stringBuilder.append(request.getHeader(headName));
            stringBuilder.append(SymbolConstant.COMMA);
        }
        return stringBuilder.toString();
    }

    /**
     * 转换结果
     *
     * @param exception
     * @return
     */
    public static ActionResult convert(Throwable exception) {
        ExceptionConvertor exceptionConvertor = EXCEPTION_CONVERTOR_MAP.get(exception.getClass());
        if (exceptionConvertor == null) {
            exceptionConvertor = DEFAULT_EXCEPTION_CONVERTOR;
        }
        return exceptionConvertor.convert(exception);
    }

}

```

### 拿一个 ParamExceptionConvertor 参数异常举例

```java

/**
 * 参数异常 目前包括
 * ConstraintViolationException
 * MissingServletRequestParameterException
 * IllegalArgumentException
 * HttpMessageNotReadableException
 *
 * @author Jiaju Zhuang
 */
public class ParamExceptionConvertor implements ExceptionConvertor<Throwable> {

    @Override
    public ActionResult convert(Throwable exception) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, exception.getMessage());
    }
}


```

### 总结

这样子我们就完成了全局异常处理，再也不用担心整个项目的 `try-catch` 代码了。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)