# Java对象复制系列四:还在使用 Spring BeanUtils 复制属性？这个工具性能秒杀它

## 前言

我们在平时的工作中经常会遇到两个对象的拷贝，很多同学使用 Spring BeanUtils ，感觉性能不错。

但是他无法解决属性名不一样的情况，也无法解决属性类型的转换，性能和原生的 get/set 比也差的很多。

今天我来介绍一款性能堪比原生 get/set 的对象复制工具：MapperStruct。

对 Spring BeanUtils 或者 Apache BeanUtils 原理好奇的同学可以去主页查看对应的文章。

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/mapperstruct/MapperStructTest)

### 简单的复制对象

直接上代码：

创建一个java 对象：

```java
/**
 * Apache BeanUtils 使用的demo
 */
@Test
public void demo() throws Exception {
    BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
    beanUtilsDemo.setId("id");
    beanUtilsDemo.setFirstName("firstName");

    BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
    BeanUtils.copyProperties(newBeanUtilsDemo, beanUtilsDemo);
    log.info("newBeanUtilsDemo: {}", newBeanUtilsDemo);
}
```

输出结果:

```text
     20:21:56.949 [main] INFO com.github.zhuangjiaju.easytools.test.demo.beanutils.ApacheBeanUtilsTest -- newBeanUtilsDemo: BeanUtilsDemoDTO(id=id, firstName=firstName, lastName=null, age=null, email=null, phoneNumber=null, address=null, city=null, state=null, country=null, major=null, gpa=null, department=null, yearOfStudy=null, advisorName=null, enrollmentStatus=null, dormitoryName=null, roommateName=null, scholarshipDetails=null, extracurricularActivities=null)
```

可见已经复制对象成功了，输出里面有 firstName 的值。

### 直接自己写一个简单的 BeanUtils

源码有点复杂，我先直接写一个简单的 BeanUtils，非常的通俗易懂，看懂了然后再看源代码就非常容易了。

复制的代码一模一样：

```java
/**
 * 自己写一个简单的 BeanUtils
 */
@Test
public void custom() throws Exception {
    BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
    beanUtilsDemo.setId("id");
    beanUtilsDemo.setFirstName("firstName");

    BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
    MyBeanUtils.copyProperties(newBeanUtilsDemo, beanUtilsDemo);
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
 public static class MyBeanUtils {

    /**
     * 复制方法
     *
     * @param dest
     * @param orig
     * @throws Exception
     */
    public static void copyProperties(Object dest, Object orig) throws Exception {
        // 获取目标对象的 PropertyDescriptor 属性资料
        PropertyDescriptor[] destPropertyDescriptors = Introspector.getBeanInfo(dest.getClass(), Object.class)
            .getPropertyDescriptors();
        // 获取来源对象的 PropertyDescriptor 属性资料
        PropertyDescriptor[] origPropertyDescriptors = Introspector.getBeanInfo(orig.getClass(), Object.class)
            .getPropertyDescriptors();
        // 上面2个 在 Apache BeanUtils 还加了缓存

        // 循环目标对象
        for (PropertyDescriptor propertyDescriptor : destPropertyDescriptors) {
            // 获取属性名
            String name = propertyDescriptor.getName();
            // 循环来源对象的属性名
            for (PropertyDescriptor origPropertyDescriptor : origPropertyDescriptors) {
                // 2个属性名匹配上了
                if (name.equals(origPropertyDescriptor.getName())) {
                    // 直接获取 method 然后反色调用即可 就设置了数据
                    propertyDescriptor.getWriteMethod().invoke(dest,
                        origPropertyDescriptor.getReadMethod().invoke(orig));
                    break;
                }
            }
        }
    }
}

```

代码是不是非常的容易？就是循环目标对象的属性，然后循环来源对象的属性，然后匹配上了就反射调用即可。

和 Apache BeanUtils 的源码逻辑基本一样，只是没有加缓存之类的。

