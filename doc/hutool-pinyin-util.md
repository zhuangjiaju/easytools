# Java 中如何获取一个中文的拼音

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

拼音工具类在旧版本的Hutool中在core包中，但是发现自己实现相关功能需要庞大的字典，放在core包中便是累赘。
于是为了方便，Hutool封装了拼音的门面，用于兼容以下拼音库:

1. TinyPinyin
2. JPinyin
3. Pinyin4j
   和其它门面模块类似，采用SPI方式识别所用的库。例如你想用Pinyin4j，只需引入jar，Hutool即可自动识别。

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

### [使用](https://www.hutool.cn/docs/#/extra/%E6%8B%BC%E9%9F%B3/%E6%8B%BC%E9%9F%B3%E5%B7%A5%E5%85%B7-PinyinUtil?id=%e4%bd%bf%e7%94%a8-1)

1. 获取拼音

```java
// "ni hao"
String pinyin = PinyinUtil.getPinyin("你好", " ");
```

这里定义的分隔符为空格，你也可以按照需求自定义分隔符，亦或者使用""无分隔符。

1. 获取拼音首字母

```java
// "h, s, d, y, g"
String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
```

1. 自定义拼音库（拼音引擎）

```java
Pinyin4jEngine engine = new Pinyin4jEngine();

// "ni hao h"
String pinyin = engine.getPinyin("你好h", " ");
```

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
