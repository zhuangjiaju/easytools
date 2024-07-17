# Java 中最全的 hash 算法集合

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

HashUtil其实是一个hash算法的集合，此工具类中融合了各种hash算法。

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

## [方法](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/Hash%E7%AE%97%E6%B3%95-HashUtil?id=%e6%96%b9%e6%b3%95)

这些算法包括：

1. `**additiveHash**` 加法hash
2. `**rotatingHash**` 旋转hash
3. `**oneByOneHash**` 一次一个hash
4. `**bernstein**` Bernstein's hash
5. `**universal**` Universal Hashing
6. `**zobrist**` Zobrist Hashing
7. `**fnvHash**` 改进的32位FNV算法1
8. `**intHash**` Thomas Wang的算法，整数hash
9. `**rsHash**` RS算法hash
10. `**jsHash**` JS算法
11. `**pjwHash**` PJW算法
12. `**elfHash**` ELF算法
13. `**bkdrHash**` BKDR算法
14. `**sdbmHash**` SDBM算法
15. `**djbHash**` DJB算法
16. `**dekHash**` DEK算法
17. `**apHash**` AP算法
18. `**tianlHash**` TianL Hash算法
19. `**javaDefaultHash**` JAVA自己带的算法
20. `**mixHash**` 混合hash算法，输出64位的值

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)