### 源码解析

org.apache.commons.beanutils.BeanUtils.copyProperties

```java
public static void copyProperties(final Object dest, final Object orig)
    throws IllegalAccessException, InvocationTargetException {

    // BeanUtilsBean 放在了 ThreadLocal 里面，所以是不可以并发的，但是通过 ThreadLocal 保障了BeanUtilsBean不会并发 也不会每次都new 
    // 直接调用 copyProperties
    BeanUtilsBean.getInstance().copyProperties(dest, orig);
}
```

org.apache.commons.beanutils.BeanUtilsBean.copyProperties

```java
 public void copyProperties(final Object dest, final Object orig)
    throws IllegalAccessException, InvocationTargetException {

    // Validate existence of the specified beans
    if (dest == null) {
        throw new IllegalArgumentException
            ("No destination bean specified");
    }
    if (orig == null) {
        throw new IllegalArgumentException("No origin bean specified");
    }
    if (log.isDebugEnabled()) {
        log.debug("BeanUtils.copyProperties(" + dest + ", " +
            orig + ")");
    }

    // Copy the properties, converting as necessary
    if (orig instanceof DynaBean) {
        final DynaProperty[] origDescriptors =
            ((DynaBean)orig).getDynaClass().getDynaProperties();
        for (DynaProperty origDescriptor : origDescriptors) {
            final String name = origDescriptor.getName();
            // Need to check isReadable() for WrapDynaBean
            // (see Jira issue# BEANUTILS-61)
            if (getPropertyUtils().isReadable(orig, name) &&
                getPropertyUtils().isWriteable(dest, name)) {
                final Object value = ((DynaBean)orig).get(name);
                copyProperty(dest, name, value);
            }
        }
    } else if (orig instanceof Map) {
        @SuppressWarnings("unchecked")
        final
        // Map properties are always of type <String, Object>
        Map<String, Object> propMap = (Map<String, Object>)orig;
        for (final Map.Entry<String, Object> entry : propMap.entrySet()) {
            final String name = entry.getKey();
            if (getPropertyUtils().isWriteable(dest, name)) {
                copyProperty(dest, name, entry.getValue());
            }
        }
    } else /* if (orig is a standard JavaBean) */ {
        // 这里比较核心 获取来源的PropertyDescriptor 属性资料 和我们自己实现的代码 一样
        // getPropertyDescriptors 我们会继续跟进
        final PropertyDescriptor[] origDescriptors =
            getPropertyUtils().getPropertyDescriptors(orig);
        // 循环来源的 属性资料
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            final String name = origDescriptor.getName();
            if ("class".equals(name)) {
                continue; // No point in trying to set an object's class
            }
            if (getPropertyUtils().isReadable(orig, name) &&
                getPropertyUtils().isWriteable(dest, name)) {
                try {
                    final Object value =
                        getPropertyUtils().getSimpleProperty(orig, name);
                    // 调用复制参数 
                    // copyProperty 我们会继续跟进
                    copyProperty(dest, name, value);
                } catch (final NoSuchMethodException e) {
                    // Should not happen
                }
            }
        }
    }

}
```

org.apache.commons.beanutils.PropertyUtilsBean.getPropertyDescriptors(java.lang.Object)
org.apache.commons.beanutils.PropertyUtilsBean.getPropertyDescriptors(java.lang.Class<?>)
org.apache.commons.beanutils.PropertyUtilsBean.getIntrospectionData

```java
  private BeanIntrospectionData getIntrospectionData(final Class<?> beanClass) {
    if (beanClass == null) {
        throw new IllegalArgumentException("No bean class specified");
    }

    // Look up any cached information for this bean class
    // 和我们自己写的比，这里核心是加了一个descriptorsCache 的缓存 
    BeanIntrospectionData data = descriptorsCache.get(beanClass);
    if (data == null) {
        data = fetchIntrospectionData(beanClass);
        descriptorsCache.put(beanClass, data);
    }

    return data;
}

```

