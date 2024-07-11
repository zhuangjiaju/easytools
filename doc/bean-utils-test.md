# Java对象复制系列一: Apache 这么常用的工具类，性能居然如此差？

## 前言

对象复制是编码过程中非常超级常见，为了方便大量字段复制，一般都会使用 Apache 的 BeanUtils 或者 Spring 的 BeanUtils。

但是大家有没有想过，这两个 BeanUtils 类的有啥区别？性能到底好不好？

今天我就带大家来测试下，结果可能会出乎大家意料。

## 最佳实践

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/BeanUtilsTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/BeanUtilsTest.java)

### 对象复制案例

我们先生成N个对象，然后分别使用 Apache BeanUtils、Spring BeanUtils、直接get/set 来复制对象，看看性能如何？

直接上代码：

创建一个java 对象：

```java
/**
 * BeanUtils的案例
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BeanUtilsDemoDTO {

    /**
     * 学生ID
     */
    private String id;
    /**
     * 名
     */
    private String firstName;
    /**
     * 姓
     */
    private String lastName;
    /**
     * 年龄
     */
    private String age;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 地址
     */
    private String address;
    /**
     * 城市
     */
    private String city;
    /**
     * 州
     */
    private String state;
    /**
     * 国家
     */
    private String country;
    /**
     * 专业
     */
    private String major;
    /**
     * 平均成绩点
     */
    private String gpa;
    /**
     * 系
     */
    private String department;
    /**
     * 学年
     */
    private String yearOfStudy;
    /**
     * 导师名字
     */
    private String advisorName;
    /**
     * 入学状态
     */
    private String enrollmentStatus;
    /**
     * 宿舍名
     */
    private String dormitoryName;
    /**
     * 室友名
     */
    private String roommateName;
    /**
     * 奖学金详情
     */
    private String scholarshipDetails;
    /**
     * 课外活动
     */
    private String extracurricularActivities;

}
```

测试代码：

```java
 /**
 * 调用N次对象复制
 * 然后比对 Apache BeanUtils、Spring BeanUtils、直接get/set 的性能
 */
@Test
public void copy() throws Exception {
    // 随机生成1个对象 然后测试
    doDemo(randomList(1));
    // 随机生成100个对象 然后测试
    doDemo(randomList(100));
    // 随机生成1000个对象 然后测试
    doDemo(randomList(1000));
    // 随机生成10000个对象 然后测试
    doDemo(randomList(10000));
    // 随机生成100000个对象 然后测试
    doDemo(randomList(100000));
}

/**
 * 调用对象复制
 *
 * @param randomList
 * @return
 * @throws Exception
 */
private void doDemo(List<BeanUtilsDemoDTO> randomList) throws Exception {
    log.info("调用{}次复制", randomList.size());

    // 计时器
    TimeInterval interval = new TimeInterval();

    // Apache BeanUtils
    log.info("Apache BeanUtils 开始处理");
    for (BeanUtilsDemoDTO beanUtilsDemo : randomList) {
        BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
        org.apache.commons.beanutils.BeanUtils.copyProperties(newBeanUtilsDemo, beanUtilsDemo);
    }
    log.info("Apache BeanUtils 结束处理处理，耗时：{}ms", interval.intervalRestart());

    // Spring BeanUtils
    log.info("Spring BeanUtils 开始处理");
    for (BeanUtilsDemoDTO beanUtilsDemo : randomList) {
        BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
        org.springframework.beans.BeanUtils.copyProperties(newBeanUtilsDemo, beanUtilsDemo);
    }
    log.info("Spring BeanUtils 结束处理处理，耗时：{}ms", interval.intervalRestart());

    // 直接get/set
    log.info("直接get/sett 开始处理");
    for (BeanUtilsDemoDTO beanUtilsDemo : randomList) {
        BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
        newBeanUtilsDemo.setId(beanUtilsDemo.getId());
        newBeanUtilsDemo.setFirstName(beanUtilsDemo.getFirstName());
        newBeanUtilsDemo.setLastName(beanUtilsDemo.getLastName());
        newBeanUtilsDemo.setAge(beanUtilsDemo.getAge());
        newBeanUtilsDemo.setEmail(beanUtilsDemo.getEmail());
        newBeanUtilsDemo.setPhoneNumber(beanUtilsDemo.getPhoneNumber());
        newBeanUtilsDemo.setAddress(beanUtilsDemo.getAddress());
        newBeanUtilsDemo.setCity(beanUtilsDemo.getCity());
        newBeanUtilsDemo.setState(beanUtilsDemo.getState());
        newBeanUtilsDemo.setCountry(beanUtilsDemo.getCountry());
        newBeanUtilsDemo.setMajor(beanUtilsDemo.getMajor());
        newBeanUtilsDemo.setGpa(beanUtilsDemo.getGpa());
        newBeanUtilsDemo.setDepartment(beanUtilsDemo.getDepartment());
        newBeanUtilsDemo.setYearOfStudy(beanUtilsDemo.getYearOfStudy());
        newBeanUtilsDemo.setAdvisorName(beanUtilsDemo.getAdvisorName());
        newBeanUtilsDemo.setEnrollmentStatus(beanUtilsDemo.getEnrollmentStatus());
        newBeanUtilsDemo.setDormitoryName(beanUtilsDemo.getDormitoryName());
        newBeanUtilsDemo.setRoommateName(beanUtilsDemo.getRoommateName());
        newBeanUtilsDemo.setScholarshipDetails(beanUtilsDemo.getScholarshipDetails());
        newBeanUtilsDemo.setExtracurricularActivities(beanUtilsDemo.getExtracurricularActivities());
    }
    log.info("直接get/set 结束处理处理，耗时：{}ms", interval.intervalRestart());
}

/**
 * 随机生成对象
 *
 * @param count
 * @return
 */
private List<BeanUtilsDemoDTO> randomList(int count) {
    List<BeanUtilsDemoDTO> randomList = Lists.newArrayListWithExpectedSize(count);
    for (int i = 0; i < count; i++) {
        BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
        BeanMap beanMap = BeanMap.create(beanUtilsDemo);
        // 所有属性放入10个随机字符串
        for (Object key : beanMap.keySet()) {
            beanMap.put(key, RandomUtil.randomString(10));
        }
        randomList.add(beanUtilsDemo);
    }
    return randomList;
}
```

