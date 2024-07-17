# Java对象复制系列五: 最快速度、方便的对象复制工具 Mapper Struct 的高阶应用

## 前言

Mapper Struct 是目前最好的 Java 对象复制的工具之一。

上一节我们讲到了 Mapper Struct 的应用，以及测试了他的性能，发现他的性能已经无限接近直接 get/set 了。

今天我们来讲一下如何集成spring 以及一些高阶用法。让你在工作中各种对象转换游刃有余。

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java)

### 先看完整代码

先看我们的对象转换需求：

1. 将一个 json 字符串 转成一个数组
2. 将一个 用户的DTO对象转成VO对象
3. 放入一个当前的日期
4. 放入一个唯一id

转换前的对象:

```java
/**
 * MapperStruct 对象
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructDTO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id的json
     */
    private String userIdJson;

    /**
     * 创建人
     */
    private UserDTO createUser;
}


```

转换后的对象：

```java
/**
 * 结果查询返回
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructQueryVO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id的json
     */
    private List<Long> userIdList;

    /**
     * 创建人
     */
    private UserVO createUser;

    /**
     * 当前日期
     */
    private Date currentDate;

    /**
     * 唯一id
     */
    private String uuid;
}

```

转换器代码：

```java
/**
 * 结果查询返回
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);

    @AfterMapping
    protected void afterMapperStructQuery(@MappingTarget MapperStructQueryVO target, MapperStructDTO source) {
        if (target == null) {
            return;
        }
        target.setUuid(UUID.fastUUID().toString());
    }
}


```

转换代码：

```java

/**
 * 查询一条数据
 *
 * @param id
 * @return
 */
@GetMapping("query")
public DataResult<MapperStructQueryVO> query(@RequestParam("id") Long id) {
    MapperStructDTO data = mapperStructDemoService.queryExistent(id);
    // 转换需求如下：
    // 1. 将一个 json 字符串 转成一个数组
    // 2. 将一个 用户的DTO对象转成VO对象
    // 3. 放入一个当前的日期
    // 4. 放入一个唯一id
    MapperStructQueryVO vo = mapperStructWebMapper.dto2vo(data);
    log.info("转换前:{}，转换后:{}", JSON.toJSONString(data), JSON.toJSONString(vo));
    return DataResult.of(vo);
}
```

输出结果：

```text
转换前:{"createUser":{"id":1,"name":"Jiaju Zhuang"},"id":11,"userIdJson":"[\"1\",\"2\"]"}，转换后:{"createUser":{"id":1,"name":"Jiaju Zhuang"},"currentDate":"2024-07-17 22:06:23.038","id":11,"userIdList":[1,2],"uuid":"eee3d4ab-bb8c-4e4c-95c5-4317a014c31a"}
```

返回给前端的结果：

```json
{
  "success": true,
  "errorCode": null,
  "errorMessage": null,
  "data": {
    "id": 11,
    "userIdList": [
      1,
      2
    ],
    "createUser": {
      "id": 1,
      "name": "Jiaju Zhuang"
    },
    "currentDate": "2024-07-17T14:06:23.038+00:00",
    "uuid": "eee3d4ab-bb8c-4e4c-95c5-4317a014c31a"
  },
  "traceId": null
}
```

可以看到返回给前端的已经是我们想要的结果了，接下来我们一步步分解。

### 怎么集成spring?

在转换器上面加入 @Mapper(componentModel = "spring") ，并且 componentModel 设置成 spring即可。 这样子 MapperStruct
就会把这个类注入到spring容器中。

```java

@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {
}
```

然后需要的用的地方直接注入即可,我们这里用了构造器注入，大家也可以用 @Resource 注入：

```java

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/mapper-struct")
public class MapperStructWebController {
    private final MapperStructWebMapper mapperStructWebMapper;
}

```

### 如何将一个 json 字符串 转成一个数组?

由于代码中经常会有这个需求，所以我们把他抽到一个通用的mapper 工具里面去。

```java
/**
 * 通用的web转换器
 *
 * @author Jiaju Zhuang
 */

@Mapper(componentModel = "spring")
public abstract class CommonWebMapper {

    /**
     * 将一个json 数组转成
     *
     * @param jsonString
     * @return
     */
    @Named("commonJsonString2LongList")
    public List<Long> commonJsonString2LongList(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        return JSON.parseArray(jsonString, Long.class);
    }
}


```

然后在 对象转换类里面加入 uses = {CommonWebMapper.class} 引入通用工具，最后用 qualifiedByName = "
commonJsonString2LongList" 调用我们指定的方法

```java

@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);
}

```

### 如何将一个特别常用的DTO转成VO

当然也可以用上面的方法，这里我们介绍另外一种方法，继承。我们的转换器继承了 BaseCommonWebMapper ，当 转换对象中 遇到了
UserDTO 转成 UserVO 的时候，他自己会去父类里面找有没有这个方法。

```java

/**
 * 结果查询返回
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring")
public abstract class BaseCommonWebMapper {

    /**
     * 用户对象转成vo
     *
     * @param user
     * @return
     */
    public abstract UserVO _userDto2UserVo(UserDTO user);
}


```

转换器中继承 BaseCommonWebMapper 即可：

```java

@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);
}

```

### 如何在转换的时候自己写代码

这个特别简单，使用 expression 即可，这样子我们就可以自己在表达式里面写代码了。当然要注意，如果遇到了方法不在当前类，需要使用
imports = {DateUtil.class} 引入该类。

```java

@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);
}

```

### 如何写特别复杂的逻辑代码

特别复杂的逻辑代码可以在转换器转换完以后，使用 @AfterMapping 注解，然后在这个方法里面写任何代码。 当内容转换完成以后，
MapperStruct 会自动执行这个方法。

```java
/**
 * 转换器
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);

    @AfterMapping
    protected void afterMapperStructQuery(@MappingTarget MapperStructQueryVO target, MapperStructDTO source) {
        if (target == null) {
            return;
        }
        target.setUuid(UUID.fastUUID().toString());
    }
}

```

### 总结

今天给大家介绍了各种高阶的使用方法，平时工作应该完全胜任了，代码会别的特别优雅，把复杂的逻辑写到转换器里面，代码会变得特别优雅。

那么问题来了，MapperStruct 究竟为什么可以做到这么高速，而且可以实现这么复杂的转换呢？

带着疑问，我们下一节再见。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
