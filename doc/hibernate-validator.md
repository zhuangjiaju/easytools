# 基于hibernate-validator,java前端入参校验最佳实践

## 前言

今天要给大家介绍的是基于 spring + hibernate-validator 完成前端入参校验。

大家是不是在业务代码里面经常看到这种代码：

```java
/**
 * 坏的案例
 * 这里会有很多冗余的代码
 * 而且如果写成所有异常一起提示更复杂
 *
 * @return
 */
@RequestMapping(value = "/base-case")
public ActionResult baseCase(@RequestBody ValidRequest request) {
    if (request.getId() == null) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "id不能为空");
    }
    if (!Validator.isEmail(request.getEmail())) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "邮箱格式不正确");
    }
    if (StringUtils.length(request.getName()) > 10) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "名字最长为10个字符串");
    }
    if (!Validator.isMobile(request.getMobile())) {
        return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "手机格式不正确");
    }
    // 做业务逻辑
    log.info("请求参数:{}", JSON.toJSONString(request));
    return ActionResult.isSuccess();
}
```

各种冗余代码，导致几行的业务代码一半在写校验。今天给大家介绍一种快速的校验方式。

## 最佳实践

基本思路使用`spring`提供的`ExceptionHandler`注解来统一拦截异常，并返回给前端。

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/valid/ValidWebController.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/valid/ValidWebController.java)

这里需要配合 "告别代码中遍地的 try-catch，使用 spring 全局统一异常处理"
一起使用,可以打开 [https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools) 找到案例

### 业务代码只要写上注解即可

Controller 只要加上 `@Valid` 注解即可

```java
/**
 * demo/校验模板
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/valid")
public class ValidWebController {
    private final ResultDemoService resultDemoService;
    private final ResultWebConverter resultWebConverter;

    /**
     * 校验数据
     * 加了 @Valid 就可以自动校验参数了
     *
     * @param request 参数
     * @return id
     */
    @PostMapping("valid")
    public ActionResult valid(@Valid @RequestBody ValidRequest request) {
        // 做业务逻辑
        log.info("请求参数:{}", JSON.toJSONString(request));
        return ActionResult.isSuccess();
    }

}

```

对应的实体类加上各种注解:

```java 
/**
 * 校验请求
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValidRequest {

    /**
     * id
     * 校验不能为空
     */
    @NotNull
    private Long id;

    /**
     * 名字
     * 校验长度不能超过10
     */
    @NotNull
    @Length(max = 10, message = "名字长度最长为10")
    private String name;

    /**
     * 名字
     * 校验必须是电子邮箱格式
     */
    @NotNull
    @Email
    private String email;

    /**
     * 手机号
     * 校验1开头的11位数字
     */
    @NotNull
    @Pattern(regexp = "^1\\d{10}$")
    private String mobile;
}


```

返回的结果：
依次输了异常信息，并且会输出原始值，这里封装了很多代码，需要的化可以直接在上面的案例地址里面复制代码。

```json
{
  "success": false,
  "errorCode": "INVALID_PARAMETER",
  "errorMessage": "请检查以下信息：1. 字段(validRequest.email)传入的值为：\"zhuangjiaju\",校验失败,原因是：不是一个合法的电子邮件地址;2. 字段(validRequest.name)传入的值为：\"zhuangjiajuzhuangjiaju\",校验失败,原因是：名字长度最长为10;3. 字段(validRequest.mobile)传入的值为：\"100000\",校验失败,原因是：需要匹配正则表达式\"^1\\d{10}$\";4. 字段(validRequest.id)传入的值为：\"null\",校验失败,原因是：不能为null;",
  "traceId": null
}
```


### hibernate-validator 支持的常见注解

这里列举了一些常见的注解

| 注解                                          | 适用的字段数据类型                                                                                                                                                                                                                                                   | 使用说明                                                      |
|---------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| @AssertFalse                                | Boolean, boolean.                                                                                                                                                                                                                                           | 该注解验证值为false的元素,或者说要求验证元素的值为false.                        |
| @AssertTrue                                 | Boolean, boolean.                                                                                                                                                                                                                                           | 该注解验证值为true的元素,或者说要求验证元素的值为true.                          |
| @DecimalMax(value = "max", message = "msg") | BigDecimal,BigInteger,String,Byte,short,int,long and the any sub-type of Number and charSequence.                                                                                                                                                           | 该注解要求验证元素的值在max范围内即集合表示为[Targetalue,max]                  |
| @DecimalMin(value = "min", message = "msg") | BigDecimal,BigInteger,String,Byte,short,int,long and the any sub-type of Number and charSequence.                                                                                                                                                           | 该注解要求验证元素的值在min范围外即集合表示为[min,Targetalue]                  |
| @Max（value=max）                             | BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type ofCharSequence (the numeric value represented by the character sequence is evaluated), any sub-type of Number. | 验证注解的元素值小于等于@Max指定的value值,即值的区间表示为[TargetValue,max]       |
| @Min（value=min）                             | BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type ofCharSequence (the numeric value represented by the character sequence is evaluated), any sub-type of Number. | 验证注解的元素值大于等于@Max指定的value值,即值的区间表示为[min,TargetValue]       |
| @Size(min=最小值, max=最大值)                     | String, Collection, Map and arrays. Additionally supported by HV: any sub-type of CharSequence.                                                                                                                                                             | 验证注解的元素值的在区间[min,max]范围之内，如字符长度、集合大小                      |
| @Range(min=最小值, max=最大值)                    | CharSequence, Collection, Map and Arrays,BigDecimal, BigInteger, CharSequence, byte, short, int, long and the respective wrappers of the primitive types                                                                                                    | 验证注解的元素值在区间[最小值,最大值]范围内                                   |
| @Length(min=下限, max=上限)                     | CharSequence                                                                                                                                                                                                                                                | 验证注解的元素值长度在[min,max]区间内                                   |
| @NotBlank                                   | CharSequence                                                                                                                                                                                                                                                | 验证注解的元素值不为空（不为null,也不能是空格），@NotBlank只应用于字符串且在校验时去除字符串中的空格 |
| @NotEmpty                                   | CharSequence,Collection, Map and Arrays                                                                                                                                                                                                                     | 验证注解的元素值不为null且不为空（字符串长度不为0即可以为单个空格或者多空格、集合大小不为0）         |
| @NotNull                                    | Any type                                                                                                                                                                                                                                                    | 验证注解的元素值只是不能为null（注，可以为任意长度的空格）                           |
| @Null                                       | Any type                                                                                                                                                                                                                                                    | 验证注解的元素值必须为null                                           |
| @Future                                     | ava.util.Date, java.util.Calendar; Additionally supported by HV, if theJoda Time date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.                                                                              | 验证注解的元素值,即日期要比当前时间之后                                      |
| @Past                                       | ava.util.Date, java.util.Calendar; Additionally supported by HV, if theJoda Time date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.                                                                              | 验证注解的元素值,即日期要比当前时间之前                                      |
| @Email                                      | CharSequence                                                                                                                                                                                                                                                | 验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式               |
| @Pattern(regex=正则表达式, flag=)                | String. Additionally supported by HV: any sub-type of CharSequence.                                                                                                                                                                                         | 验证注解的元素值与指定的正则表达式匹配                                       |


### 总结

通过以上案例，我们用了非常优雅的代码来完成了前端入参校验，不仅代码简洁，而且可读性也非常高，大家可以在实际项目中使用。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)