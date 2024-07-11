# java 如何一行代码生成随机数据？

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

RandomUtil可以随机生成N个字符串、数字等数据，在做测试的时候非常方便。一行代码搞定。

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

### 使用

- RandomUtil.randomInt 获得指定范围内的随机数

例如我们想产生一个[10, 100)的随机数，则：

```
int c = RandomUtil.randomInt(10, 100);
```

- RandomUtil.randomBytes 随机bytes，一般用于密码或者salt生成

```
byte[] c = RandomUtil.randomBytes(10);
```

- RandomUtil.randomEle 随机获得列表中的元素
- RandomUtil.randomEleSet 随机获得列表中的一定量的不重复元素，返回LinkedHashSet

```
Set<Integer> set = RandomUtil.randomEleSet(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
```

- RandomUtil.randomString 获得一个随机的字符串（只包含数字和字符）
- RandomUtil.randomNumbers 获得一个只包含数字的字符串
- RandomUtil.weightRandom 权重随机生成器，传入带权重的对象，然后根据权重随机获取对象

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)