org.apache.commons.beanutils.PropertyUtilsBean.fetchIntrospectionData
org.apache.commons.beanutils.DefaultBeanIntrospector.introspect

```java
 public void introspect(final IntrospectionContext icontext) {
    BeanInfo beanInfo = null;
    try {
        // 这里和我们自己实现的一样 可以获取一个类的 BeanInfo 信息
        beanInfo = Introspector.getBeanInfo(icontext.getTargetClass());
    } catch (final IntrospectionException e) {
        // no descriptors are added to the context
        log.error(
            "Error when inspecting class " + icontext.getTargetClass(),
            e);
        return;
    }

    //  获取 bean 的 PropertyDescriptor 属性的资料信息
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    if (descriptors == null) {
        descriptors = new PropertyDescriptor[0];
    }

    handleIndexedPropertyDescriptors(icontext.getTargetClass(),
        descriptors);
    icontext.addPropertyDescriptors(descriptors);
}
```

通过以上方法，我们就拿到了 PropertyDescriptor[] origDescriptors 接下来我们看 copyProperty

org.apache.commons.beanutils.BeanUtilsBean.copyProperty

```java
public void copyProperty(final Object bean, String name, Object value)
    throws IllegalAccessException, InvocationTargetException {

    // Trace logging (if enabled)
    if (log.isTraceEnabled()) {
        final StringBuilder sb = new StringBuilder("  copyProperty(");
        sb.append(bean);
        sb.append(", ");
        sb.append(name);
        sb.append(", ");
        if (value == null) {
            sb.append("<NULL>");
        } else if (value instanceof String) {
            sb.append((String)value);
        } else if (value instanceof String[]) {
            final String[] values = (String[])value;
            sb.append('[');
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(values[i]);
            }
            sb.append(']');
        } else {
            sb.append(value.toString());
        }
        sb.append(')');
        log.trace(sb.toString());
    }

    // Resolve any nested expression to get the actual target bean
    Object target = bean;
    final Resolver resolver = getPropertyUtils().getResolver();
    while (resolver.hasNested(name)) {
        try {
            target = getPropertyUtils().getProperty(target, resolver.next(name));
            name = resolver.remove(name);
        } catch (final NoSuchMethodException e) {
            return; // Skip this property setter
        }
    }
    if (log.isTraceEnabled()) {
        log.trace("    Target bean = " + target);
        log.trace("    Target name = " + name);
    }

    // Declare local variables we will require
    final String propName = resolver.getProperty(name); // Simple name of target property
    Class<?> type = null;                         // Java type of target property
    final int index = resolver.getIndex(name);         // Indexed subscript value (if any)
    final String key = resolver.getKey(name);           // Mapped key value (if any)

    // Calculate the target property type
    if (target instanceof DynaBean) {
        final DynaClass dynaClass = ((DynaBean)target).getDynaClass();
        final DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
        if (dynaProperty == null) {
            return; // Skip this property setter
        }
        type = dynaPropertyType(dynaProperty, value);
    } else {
        PropertyDescriptor descriptor = null;
        try {
            descriptor =
                getPropertyUtils().getPropertyDescriptor(target, name);
            if (descriptor == null) {
                return; // Skip this property setter
            }
        } catch (final NoSuchMethodException e) {
            return; // Skip this property setter
        }
        type = descriptor.getPropertyType();
        if (type == null) {
            // Most likely an indexed setter on a POJB only
            if (log.isTraceEnabled()) {
                log.trace("    target type for property '" +
                    propName + "' is null, so skipping ths setter");
            }
            return;
        }
    }
    if (log.isTraceEnabled()) {
        log.trace("    target propName=" + propName + ", type=" +
            type + ", index=" + index + ", key=" + key);
    }

    // Convert the specified value to the required type and store it
    if (index >= 0) {                    // Destination must be indexed
        value = convertForCopy(value, type.getComponentType());
        try {
            getPropertyUtils().setIndexedProperty(target, propName,
                index, value);
        } catch (final NoSuchMethodException e) {
            throw new InvocationTargetException
                (e, "Cannot set " + propName);
        }
    } else if (key != null) {            // Destination must be mapped
        // Maps do not know what the preferred data type is,
        // so perform no conversions at all
        // FIXME - should we create or support a TypedMap?
        try {
            getPropertyUtils().setMappedProperty(target, propName,
                key, value);
        } catch (final NoSuchMethodException e) {
            throw new InvocationTargetException
                (e, "Cannot set " + propName);
        }
    } else {                             // Destination must be simple
        value = convertForCopy(value, type);
        try {
            // 核心我们看这里 设置属性值
            getPropertyUtils().setSimpleProperty(target, propName, value);
        } catch (final NoSuchMethodException e) {
            throw new InvocationTargetException
                (e, "Cannot set " + propName);
        }
    }

}

```

