# Java对象复制系列三: 手把手带你写一个 Spring BeanUtils

## 前言

上一节我带大家手写了一个 Apache BeanUtils，并阅读了源码，大家应该都自己去阅读过源码了吧？

今天我大家手写一个 Spring BeanUtils，不像 Apache BeanUtils 我们就写了一个简单的案例，这次我们要写一个完整的 Spring BeanUtils 并带上和 Apache BeanUtils ,看看我们自己写的和 Apache BeanUtils 性能会有啥差别？

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/SpringBeanUtilsTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/SpringBeanUtilsTest.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/SpringBeanUtilsTest.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/beanutils/SpringBeanUtilsTest.java)

需要上一期的教程自己去主页翻。

### 简单的复制对象

直接上代码：

```java
/**
 * Spring BeanUtils 使用的demo
 */
@Test
public void demo() throws Exception {
    BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
    beanUtilsDemo.setId("id");
    beanUtilsDemo.setFirstName("firstName");

    BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
    //这里注意 spring 和 apache 的参数位置不一样 spring 是先 source 对象 后 target 对象
    BeanUtils.copyProperties(beanUtilsDemo, newBeanUtilsDemo);
    log.info("newBeanUtilsDemo: {}", newBeanUtilsDemo);
}

```

输出结果:

```text
19:47:02.946 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.SpringBeanUtilsTest -- newBeanUtilsDemo: BeanUtilsDemoDTO(id=id, firstName=firstName, lastName=null, age=null, email=null, phoneNumber=null, address=null, city=null, state=null, country=null, major=null, gpa=null, department=null, yearOfStudy=null, advisorName=null, enrollmentStatus=null, dormitoryName=null, roommateName=null, scholarshipDetails=null, extracurricularActivities=null)
```

可见已经复制对象成功了，输出里面有 firstName 的值。这里要注意 spring 和 apache 的参数位置不一样 spring 是先 source 对象 ，后 target 对象。

### 自己写一个完整的 Spring BeanUtils

Spring BeanUtils 源码相对比较简单，我们这里直接实现全部逻辑了，自己实现一遍在去看源码就会感觉怎么这么简单。

```java
/**
 * 自己写一个完整的 Spring BeanUtils
 */
@Test
public void custom() throws Exception {
    BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
    beanUtilsDemo.setId("id");
    beanUtilsDemo.setFirstName("firstName");

    BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
    MySpringBeanUtils.copyProperties(beanUtilsDemo, newBeanUtilsDemo);
    log.info("newBeanUtilsDemo: {}", newBeanUtilsDemo);
}
```

我们自己实现的工具类：

前置知识：

Introspector.getBeanInfo： 是 Java 自带的一个类，可以获取一个类的 BeanInfo 信息，然后获取属性的描述资料 PropertyDescriptor
BeanInfo ： bean 的描述信息
PropertyDescriptor： bean 的属性的资料信息 ，可以获取到属性的 get/set 方法
Method： 方法,用这个对象可以反射掉调用

```java
    public static class MySpringBeanUtils {

    private static final ConcurrentMap<Class<?>, DescriptorCache> STRONG_CLASS_CACHE
        = new ConcurrentHashMap<>();

    /**
     * 复制参数
     *
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyProperties(Object source, Object target) throws Exception {

        // 去缓存 获取目标对象的 PropertyDescriptor 属性资料
        DescriptorCache targetDescriptorCache = getPropertyDescriptorFormClass(target.getClass());
        // 去缓存 获取来源对象的 PropertyDescriptor 属性资料
        DescriptorCache sourceDescriptorCache = getPropertyDescriptorFormClass(source.getClass());

        // 循环目标对象
        for (PropertyDescriptor targetPropertyDescriptor : targetDescriptorCache.getPropertyDescriptors()) {
            // 获取属性名
            String name = targetPropertyDescriptor.getName();
            // 去Map 直接获取来源对象的属性资料 不用循环获取
            PropertyDescriptor sourcePropertyDescriptor = sourceDescriptorCache.getPropertyDescriptorMap().get(
                name);
            // 反射直接调用方法
            targetPropertyDescriptor.getWriteMethod().invoke(target,
                sourcePropertyDescriptor.getReadMethod().invoke(source));
        }
    }

    /**
     * 我们在这里加一个缓存 这样不用每次都去解析 PropertyDescriptor
     * @param beanClass
     * @return
     * @throws Exception
     */
    private static DescriptorCache getPropertyDescriptorFormClass(Class<?> beanClass) throws Exception {
        return STRONG_CLASS_CACHE.computeIfAbsent(beanClass, clazz -> {
            try {
                // 解析 PropertyDescriptor
                PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(beanClass,
                    Object.class).getPropertyDescriptors();
                // 转换成name的map 时间复杂度由 O(n) -> O(1) 也是用于加速
                Map<String, PropertyDescriptor> propertyDescriptorMap = Arrays.stream(propertyDescriptors)
                    .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity(),
                        (oldValue, newValue) -> newValue));
                return DescriptorCache.builder()
                    .propertyDescriptors(propertyDescriptors)
                    .propertyDescriptorMap(propertyDescriptorMap)
                    .build();
            } catch (IntrospectionException ignore) {

            }
            return null;
        });
    }

}


```

