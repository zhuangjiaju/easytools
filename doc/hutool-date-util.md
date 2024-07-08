# 还在代码中写DateUtil?是时候说再见啦

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

相对于我们自己项目中写的 DateUtil ,Hutool 的 DateUtil 会强大非常多，也有更多的人帮忙测试，相对于 apache 的 DateUtil 来说 Hutool 的 API 会多非常多，也更符合国人习惯，自从用了 Hutool 之后，我项目中自己再也没写过 DateUtil 了。

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

### 转换

#### [#](https://doc.hutool.cn/pages/DateUtil/#date%E3%80%81long%E3%80%81calendar%E4%B9%8B%E9%97%B4%E7%9A%84%E7%9B%B8%E4%BA%92%E8%BD%AC%E6%8D%A2)Date、long、Calendar之间的相互转换

```java
//当前时间
Date date = DateUtil.date();
//当前时间
Date date2 = DateUtil.date(Calendar.getInstance());
//当前时间
Date date3 = DateUtil.date(System.currentTimeMillis());
//当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
String now = DateUtil.now();
//当前日期字符串，格式：yyyy-MM-dd
String today= DateUtil.today();
```

#### [#](https://doc.hutool.cn/pages/DateUtil/#%E5%AD%97%E7%AC%A6%E4%B8%B2%E8%BD%AC%E6%97%A5%E6%9C%9F)字符串转日期

DateUtil.parse方法会自动识别一些常用格式，包括：

- yyyy-MM-dd HH:mm:ss
- yyyy/MM/dd HH:mm:ss
- yyyy.MM.dd HH:mm:ss
- yyyy年MM月dd日 HH时mm分ss秒
- yyyy-MM-dd
- yyyy/MM/dd
- yyyy.MM.dd
- HH:mm:ss
- HH时mm分ss秒
- yyyy-MM-dd HH:mm
- yyyy-MM-dd HH:mm:ss.SSS
- yyyyMMddHHmmss
- yyyyMMddHHmmssSSS
- yyyyMMdd
- EEE, dd MMM yyyy HH:mm:ss z
- EEE MMM dd HH:mm:ss zzz yyyy
- yyyy-MM-dd'T'HH:mm:ss'Z'
- yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
- yyyy-MM-dd'T'HH:mm:ssZ
- yyyy-MM-dd'T'HH:mm:ss.SSSZ

```java
String dateStr = "2017-03-01";
Date date = DateUtil.parse(dateStr);
```

我们也可以使用自定义日期格式转化：

```java
String dateStr = "2017-03-01";
Date date = DateUtil.parse(dateStr, "yyyy-MM-dd");
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%A0%BC%E5%BC%8F%E5%8C%96%E6%97%A5%E6%9C%9F%E8%BE%93%E5%87%BA)格式化日期输出

```java
String dateStr = "2017-03-01";
Date date = DateUtil.parse(dateStr);

//结果 2017/03/01
String format = DateUtil.format(date, "yyyy/MM/dd");

//常用格式的格式化，结果：2017-03-01
String formatDate = DateUtil.formatDate(date);

//结果：2017-03-01 00:00:00
String formatDateTime = DateUtil.formatDateTime(date);

//结果：00:00:00
String formatTime = DateUtil.formatTime(date);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E8%8E%B7%E5%8F%96date%E5%AF%B9%E8%B1%A1%E7%9A%84%E6%9F%90%E4%B8%AA%E9%83%A8%E5%88%86)获取Date对象的某个部分

```java
Date date = DateUtil.date();
//获得年的部分
DateUtil.year(date);
//获得月份，从0开始计数
DateUtil.month(date);
//获得月份枚举
DateUtil.monthEnum(date);
//.....
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E5%BC%80%E5%A7%8B%E5%92%8C%E7%BB%93%E6%9D%9F%E6%97%B6%E9%97%B4)开始和结束时间

有的时候我们需要获得每天的开始时间、结束时间，每月的开始和结束时间等等，DateUtil也提供了相关方法：

```java
String dateStr = "2017-03-01 22:33:23";
Date date = DateUtil.parse(dateStr);

