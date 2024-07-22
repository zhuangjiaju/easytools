# Java 中如何快速读取 CSV 文件

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

逗号分隔值（Comma-Separated Values，CSV，有时也称为字符分隔值，因为分隔字符也可以不是逗号），其文件以纯文本形式存储表格数据（数字和文本）。

Hutool针对此格式，参考FastCSV项目做了对CSV文件读写的实现(Hutool实现完全独立，不依赖第三方)

CsvUtil是CSV工具类，主要封装了两个方法：

getReader 用于对CSV文件读取
getWriter 用于生成CSV文件
这两个方法分别获取CsvReader对象和CsvWriter，从而独立完成CSV文件的读写。

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

## [使用](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e4%bd%bf%e7%94%a8)
### [读取CSV文件](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e8%af%bb%e5%8f%96csv%e6%96%87%e4%bb%b6)
#### [读取为CsvRow](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e8%af%bb%e5%8f%96%e4%b8%bacsvrow)
```java
CsvReader reader = CsvUtil.getReader();
//从文件中读取CSV数据
CsvData data = reader.read(FileUtil.file("test.csv"));
List<CsvRow> rows = data.getRows();
//遍历行
for (CsvRow csvRow : rows) {
    //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
    Console.log(csvRow.getRawList());
}
```
`**CsvRow**`对象还记录了一些其他信息，包括原始行号等。
#### [读取为Bean列表](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e8%af%bb%e5%8f%96%e4%b8%babean%e5%88%97%e8%a1%a8)
首先测试的CSV：`**test_bean.csv**`:
```
姓名,gender,focus,age
张三,男,无,33
李四,男,好对象,23
王妹妹,女,特别关注,22
```

1. 定义Bean：
```java
// lombok注解
@Data
private static class TestBean{
    // 如果csv中标题与字段不对应，可以使用alias注解设置别名
    @Alias("姓名")
    private String name;
    private String gender;
    private String focus;
    private Integer age;
}
```

1. 读取
```java
final CsvReader reader = CsvUtil.getReader();
//假设csv文件在classpath目录下
final List<TestBean> result = reader.read(
                ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);
```

1. 输出：
```
CsvReaderTest.TestBean(name=张三, gender=男, focus=无, age=33)
CsvReaderTest.TestBean(name=李四, gender=男, focus=好对象, age=23)
CsvReaderTest.TestBean(name=王妹妹, gender=女, focus=特别关注, age=22)
```
### [生成CSV文件](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e7%94%9f%e6%88%90csv%e6%96%87%e4%bb%b6)
```java
//指定路径和编码
CsvWriter writer = CsvUtil.getWriter("e:/testWrite.csv", CharsetUtil.CHARSET_UTF_8);
//按行写出
writer.write(
    new String[] {"a1", "b1", "c1"}, 
    new String[] {"a2", "b2", "c2"}, 
    new String[] {"a3", "b3", "c3"}
);
```
效果如下： ![](https://cdn.nlark.com/yuque/0/2024/png/553000/1721652020133-44dd1022-222a-4b1d-b356-f78975a6b8c5.png#averageHue=%23efeeee&clientId=u19cca220-584b-4&from=paste&id=u6f146c0d&originHeight=119&originWidth=353&originalType=url&ratio=1&rotation=0&showTitle=false&status=done&style=none&taskId=u07612c7b-37d3-4143-be5b-4bdd339a468&title=)
### [乱码问题](https://www.hutool.cn/docs/#/core/%E6%96%87%E6%9C%AC%E6%93%8D%E4%BD%9C/CSV%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86%E5%B7%A5%E5%85%B7-CsvUtil?id=%e4%b9%b1%e7%a0%81%e9%97%ae%e9%a2%98)
CSV文件本身为一种简单文本格式，有编码区分，你可以使用任意编码。
但是当使用Excel读取CSV文件时，如果你的编码与系统编码不一致，会出现乱码的情况，解决方案如下：

1. 可以将csv文本编码设置为与系统一致，如Windows下可以设置GBK
2. 可以增加BOM头来指定编码，这样Excel可以自动识别bom头的编码完成解析。


## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
