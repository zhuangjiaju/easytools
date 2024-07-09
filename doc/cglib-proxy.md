# 大厂面试必备系列：一文彻底搞懂 Cglib 代理

## 前言

大家在面试中经常被问到 Cglib 和 JDK动态代理有啥区别？ 然后每次回答都是 Cglib 通过创建目标类的子类来实现代理。

这个回答当然是对的，但是太敷衍了，没得加分，今天我带大家深入了解下。

## 最佳实践

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/cglib/CglibTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/cglib/CglibTest.java)

### 最简单的cglib案例

Cglib 核心是生成了一个代理类，所以性能会比反射有优势。 我们先看一个列子，用反射和cglib分别设置一个值到一个对象中。

```java
/**
 * 最简单的cglib案例
 * 设置一个值到一个对象中
 */
@Test
public void beanMap() throws Exception {
    // 我们需求是将 cglibDemo.name 设置成 "JiaJu Zhuang"

    // 用反射
    CglibDemoDTO cglibDemo = new CglibDemoDTO();
    Field nameField = CglibDemoDTO.class.getDeclaredField("name");
    // 设置私有字段可访问
    nameField.setAccessible(true);
    nameField.set(cglibDemo, "JiaJu Zhuang");
    log.info("设置结果:{}", cglibDemo.getName());
    Assertions.assertEquals("JiaJu Zhuang", cglibDemo.getName());

    // 反射的性能相对较差，所以spring用了cglib代理，性能会更好的方案

    // 用cglib
    // cglib给我们提供了一个BeanMap的工具类，可以将一个对象转换成一个map，并且可以设置值
    cglibDemo = new CglibDemoDTO();
    BeanMap beanMap = BeanMap.create(cglibDemo);
    beanMap.put("name", "JiaJu Zhuang");
    log.info("设置结果:{}", cglibDemo.getName());
    Assertions.assertEquals("JiaJu Zhuang", cglibDemo.getName());
}

```

是不是好好奇？BeanMap 的 put 方法这么神奇，他就是做了啥？

### 查看生成后的源码

我们通过` System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path)` 方法来让 Cglib 输出生成的源码。

```java
    /**
 * 查看生成后的源码
 */
@Test
public void showClass() {
    // 我们只要设置输出一个路径，就可以看到生成的源码
    // 打印到 easytools/easytools-test/target/test-classes/cglib
    String path = this.getClass().getResource("/").getPath() + "cglib";
    log.info("输出目录是：{}", path);
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path);
    BeanMap.create(new CglibDemoDTO());
}

```

先看 BeanMap 的 put 方法，非常简单，直接调用了一个抽象方法的put 方法。

```java
abstract public class BeanMap implements Map {
    @Override
    public Object put(Object key, Object value) {
        return put(bean, key, value);
    }

    abstract public Object put(Object bean, Object key, Object value);
}
```

生成的源码如下，他继承了BeanMap 所以直接看put方法：

```java

package com.github.zhuangjiaju.easytools.test.demo.cglib;

import java.util.Set;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.beans.FixedKeySet;

public class CglibDemoDTO$$BeanMapByCGLIB$$fe3c380f extends BeanMap {
    private static FixedKeySet keys;
    private static final Class CGLIB$load_class$java$2Elang$2EString;

    public CglibDemoDTO$$BeanMapByCGLIB$$fe3c380f() {
    }

    public BeanMap newInstance(Object var1) {
        return new CglibDemoDTO$$BeanMapByCGLIB$$fe3c380f(var1);
    }

    public CglibDemoDTO$$BeanMapByCGLIB$$fe3c380f(Object var1) {
        super(var1);
    }

    public Object get(Object var1, Object var2) {
        CglibDemoDTO var10000 = (CglibDemoDTO)var1;
        String var10001 = (String)var2;
        switch (((String)var2).hashCode()) {
            case 3373707:
                if (var10001.equals("name")) {
                    return var10000.getName();
                }
        }

        return null;
    }

    public Object put(Object var1, Object var2, Object var3) {
        CglibDemoDTO var10000 = (CglibDemoDTO)var1;
        String var10001 = (String)var2;
        // 这里用 switch 核心是加速，如果属性特别多，不用一个个equals，equals的性能不太好
        switch (((String)var2).hashCode()) {
            case 3373707:
                // 判断是否是 name 属性
                if (var10001.equals("name")) {
                    String var10002 = var10000.getName();
                    // 调用原始类的 setName 方法
                    var10000.setName((String)var3);
                    return var10002;
                }
        }

        return null;
    }

    static {
        CGLIB$STATICHOOK1();
        keys = new FixedKeySet(new String[] {"name"});
    }

    static void CGLIB$STATICHOOK1() {
        CGLIB$load_class$java$2Elang$2EString = Class.forName("java.lang.String");
    }

    public Set keySet() {
        return keys;
    }

    public Class getPropertyType(String var1) {
        switch (var1.hashCode()) {
            case 3373707:
                if (var1.equals("name")) {
                    return CGLIB$load_class$java$2Elang$2EString;
                }
        }

        return null;
    }
}

```