输出结果:

```xml
        21:05:38.375 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 调用1次复制
        21:05:38.380 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 开始处理
        21:05:38.409 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 结束处理处理，耗时：28ms
        21:05:38.409 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 开始处理
        21:05:38.449 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 结束处理处理，耗时：41ms
        21:05:38.449 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/sett 开始处理
        21:05:38.449 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/set 结束处理处理，耗时：0ms
        21:05:38.452 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 调用100次复制
        21:05:38.452 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 开始处理
        21:05:38.470 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 结束处理处理，耗时：18ms
        21:05:38.470 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 开始处理
        21:05:38.482 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 结束处理处理，耗时：12ms
        21:05:38.482 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/sett 开始处理
        21:05:38.482 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/set 结束处理处理，耗时：0ms
        21:05:38.492 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 调用1000次复制
        21:05:38.492 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 开始处理
        21:05:38.539 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 结束处理处理，耗时：47ms
        21:05:38.539 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 开始处理
        21:05:38.562 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 结束处理处理，耗时：23ms
        21:05:38.562 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/sett 开始处理
        21:05:38.563 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/set 结束处理处理，耗时：1ms
        21:05:38.599 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 调用10000次复制
        21:05:38.600 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 开始处理
        21:05:38.842 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 结束处理处理，耗时：242ms
        21:05:38.842 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 开始处理
        21:05:38.855 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 结束处理处理，耗时：13ms
        21:05:38.855 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/sett 开始处理
        21:05:38.862 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/set 结束处理处理，耗时：7ms
        21:05:39.052 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 调用100000次复制
        21:05:39.052 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 开始处理
        21:05:40.974 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Apache BeanUtils 结束处理处理，耗时：1922ms
        21:05:40.974 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 开始处理
        21:05:41.035 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- Spring BeanUtils 结束处理处理，耗时：61ms
        21:05:41.035 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/sett 开始处理
        21:05:41.045 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsTest -- 直接get/set 结束处理处理，耗时：10ms
```

### 结果对比

|| 1次 | 100次 |1000次 |10000次 |100000次 |
|---|---|---|---|---|---|
|Apache BeanUtils|28ms|18ms|47ms|242ms|1922ms|
|Spring BeanUtils|41ms|12ms|23ms|13ms|61ms|
|直接get/set|0ms|0ms|1ms|7ms|10ms|

结果很明显 直接get/set 性能是最优的。
Spring BeanUtils 第1次很慢，但是100次反而1次性能能好，是不是很神奇？
Apache BeanUtils 第1次很慢，后续也增幅很大，在10万次时候，到达了2秒多，这个超出预期。

### 总结

所以大家在工作中尽量使用 Spring BeanUtils 来替换 Apache BeanUtils，这个替换成本非常低，但是效果收货很不错。

为啥 Spring BeanUtils 比 Apache BeanUtils 性能好？为啥 Spring BeanUtils 第1次很慢，但是100次反而1次性能能好？

这个请看我后面的文章，我们来一个个深入了解。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)