org.apache.commons.beanutils.PropertyUtilsBean.setSimpleProperty

```java
public void setSimpleProperty(final Object bean,
    final String name, final Object value)
    throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {

    if (bean == null) {
        throw new IllegalArgumentException("No bean specified");
    }
    if (name == null) {
        throw new IllegalArgumentException("No name specified for bean class '" +
            bean.getClass() + "'");
    }

    // Validate the syntax of the property name
    if (resolver.hasNested(name)) {
        throw new IllegalArgumentException
            ("Nested property names are not allowed: Property '" +
                name + "' on bean class '" + bean.getClass() + "'");
    } else if (resolver.isIndexed(name)) {
        throw new IllegalArgumentException
            ("Indexed property names are not allowed: Property '" +
                name + "' on bean class '" + bean.getClass() + "'");
    } else if (resolver.isMapped(name)) {
        throw new IllegalArgumentException
            ("Mapped property names are not allowed: Property '" +
                name + "' on bean class '" + bean.getClass() + "'");
    }

    // Handle DynaBean instances specially
    if (bean instanceof DynaBean) {
        final DynaProperty descriptor =
            ((DynaBean)bean).getDynaClass().getDynaProperty(name);
        if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" +
                name + "' on dynaclass '" +
                ((DynaBean)bean).getDynaClass() + "'");
        }
        ((DynaBean)bean).set(name, value);
        return;
    }

    // Retrieve the property setter method for the specified property
    final PropertyDescriptor descriptor =
        getPropertyDescriptor(bean, name);
    if (descriptor == null) {
        throw new NoSuchMethodException("Unknown property '" +
            name + "' on class '" + bean.getClass() + "'");
    }
    // 通过 PropertyDescriptor 获取道理 set 方法 的 Method
    final Method writeMethod = getWriteMethod(bean.getClass(), descriptor);
    if (writeMethod == null) {
        throw new NoSuchMethodException("Property '" + name +
            "' has no setter method in class '" + bean.getClass() + "'");
    }

    // Call the property setter method
    final Object[] values = new Object[1];
    values[0] = value;
    if (log.isTraceEnabled()) {
        final String valueClassName =
            value == null ? "<null>" : value.getClass().getName();
        log.trace("setSimpleProperty: Invoking method " + writeMethod
            + " with value " + value + " (class " + valueClassName + ")");
    }
    // 这个方法就是 简单的 writeMethod 调用 invoke 方法 这样子我们的值就设置好了
    invokeMethod(writeMethod, bean, values);


```

好了，这样子一个值就复制到新的对象里面了，是不是很简单？

### 总结

今天学习了 Apache BeanUtils 的源码，总体上就是一个缓存+反射的调用，看是记不住的，大家赶快打开自己的电脑跟几遍源码吧。

后面还会带大家看 Spring BeanUtils 的源码，欢迎持续关注。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)