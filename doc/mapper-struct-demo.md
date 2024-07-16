# Java对象复制系列四: 还在使用 Spring BeanUtils 复制属性？这个工具性能秒杀它

## 前言

我们在平时的工作中经常会遇到两个对象的拷贝，很多同学使用 Spring BeanUtils ，感觉性能不错。

但是他无法解决属性名不一样的情况，也无法解决属性类型的转换，性能和原生的 get/set 比也差的很多。

今天我来介绍一款性能堪比原生 get/set 的对象复制工具：MapperStruct。

对 Spring BeanUtils 或者 Apache BeanUtils 原理好奇的同学可以去主页查看对应的文章。

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest.java)

### 使用 MapperStruct 复制对象

直接上代码,其中：

* MapperStructSourceDTO 存在 id(String) firstName(String) lastName(String) age(String)
* MapperStructTargetDTO 存在 id(Long) name(lastName+firstName) age(Integer)

我们的需求：

* id -> id 需要字符串转Long
* lastName + firstName -> name 涉及到字符串拼接
* age -> age 需要字符串转Integer

复制的来源对象:

```java
/**
 * MapperStruct的案例
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructSourceDTO {

    /**
     * 学生ID
     */
    private String id;
    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 年龄
     */
    private String age;

    /**
     * 邮箱
     */
    private String email;

}
```

复制的目标对象:

```java
/**
 * MapperStruct的案例
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructTargetDTO {

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

}
```

转换器代码：

```java
/**
 * 转换器
 *
 * @author Jiaju Zhuang
 */
@Mapper
public interface MapperStructMapper {

    /**
     * 固定写法 不用管 获取一个 MapperStructMapper 实例
     */
    MapperStructMapper INSTANCE = Mappers.getMapper(MapperStructMapper.class);

    BeanUtilsDemoDTO dto2dto(BeanUtilsDemoDTO dto);

    /**
     * 将 source 转换为 target
     *
     * @param source
     * @return
     */
    @Mappings({
        // id 转存 id 我们实际上可以不写 他自己会转换
        @Mapping(target = "id", source = "id"),
        // lastName+ firstName 转存 name ,需要我们自己写表达式
        @Mapping(target = "name", expression = "java(source.getLastName()+source.getFirstName())"),
        // age 转存 age 我们就不写了 一样的他自己会转换
        //@Mapping(target = "age", source = "age"),
        // email 我们不想要给我转换
        @Mapping(target = "email", ignore = true),
    })
    MapperStructTargetDTO dto2dto(MapperStructSourceDTO source);

}

```

构建一个对象 然后直接调用转换器即可：

```java
/**
 * MapperStruct 使用的demo
 */
@Test
public void demo() throws Exception {
    // MapperStructSourceDTO  存在 id(String) firstName(String) lastName(String) age(String)
    // MapperStructTargetDTO  存在 id(Long) name(lastName+firstName) age(Integer)
    // 我们的需求变得复杂了 不仅仅存在 字符在转成数字 还存在字符串拼接 这个显然 BeanUtils 搞不定了
    MapperStructSourceDTO source = new MapperStructSourceDTO();
    source.setId("1");
    source.setLastName("Zhuang");
    source.setFirstName("Jiaju");
    source.setAge("18");

    // 转换对象
    MapperStructTargetDTO target = MapperStructMapper.INSTANCE.dto2dto(source);
    log.info("newBeanUtilsDemo: {}", JSON.toJSONString(target));
}
```

输出结果:

```text
21:13:50.980 [main] INFO com.github.zhuangjiaju.easytools.test.demo.mapperstruct.MapperStructTest -- newBeanUtilsDemo: {"age":18,"id":1,"name":"ZhuangJiaju"}
```

可见已经复制对象成功了，而且完全按照了我们的要求。

### 性能测试

接下来 我们看看 Apache BeanUtils、MapperStruct、直接get/set 的性能对比

代码我就不贴了，可以打开案例地址看到代码。

|                  | 1次   | 100次 | 1000次 | 10000次 | 100000次 |
|------------------|------|------|-------|--------|---------|
| Apache BeanUtils | 37ms | 18ms | 61ms  | 269ms  | 1909ms  |
| MapperStruct     | 2ms  | 0ms  | 1ms   | 2ms    | 9ms     |
| 直接get/set        | 0ms  | 0ms  | 1ms   | 1ms    | 7ms     |

发现没有，MapperStruct 性能已经无限接近 直接get/set 了，所以这个在工作中可以作为一个首选。

### 总结

今天带大家基本了解了 MapperStruct 的用法，并且发现了他卓越的性能，毫无疑问可以抛弃 Spring 和 Apache 的 BeanUtils 了。

但是，为什么他的性能会这么好呢？ 其他还有什么更高级的用法呢？

带着疑问，我们下一节再见。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
