# Java对象复制系列六: 史上最快的对象复制工具 Mapper Struct 原理浅析

## 前言

Mapper Struct 是目前最好的 Java 对象复制的工具之一。

上一节我们介绍了 Mapper Struct 高阶应用，今天我们来看下为什么可以做到这么快。

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-web/easytools-web-web/src/main/java/com/github/zhuangjiaju/easytools/web/web/contoller/mapperstruct/MapperStructWebController.java)

### 为什么 Mapper Struct 这么快？

Mapper Struct 会在编译期生成一个 MapperStructWebMapper 的实现类 MapperStructWebMapperImpl ,然后真正代码执行的时候调用的就是这个类，所以他的性就应该和
原生的get/set 一样快。只是编译期消耗一些性能。

先来看看我们转换器的代码：

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

在编辑改文件的时候，会在 `target/generated-sources/annotations/你的包名`下面生成一个 MapperStructWebMapperImpl。 我们打开看下：

```java

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-18T21:44:10+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class MapperStructWebMapperImpl extends MapperStructWebMapper {

    @Autowired
    private CommonWebMapper commonWebMapper;

    // 这个是 MapperStructWebMapper的父类 BaseCommonWebMapper 的方法，所以也一起实现了
    @Override
    public UserVO _userDto2UserVo(UserDTO arg0) {
        if (arg0 == null) {
            return null;
        }

        UserVO userVO = new UserVO();

        userVO.setId(arg0.getId());
        userVO.setName(arg0.getName());

        return userVO;
    }

    @Override
    public MapperStructQueryVO dto2vo(MapperStructDTO user) {
        if (user == null) {
            return null;
        }

        MapperStructQueryVO mapperStructQueryVO = new MapperStructQueryVO();

        // 这个使用了 通用的mapper 的调用
        //        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
        mapperStructQueryVO.setUserIdList(commonWebMapper.commonJsonString2LongList(user.getUserIdJson()));
        mapperStructQueryVO.setId(user.getId());
        // 这里检测到有 UserDTO -> UserVO 的方法直接调用了
        mapperStructQueryVO.setCreateUser(_userDto2UserVo(user.getCreateUser()));
        // 设置当前日期
        //         @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        mapperStructQueryVO.setCurrentDate(DateUtil.date());

        // 我们自己定义了 afterMapperStructQuery 所以会在最后调用
        //@AfterMapping
        //protected void afterMapperStructQuery(@MappingTarget MapperStructQueryVO target, MapperStructDTO source) {}
        afterMapperStructQuery(mapperStructQueryVO, user);

        return mapperStructQueryVO;
    }
}

```

可以看到我们生成的 MapperStructWebMapperImpl 实际上和我们自己写代码是一样，他实现了我们需要各种get/set，所以无需担心他的性能。

### Mapper Struct 是怎么做到的？

我们首先要了解什么是 JavaProcessor ，JavaProcessor 是 Java
编译器的扩展，它能够在编译期间扫描和处理源代码中的注解。JavaProcessor可以用来生成新的源代码、验证代码的正确性、收集统计信息等。它通过javax.annotation.processing包中的一组API来实现。

在 Mapper Struct 的包中 我们可以找到这个文件：META-INF/services/javax.annotation.processing.Processor

```text
org.mapstruct.ap.MappingProcessor

```

java 在编译期会读取这个文件 并回调 org.mapstruct.ap.MappingProcessor.process 方法。

```java

@Override
public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
    // nothing to do in the last round
    if (!roundEnvironment.processingOver()) {
        RoundContext roundContext = new RoundContext(annotationProcessorContext);

        // process any mappers left over from previous rounds
        Set<TypeElement> deferredMappers = getAndResetDeferredMappers();
        processMapperElements(deferredMappers, roundContext);

        // get and process any mappers from this round
        Set<TypeElement> mappers = getMappers(annotations, roundEnvironment);
        processMapperElements(mappers, roundContext);
    } else if (!deferredMappers.isEmpty()) {
        // If the processing is over and there are deferred mappers it means something wrong occurred and
        // MapStruct didn't generate implementations for those
        for (DeferredMapper deferredMapper : deferredMappers) {

            TypeElement deferredMapperElement = deferredMapper.deferredMapperElement;
            Element erroneousElement = deferredMapper.erroneousElement;
            String erroneousElementName;

            if (erroneousElement instanceof QualifiedNameable) {
                erroneousElementName = ((QualifiedNameable)erroneousElement).getQualifiedName().toString();
            } else {
                erroneousElementName =
                    erroneousElement != null ? erroneousElement.getSimpleName().toString() : null;
            }

            // When running on Java 8 we need to fetch the deferredMapperElement again.
            // Otherwise the reporting will not work properly
            deferredMapperElement = annotationProcessorContext.getElementUtils()
                .getTypeElement(deferredMapperElement.getQualifiedName());

            processingEnv.getMessager()
                .printMessage(
                    Kind.ERROR,
                    "No implementation was created for " + deferredMapperElement.getSimpleName() +
                        " due to having a problem in the erroneous element " + erroneousElementName + "." +
                        " Hint: this often means that some other annotation processor was supposed to" +
                        " process the erroneous element. You can also enable MapStruct verbose mode by setting" +
                        " -Amapstruct.verbose=true as a compilation argument.",
                    deferredMapperElement
                );
        }

    }

    return ANNOTATIONS_CLAIMED_EXCLUSIVELY;
}
```

org.mapstruct.ap.MappingProcessor.processMapperTypeElement
根据不同 Processors 不同 逻辑，这里一共9个：
0 = {MethodRetrievalProcessor@8078}
1 = {MapperCreationProcessor@8079}
2 = {CdiComponentProcessor@8080}
3 = {JakartaCdiComponentProcessor@8081}
4 = {Jsr330ComponentProcessor@8082}
5 = {JakartaComponentProcessor@8083}
6 = {SpringComponentProcessor@8084}
7 = {MapperRenderingProcessor@8085}
8 = {MapperServiceProcessor@8086}

```java
private void processMapperTypeElement(ProcessorContext context, TypeElement mapperTypeElement) {
    Object model = null;

    for (ModelElementProcessor<?, ?> processor : getProcessors()) {
        try {
            model = process(context, processor, mapperTypeElement, model);
        } catch (AnnotationProcessingException e) {
            processingEnv.getMessager()
                .printMessage(
                    Kind.ERROR,
                    e.getMessage(),
                    e.getElement(),
                    e.getAnnotationMirror(),
                    e.getAnnotationValue()
                );
            break;
        }
    }
}

```

中间的解析和处理逻辑非常复杂，我们这里就不分析了最后调用了：
org.mapstruct.ap.internal.processor.MapperRenderingProcessor
org.mapstruct.ap.internal.processor.MapperRenderingProcessor.writeToSourceFile
org.mapstruct.ap.internal.processor.MapperRenderingProcessor.createSourceFile

```java
    private void createSourceFile(GeneratedType model, ModelWriter modelWriter, Filer filer,
    TypeElement originatingElement) {
    String fileName = "";
    if (model.hasPackageName()) {
        fileName += model.getPackageName() + ".";
    }
    fileName += model.getName();

    JavaFileObject sourceFile;
    try {
        sourceFile = filer.createSourceFile(fileName, originatingElement);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    // 这里最终生成一个 impl 文件
    modelWriter.writeModel(sourceFile, model);
}

```

### 总结

今天带着大家了解了为什么 Mapper Struct 为上面这么快，知根知底以后大家可以在工作中使用起来了，本人在项目中引入收到的反馈非常棒，绝对是一款项目必备的工具。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
