# Java 一行代码如何扫描一个包下面所有的类？

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

这个工具主要是封装了一些反射的方法，使调用更加方便。而这个类中最有用的方法是`scanPackage`
方法，这个方法会扫描classpath下所有类，这个在Spring中是特性之一，主要为[Hulu(opens new window)](https://github.com/looly/hulu)
框架中类扫描的一个基础。下面介绍下这个类中的方法。

## 最佳实践

### 引入pom

```xml

<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <!-- 请查看最新版本 https://mvnrepository.com/artifact/cn.hutool/hutool-all -->
    <version>5.8.26</version>
</dependency>
```

### [#](https://doc.hutool.cn/pages/ClassUtil/#getshortclassname)`getShortClassName`

获取完整类名的短格式如：`cn.hutool.core.util.StrUtil` -> `c.h.c.u.StrUtil`

### [#](https://doc.hutool.cn/pages/ClassUtil/#isallassignablefrom)`isAllAssignableFrom`

比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回true

### [#](https://doc.hutool.cn/pages/ClassUtil/#isprimitivewrapper)`isPrimitiveWrapper`

是否为包装类型

### [#](https://doc.hutool.cn/pages/ClassUtil/#isbasictype)`isBasicType`

是否为基本类型（包括包装类和原始类）

### [#](https://doc.hutool.cn/pages/ClassUtil/#getpackage)`getPackage`

获得给定类所在包的名称，例如： `cn.hutool.util.ClassUtil` -> `cn.hutool.util`

### [#](https://doc.hutool.cn/pages/ClassUtil/#scanpackage%E6%96%B9%E6%B3%95)`scanPackage`方法

此方法唯一的参数是包的名称，返回结果为此包以及子包下所有的类。方法使用很简单，但是过程复杂一些，包扫描首先会调用 `getClassPaths`
方法获得ClassPath，然后扫描ClassPath，如果是目录，扫描目录下的类文件，或者jar文件。如果是jar包，则直接从jar包中获取类名。这个方法的作用显而易见，就是要找出所有的类，在Spring中用于依赖注入，我在[Hulu(opens new window)](https://github.com/looly/hulu)
中则用于找到Action类。当然，你也可以传一个`ClassFilter`对象，用于过滤不需要的类。

### [#](https://doc.hutool.cn/pages/ClassUtil/#getclasspaths%E6%96%B9%E6%B3%95)`getClassPaths`方法

此方法是获得当前线程的ClassPath，核心是`Thread.currentThread().getContextClassLoader().getResources`的调用。

### [#](https://doc.hutool.cn/pages/ClassUtil/#getjavaclasspaths%E6%96%B9%E6%B3%95)`getJavaClassPaths`方法

此方法用于获得java的系统变量定义的ClassPath。

### [#](https://doc.hutool.cn/pages/ClassUtil/#getclassloader%E5%92%8Cgetcontextclassloader%E6%96%B9%E6%B3%95)`getClassLoader`和`getContextClassLoader`方法

后者只是获得当前线程的ClassLoader，前者在获取失败的时候获取`ClassUtil`这个类的ClassLoader。

### [#](https://doc.hutool.cn/pages/ClassUtil/#getdefaultvalue)`getDefaultValue`

获取指定类型的默认值，默认值规则为：

1. 如果为原始类型，返回0（boolean类型返回false）
2. 非原始类型返回null

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)