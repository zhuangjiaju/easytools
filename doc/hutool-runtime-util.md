# Java 中如何执行命令行方法

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

在Java世界中，如果想与其它语言打交道，处理调用接口，或者JNI，就是通过本地命令方式调用了。Hutool封装了JDK的Process类，用于执行命令行命令（在Windows下是cmd，在Linux下是shell命令）。

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

## [#](https://doc.hutool.cn/pages/RuntimeUtil/#%E6%96%B9%E6%B3%95)方法
### [#](https://doc.hutool.cn/pages/RuntimeUtil/#%E5%9F%BA%E7%A1%80%E6%96%B9%E6%B3%95)基础方法

1. `exec` 执行命令行命令，返回Process对象，Process可以读取执行命令后的返回内容的流
### [#](https://doc.hutool.cn/pages/RuntimeUtil/#%E5%BF%AB%E6%8D%B7%E6%96%B9%E6%B3%95)快捷方法

1. `execForStr` 执行系统命令，返回字符串
2. `execForLines` 执行系统命令，返回行列表
## [#](https://doc.hutool.cn/pages/RuntimeUtil/#%E4%BD%BF%E7%94%A8)使用
```
String str = RuntimeUtil.execForStr("ipconfig");
```
执行这个命令后，在Windows下可以获取网卡信息。


## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