//一天的开始，结果：2017-03-01 00:00:00
Date beginOfDay = DateUtil.beginOfDay(date);

//一天的结束，结果：2017-03-01 23:59:59
Date endOfDay = DateUtil.endOfDay(date);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4%E5%81%8F%E7%A7%BB)日期时间偏移

日期或时间的偏移指针对某个日期增加或减少分、小时、天等等，达到日期变更的目的。Hutool也针对其做了大量封装

```java
String dateStr = "2017-03-01 22:33:23";
Date date = DateUtil.parse(dateStr);

//结果：2017-03-03 22:33:23
Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 2);

//常用偏移，结果：2017-03-04 22:33:23
DateTime newDate2 = DateUtil.offsetDay(date, 3);

//常用偏移，结果：2017-03-01 19:33:23
DateTime newDate3 = DateUtil.offsetHour(date, -3);
```

针对当前时间，提供了简化的偏移方法（例如昨天、上周、上个月等）：

```java
//昨天
DateUtil.yesterday()
//明天
DateUtil.tomorrow()
//上周
DateUtil.lastWeek()
//下周
DateUtil.nextWeek()
//上个月
DateUtil.lastMonth()
//下个月
DateUtil.nextMonth()
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4%E5%B7%AE)日期时间差

有时候我们需要计算两个日期之间的时间差（相差天数、相差小时数等等），Hutool将此类方法封装为between方法：

```java
String dateStr1 = "2017-03-01 22:33:23";
Date date1 = DateUtil.parse(dateStr1);

String dateStr2 = "2017-04-01 23:33:23";
Date date2 = DateUtil.parse(dateStr2);

//相差一个月，31天
long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%A0%BC%E5%BC%8F%E5%8C%96%E6%97%B6%E9%97%B4%E5%B7%AE)格式化时间差

有时候我们希望看到易读的时间差，比如XX天XX小时XX分XX秒，此时使用DateUtil.formatBetween方法：

```java
//Level.MINUTE表示精确到分
String formatBetween = DateUtil.formatBetween(between, Level.MINUTE);
//输出：31天1小时
Console.log(formatBetween);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%98%9F%E5%BA%A7%E5%92%8C%E5%B1%9E%E7%9B%B8)星座和属相

```java
// "摩羯座"
String zodiac = DateUtil.getZodiac(Month.JANUARY.getValue(), 19);

// "狗"
String chineseZodiac = DateUtil.getChineseZodiac(1994);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E6%97%A5%E6%9C%9F%E8%8C%83%E5%9B%B4)日期范围

```java
// 创建日期范围生成器
DateTime start = DateUtil.parse("2021-01-31");
DateTime end = DateUtil.parse("2021-03-31");
DateRange range = DateUtil.range(start, end, DateField.MONTH);

// 简单使用
// 开始时间
DateRange startRange = DateUtil.range(DateUtil.parse("2017-01-01"), DateUtil.parse("2017-01-31"), DateField.DAY_OF_YEAR);
// 结束时间
DateRange endRange = DateUtil.range(DateUtil.parse("2017-01-31"), DateUtil.parse("2017-02-02"), DateField.DAY_OF_YEAR);
// 交集 返回 [2017-01-31 00:00:00]
List<DateTime> dateTimes = DateUtil.rangeContains(startRange, endRange);
// 差集 返回 [2017-02-01 00:00:00, 2017-02-02 00:00:00]
List<DateTime> dateNotTimes = DateUtil.rangeNotContains(startRange,endRange);
// 区间 返回[2017-01-01 00:00:00, 2017-01-02 00:00:00, 2017-01-03 00:00:00]
List<DateTime> rangeToList = DateUtil.rangeToList(DateUtil.parse("2017-01-01"), DateUtil.parse("2017-01-03"), DateField.DAY_OF_YEAR);
```

### [#](https://doc.hutool.cn/pages/DateUtil/#%E5%85%B6%E5%AE%83)其它

```java
//年龄
DateUtil.ageOfNow("1990-01-30");

//是否闰年
DateUtil.isLeapYear(2017);
```

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)