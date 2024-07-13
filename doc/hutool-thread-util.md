# Java 如何不用再每次新建线程，直接使用公共线程池

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

并发在Java中算是一个比较难理解和容易出问题的部分，而并发的核心在线程。好在从JDK1.5开始Java提供了concurrent包可以很好的帮我们处理大部分并发、异步等问题。

不过，ExecutorService和Executors等众多概念依旧让我们使用这个包变得比较麻烦，如何才能隐藏这些概念？又如何用一个方法解决问题？ThreadUtil便为此而生。

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

### [ThreadUtil.execute](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilexecute)

直接在公共线程池中执行线程

### [ThreadUtil.newExecutor](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilnewexecutor)

获得一个新的线程池

### [ThreadUtil.execAsync](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilexecasync)

执行异步方法

### [ThreadUtil.newCompletionService](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilnewcompletionservice)

创建CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。若未完成，则会阻塞。

### [ThreadUtil.newCountDownLatch](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilnewcountdownlatch)

新建一个CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。

### [ThreadUtil.sleep](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilsleep)

挂起当前线程，是`**Thread.sleep**`的封装，通过返回boolean值表示是否被打断，而不是抛出异常。
`**ThreadUtil.safeSleep**`**方法是一个保证挂起足够时间的方法，当给定一个挂起时间，使用此方法可以保证挂起的时间大于或等于给定时间，解决
**`**Thread.sleep**`**挂起时间不足问题，此方法在Hutool-cron的定时器中使用保证定时任务执行的准确性。**

### [ThreadUtil.getStackTrace](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=threadutilgetstacktrace)

此部分包括两个方法：

- `**getStackTrace**` 获得堆栈列表
- `**getStackTraceElement**` 获得堆栈项

### [其它](https://www.hutool.cn/docs/#/core/%E7%BA%BF%E7%A8%8B%E5%92%8C%E5%B9%B6%E5%8F%91/%E7%BA%BF%E7%A8%8B%E5%B7%A5%E5%85%B7-ThreadUtil?id=%e5%85%b6%e5%ae%83)

- `**createThreadLocal**` 创建本地线程对象
- `**interupt**` 结束线程，调用此方法后，线程将抛出InterruptedException异常
- `**waitForDie**` 等待线程结束. 调用 `**Thread.join()**` 并忽略 InterruptedException
- `**getThreads**` 获取JVM中与当前线程同组的所有线程
- `**getMainThread**` 获取进程的主线程

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)