缓存对象

```java

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public static class DescriptorCache {
    /**
     * 存储所有的 PropertyDescriptor 属性资料
     */
    private PropertyDescriptor[] propertyDescriptors;

    /**
     * 存储对应方法的  PropertyDescriptor 属性资料 用于加速
     */
    private Map<String, PropertyDescriptor> propertyDescriptorMap;

}
```

这个和上一期的 Apache BeanUtils 我们加入了缓存，速度有了质的飞跃。

其实Spring BeanUtils 实现基本我们实现的一模一样，特别的容易。

### 性能测试

性能测试的代码 我就不贴了，大家有兴趣的 可以直接看案例代码。

|                  | 1次   | 100次 | 1000次 | 10000次 | 100000次 |
|------------------|------|------|-------|--------|---------|
| Apache BeanUtils | 33ms | 17ms | 52ms  | 264ms  | 1964ms  |
| 自己写的BeanUtils    | 5ms  | 12ms | 18ms  | 13ms   | 116ms   |
| 直接get/set        | 0ms  | 1ms  | 1ms   | 1ms    | 13ms    |

发现了没有 我们自己几行代码就实现了一个性能秒杀 Apache BeanUtils 的 BeanUtils。

很多源码没有大家想象的那么复杂，只要多去看看就行。

### 源码解析

org.apache.commons.beanutils.BeanUtils.copyProperties
org.springframework.beans.BeanUtils.copyProperties(java.lang.Object, java.lang.Object, java.lang.Class<?>,
java.lang.String...)

```java
private static void copyProperties(Object source, Object target, @Nullable Class<?> editable,
    @Nullable String... ignoreProperties) throws BeansException {

    Assert.notNull(source, "Source must not be null");
    Assert.notNull(target, "Target must not be null");

    Class<?> actualEditable = target.getClass();
    if (editable != null) {
        if (!editable.isInstance(target)) {
            throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                "] not assignable to editable class [" + editable.getName() + "]");
        }
        actualEditable = editable;
    }
    // 这里去缓存里面获取 PropertyDescriptor
    PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
    Set<String> ignoredProps = (ignoreProperties != null ? new HashSet<>(Arrays.asList(ignoreProperties)) : null);
    CachedIntrospectionResults sourceResults = (actualEditable != source.getClass() ?
        CachedIntrospectionResults.forClass(source.getClass()) : null);

    // 循环目标的 PropertyDescriptor
    for (PropertyDescriptor targetPd : targetPds) {
        Method writeMethod = targetPd.getWriteMethod();
        if (writeMethod != null && (ignoredProps == null || !ignoredProps.contains(targetPd.getName()))) {
            PropertyDescriptor sourcePd = (sourceResults != null ?
                sourceResults.getPropertyDescriptor(targetPd.getName()) : targetPd);
            if (sourcePd != null) {
                Method readMethod = sourcePd.getReadMethod();
                if (readMethod != null) {
                    if (isAssignable(writeMethod, readMethod, sourcePd, targetPd)) {
                        try {
                            ReflectionUtils.makeAccessible(readMethod);
                            // 读取到source 中的值
                            Object value = readMethod.invoke(source);
                            ReflectionUtils.makeAccessible(writeMethod);
                            // 反射写到 target
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}
```

