# Java 中怎么解决 0.1 + 0.2 不等于 0.3？

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

数字工具针对数学运算做工具性封装

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

### [加减乘除](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e5%8a%a0%e5%87%8f%e4%b9%98%e9%99%a4)

- `**NumberUtil.add**` 针对数字类型做加法
- `**NumberUtil.sub**` 针对数字类型做减法
- `**NumberUtil.mul**` 针对数字类型做乘法
- `**NumberUtil.div**` 针对数字类型做除法，并提供重载方法用于规定除不尽的情况下保留小数位数和舍弃方式。

以上四种运算都会将double转为BigDecimal后计算，解决float和double类型无法进行精确计算的问题。这些方法常用于商业计算。

### [保留小数](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e4%bf%9d%e7%95%99%e5%b0%8f%e6%95%b0)

保留小数的方法主要有两种：

- `**NumberUtil.round**` 方法主要封装BigDecimal中的方法来保留小数，返回BigDecimal，这个方法更加灵活，可以选择四舍五入或者全部舍弃等模式。

```java
double te1 = 123456.123456;
double te2 = 123456.128456;
Console.

log(round(te1,4));//结果:123456.1235
    Console.

log(round(te2,4));//结果:123456.1285
```

- `**NumberUtil.roundStr**` 方法主要封装`**String.format**`方法,舍弃方式采用四舍五入。

```java
double te1 = 123456.123456;
double te2 = 123456.128456;
Console.

log(roundStr(te1,2));//结果:123456.12
    Console.

log(roundStr(te2,2));//结果:123456.13
```

### [decimalFormat](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=decimalformat)

针对 `**DecimalFormat.format**`进行简单封装。按照固定格式对double或long类型的数字做格式化操作。

```java
long c = 299792458;//光速
String format = NumberUtil.decimalFormat(",###", c);//299,792,458
```

格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。

- 0 -> 取一位整数
- 0.00 -> 取一位整数和两位小数
- 00.000 -> 取两位整数和三位小数
- # -> 取所有整数部分
- #.##% -> 以百分比方式计数，并取两位小数
- #.#####E0 -> 显示为科学计数法，并取五位小数
- ,### -> 每三位以逗号进行分隔，例如：299,792,458
- 光速大小为每秒,###米 -> 将格式嵌入文本

关于格式的更多说明，请参阅：[Java DecimalFormat的主要功能及使用方法](http://blog.csdn.net/evangel_z/article/details/7624503)

### [是否为数字](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e6%98%af%e5%90%a6%e4%b8%ba%e6%95%b0%e5%ad%97)

- `**NumberUtil.isNumber**` 是否为数字
- `**NumberUtil.isInteger**` 是否为整数
- `**NumberUtil.isDouble**` 是否为浮点数
- `**NumberUtil.isPrimes**` 是否为质数

### [随机数](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e9%9a%8f%e6%9c%ba%e6%95%b0)

- `**NumberUtil.generateRandomNumber**` 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组。
- `**NumberUtil.generateBySet**` 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组。

### [整数列表](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e6%95%b4%e6%95%b0%e5%88%97%e8%a1%a8)

`**NumberUtil.range**` 方法根据范围和步进，生成一个有序整数列表。 `**NumberUtil.appendRange**` 将给定范围内的整数添加到已有集合中

### [其它](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil?id=%e5%85%b6%e5%ae%83)

- `**NumberUtil.factorial**` 阶乘
- `**NumberUtil.sqrt**` 平方根
- `**NumberUtil.divisor**` 最大公约数
- `**NumberUtil.multiple**` 最小公倍数
- `**NumberUtil.getBinaryStr**` 获得数字对应的二进制字符串
- `**NumberUtil.binaryToInt**` 二进制转int
- `**NumberUtil.binaryToLong**` 二进制转long
- `**NumberUtil.compare**` 比较两个值的大小
- `**NumberUtil.toStr**` 数字转字符串，自动并去除尾小数点儿后多余的0

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
