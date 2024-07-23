# Java 如何获取一个 class 的所有属性和方法

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

Java的反射机制，可以让语言变得更加灵活，对对象的操作也更加“动态”，因此在某些情况下，反射可以做到事半功倍的效果。Hutool针对Java的反射机制做了工具化封装，封装包括：
1. 获取构造方法
2. 获取字段
3. 获取字段值
4. 获取方法
5. 执行方法（对象方法和静态方法）

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

## [使用](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8F%8D%E5%B0%84%E5%B7%A5%E5%85%B7-ReflectUtil?id=%e4%bd%bf%e7%94%a8)
### [获取某个类的所有方法](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8F%8D%E5%B0%84%E5%B7%A5%E5%85%B7-ReflectUtil?id=%e8%8e%b7%e5%8f%96%e6%9f%90%e4%b8%aa%e7%b1%bb%e7%9a%84%e6%89%80%e6%9c%89%e6%96%b9%e6%b3%95)
```java
Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
```
### [获取某个类的指定方法](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8F%8D%E5%B0%84%E5%B7%A5%E5%85%B7-ReflectUtil?id=%e8%8e%b7%e5%8f%96%e6%9f%90%e4%b8%aa%e7%b1%bb%e7%9a%84%e6%8c%87%e5%ae%9a%e6%96%b9%e6%b3%95)
```java
Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
```
### [构造对象](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8F%8D%E5%B0%84%E5%B7%A5%E5%85%B7-ReflectUtil?id=%e6%9e%84%e9%80%a0%e5%af%b9%e8%b1%a1)
```java
ReflectUtil.newInstance(ExamInfoDict.class);
```
### [执行方法](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8F%8D%E5%B0%84%E5%B7%A5%E5%85%B7-ReflectUtil?id=%e6%89%a7%e8%a1%8c%e6%96%b9%e6%b3%95)
```java
class TestClass {
    private int a;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
```
```java
TestClass testClass = new TestClass();
ReflectUtil.invoke(testClass, "setA", 10);
```




## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