org.springframework.beans.BeanUtils.getPropertyDescriptors
org.springframework.beans.CachedIntrospectionResults.forClass

```java
static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
    // 判断缓存中是否存在 存在则返回
    CachedIntrospectionResults results = strongClassCache.get(beanClass);
    if (results != null) {
        return results;
    }
    results = softClassCache.get(beanClass);
    if (results != null) {
        return results;
    }

    // 封装缓存的对象 核心代码在 CachedIntrospectionResults 的构造方法
    results = new CachedIntrospectionResults(beanClass);
    ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;

    if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) ||
        isClassLoaderAccepted(beanClass.getClassLoader())) {
        classCacheToUse = strongClassCache;
    } else {
        if (logger.isDebugEnabled()) {
            logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
        }
        classCacheToUse = softClassCache;
    }

    CachedIntrospectionResults existing = classCacheToUse.putIfAbsent(beanClass, results);
    return (existing != null ? existing : results);
}

```

org.springframework.beans.CachedIntrospectionResults.CachedIntrospectionResults

```java
    private CachedIntrospectionResults(Class<?> beanClass) throws BeansException {
    try {
        if (logger.isTraceEnabled()) {
            logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
        }
        // 获取bean 的信息  最终调用了：Introspector.getBeanInfo
        this.beanInfo = getBeanInfo(beanClass);

        if (logger.isTraceEnabled()) {
            logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
        }
        this.propertyDescriptors = new LinkedHashMap<>();

        Set<String> readMethodNames = new HashSet<>();

        // This call is slow so we do it once.
        // 获取所有的 PropertyDescriptor
        PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            // 忽略各种不需要的属性
            if (Class.class == beanClass && !("name".equals(pd.getName()) ||
                (pd.getName().endsWith("Name") && String.class == pd.getPropertyType()))) {
                // Only allow all name variants of Class properties
                continue;
            }
            if (URL.class == beanClass && "content".equals(pd.getName())) {
                // Only allow URL attribute introspection, not content resolution
                continue;
            }
            if (pd.getWriteMethod() == null && isInvalidReadOnlyPropertyType(pd.getPropertyType(), beanClass)) {
                // Ignore read-only properties such as ClassLoader - no need to bind to those
                continue;
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Found bean property '" + pd.getName() + "'" +
                    (pd.getPropertyType() != null ? " of type [" + pd.getPropertyType().getName() + "]" : "") +
                    (pd.getPropertyEditorClass() != null ?
                        "; editor [" + pd.getPropertyEditorClass().getName() + "]" : ""));
            }
            pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
            // 生成一个 PropertyDescriptor 的map 提高调用速度
            this.propertyDescriptors.put(pd.getName(), pd);
            Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
                readMethodNames.add(readMethod.getName());
            }
        }

        // Explicitly check implemented interfaces for setter/getter methods as well,
        // in particular for Java 8 default methods...
        Class<?> currClass = beanClass;
        while (currClass != null && currClass != Object.class) {
            introspectInterfaces(beanClass, currClass, readMethodNames);
            currClass = currClass.getSuperclass();
        }

        // Check for record-style accessors without prefix: e.g. "lastName()"
        // - accessor method directly referring to instance field of same name
        // - same convention for component accessors of Java 15 record classes
        introspectPlainAccessors(beanClass, readMethodNames);
    } catch (IntrospectionException ex) {
        throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
    }
}

```

Spring 源码就这么简单，总结下来就是 解析成PropertyDescriptor -> 缓存 -> 循环 target 的 method 去反射调用。

### 为什么Spring BeanUtils 速度比 Apache BeanUtils 快？

2个源码我们都看完了，实际上2个源码几乎是一模一样的， Apache BeanUtils 慢的原因如主要是：对象拷贝加了各种花里胡哨的校验。

是不是答案特别的神奇，不看源码我也没法下这个结论。

### 总结

我们已经学习了 Apache BeanUtils 和 Spring BeanUtils 的源码，虽然 Spring BeanUtils 的性能比 Apache BeanUtils 好很多，但是和原生的还是有一定差距。

我们有办法 进一步 的优化 BeanUtils 的性能呢？

还有不管是 Spring BeanUtils 还是 Apache BeanUtils 都只能拷贝相同的属性，如果属性名不一样怎么办？

下一节我就带大家来解决这个2个问题。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