是不是很简单？他就是直接调用了原始类的 setName 方法，没有任何技术含量。

### 我们自己来实现下代理

Spring 使用的 Cglib 动态代理，是不是和下面的差不多。 我们对用 setName 方法做一个增强，都改成 name+" 你真棒！！！"

```java
/**
 * 我们自己来实现下代理
 * 调用setName 都改成 name+" 你真棒！！！"
 */
@Test
public void proxy() {
    // 我们只要设置输出一个路径，就可以看到生成的源码
    // 打印到 easytools/easytools-test/target/test-classes/cglib
    String path = this.getClass().getResource("/").getPath() + "cglib";
    log.info("输出目录是：{}", path);
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path);

    // 新建增强器
    Enhancer enhancer = new Enhancer();
    // 父类是CglibDemoDTO
    enhancer.setSuperclass(CglibDemoDTO.class);
    enhancer.setCallback((MethodInterceptor)(obj, method, args, proxy) -> {
        // 判断setName方法且是String
        if (method.getName().equals("setName") && args[0] != null && args[0] instanceof String) {
            String name = (String)args[0];
            // 修改参数
            args[0] = name + " 你真棒！！！";
        }
        // 继续调用父类
        return proxy.invokeSuper(obj, args);
    });
    CglibDemoDTO cglibDemo = (CglibDemoDTO)enhancer.create();
    cglibDemo.setName("JiaJu Zhuang");
    log.info("输出结果:{},{}", cglibDemo.getName(), cglibDemo.getClass());
    Assertions.assertEquals("JiaJu Zhuang 你真棒！！！", cglibDemo.getName());
}

```

输出结果：

```text
20:40:12.644 [main] INFO com.github.zhuangjiaju.easytools.test.demo.cglib.CglibTest -- 输出结果:JiaJu Zhuang 你真棒！！！,class com.github.zhuangjiaju.easytools.test.demo.cglib.CglibDemoDTO$$EnhancerByCGLIB$$6be31ec5

```

可以看到已经能输出 `JiaJu Zhuang 你真棒！！！` 了，而不是原来的 `JiaJu Zhuang`。

我们直接看下生成的代理类 `com.github.zhuangjiaju.easytools.test.demo.cglib.CglibDemoDTO$$EnhancerByCGLIB$$6be31ec5` 他继承了
CglibDemoDTO类， 然后重写了 `setName` 方法,并用`final`修饰。

```java
public class CglibDemoDTO$$EnhancerByCGLIB$$6be31ec5 extends CglibDemoDTO implements Factory {

    // 省略了很多方法

    public final void setName(String var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        // 判断有没有MethodInterceptor 有的话直接调用
        if (var10000 != null) {
            // 直接调用我们的 匿名内部类了
            var10000.intercept(this, CGLIB$setName$1$Method, new Object[] {var1}, CGLIB$setName$1$Proxy);
        } else {
            super.setName(var1);
        }
    }

    // 省略了很多方法
}
```

是不是也特别简单，就是一个简单回调。

### 补充问题：Cglib 能不能代理 final 类？能不能代理 final 方法？

这个就不用我多说了吧，我们看到了生成的代理类，他继承了我们的原始类，原始类是 final 的话，会导致无法代理。

代理方法也一样，如果是 final 方法，子类无法重写父类的 final 方法，所以也无法代理。

这里有个有趣的现象，被代理的方法也是 final 的，所以生成的代理也无法被再次代理了。

### 总结

通过这篇文章大家是不是对吹的神乎其技的 Cglib 有了新的理解，实际上他比大家想象的容易非常多，只是大家没有时间去实践一下，大家去动手试试吧，试过才是自己的。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)