# 大厂面试必备系列：一文彻底搞懂 Jdk 动态代理

## 前言

大家在面试中经常被问到 Cglib 和 JDK动态代理有啥区别？ 然后每次回答都是 Jdk 动态代理是实现接口。

这个回答当然是对的，但是太敷衍了，没得加分，今天我带大家深入了解下，带你搞清楚到时实现了啥东西。

## 最佳实践

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/jdk/JdkProxyTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/jdk/JdkProxyTest.java)

建议先读 "大厂面试必备系列：一文彻底搞懂 Cglib 代理"
,可以打开 [https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools) 找到案例

### 最简单的jdk动态代理案例

jdk动态代理核心是根据接口生成一个代理类，然后访问接口的时候先调用 InvocationHandler ,然后再调用具体实现类。

假设我们现在要实现：调用setName 都改成 name+" 你真棒！！！"

这里和 Cglib 不同，必须有接口 不然无法使用：

```java
/**
 * jdk动态代理的接口
 * 这里和 Cglib 不同，必须有接口 不然无法使用
 *
 * @author Jiaju Zhuang
 */
public interface JdkProxyDemoInterfaceDTO {

    String getName();

    void setName(String name);
}


```
具体实现类很简单：

```java

/**
 * jdk动态代理的案例
 *
 * @author Jiaju Zhuang
 */
public class JdkProxyDemoDTO implements JdkProxyDemoInterfaceDTO {
    /**
     * 名字
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

最后是实现代码：
```java
     /**
 * 最简单的jdk动态代理案例
 * 调用setName 都改成 name+" 你真棒！！！"
 */
@Test
public void simple() throws Exception {
    // 我们需求是将 jdkProxyDemo.name 设置成 "JiaJu Zhuang"
    // 先新建一个对象
    Object object = new JdkProxyDemoDTO();
    JdkProxyDemoInterfaceDTO proxyInstance = (JdkProxyDemoInterfaceDTO)Proxy.newProxyInstance(
        // 指定用上面类加载器
        JdkProxyTest.class.getClassLoader(),
        // 指定要代理成上面接口
        new Class<?>[] {JdkProxyDemoInterfaceDTO.class},
        // 指定InvocationHandler，用于代理后的回调
        (proxy, method, args) -> {
            // 判断setName方法且是String
            if (method.getName().equals("setName") && args[0] != null && args[0] instanceof String) {
                String name = (String)args[0];
                // 修改参数
                args[0] = name + " 你真棒！！！";
            }
            return method.invoke(object, args);
        });

    proxyInstance.setName("JiaJu Zhuang");
    log.info("输出结果:{},{}", proxyInstance.getName(), proxyInstance.getClass());
    Assertions.assertEquals("JiaJu Zhuang 你真棒！！！", proxyInstance.getName());
}

```

是不是好好奇？为什么调用 `proxyInstance.setName` 会回调到 `InvocationHandler` 的代码呢？

### 错误的查看生成后的源码

我们通过`System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");` 来设置输出代理类，无法指定位置，生成在工程目录下。

```java
    /**
 * 查看生成后的源码
 */
@Test
public void showClass() throws Exception {
    // 只需要设置 jdk.proxy.ProxyGenerator.saveGeneratedFiles
    // 这里设置了没用？
    System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

    // 先新建一个对象
    Object object = new JdkProxyDemoDTO();
    JdkProxyDemoInterfaceDTO proxyInstance = (JdkProxyDemoInterfaceDTO)Proxy.newProxyInstance(
        // 指定用上面类加载器
        JdkProxyTest.class.getClassLoader(),
        // 指定要代理成上面接口
        new Class<?>[] {JdkProxyDemoInterfaceDTO.class},
        // 指定InvocationHandler，用于代理后的回调
        (proxy, method, args) -> {
            // 判断setName方法且是String
            if (method.getName().equals("setName") && args[0] != null && args[0] instanceof String) {
                String name = (String)args[0];
                // 修改参数
                args[0] = name + " 你真棒！！！";
            }
            return method.invoke(object, args);
        });

    proxyInstance.setName("JiaJu Zhuang");
    log.info("输出结果:{},{}", proxyInstance.getName(), proxyInstance.getClass());
    Assertions.assertEquals("JiaJu Zhuang 你真棒！！！", proxyInstance.getName());
}

```

我们会发现压根没有输出，只能跟进源码`java.lang.reflect.ProxyGenerator` ,关键代码：

```java
final class ProxyGenerator extends ClassWriter {
   

    /**
     * 这里是成员变量 所以第一次加载class的时候就会去读去配置
     */
    @SuppressWarnings("removal")
    private static final boolean saveGeneratedFiles =
        java.security.AccessController.doPrivileged(
            new GetBooleanAction(
                "jdk.proxy.ProxyGenerator.saveGeneratedFiles"));

    static byte[] generateProxyClass(ClassLoader loader,
        final String name,
        List<Class<?>> interfaces,
        int accessFlags) {
        ProxyGenerator gen = new ProxyGenerator(loader, name, interfaces, accessFlags);
        final byte[] classFile = gen.generateClassFile();

        // 判断是否需要输出class 需要则输出
        if (saveGeneratedFiles) {
            java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<Void>() {
                    public Void run() {
                        try {
                            int i = name.lastIndexOf('.');
                            Path path;
                            if (i > 0) {
                                Path dir = Path.of(dotToSlash(name.substring(0, i)));
                                Files.createDirectories(dir);
                                path = dir.resolve(name.substring(i + 1) + ".class");
                            } else {
                                path = Path.of(name + ".class");
                            }
                            Files.write(path, classFile);
                            return null;
                        } catch (IOException e) {
                            throw new InternalError(
                                "I/O exception saving generated file: " + e);
                        }
                    }
                });
        }

        return classFile;
    }
}

```
debug后发现 `saveGeneratedFiles` 方法在我们 调用`System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");` 之前就已经设置好了。
原因是：junit 也用了jdk 代理，然后去初始化 `saveGeneratedFiles` 我们压根没有设置变量，所以一直无法打印。


### 正确的查看生成后的源码

最简单的就是不要使用junit了 直接写main函数。

```java
 /**
 * 查看生成后的源码
 */
public static void main(String[] args1) {
    // 只需要设置 jdk.proxy.ProxyGenerator.saveGeneratedFiles 即可
    // 这里为啥不用junit了？ 原因是junit 调用我们 System.setProperty 之前已经用来 动态代理，然后已经读取了 配置 我们设置也没用了
    System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
    // 先新建一个对象
    Object object = new JdkProxyDemoDTO();
    JdkProxyDemoInterfaceDTO proxyInstance = (JdkProxyDemoInterfaceDTO)Proxy.newProxyInstance(
        // 指定用上面类加载器
        JdkProxyTest.class.getClassLoader(),
        // 指定要代理成上面接口
        new Class<?>[] {JdkProxyDemoInterfaceDTO.class},
        // 指定InvocationHandler，用于代理后的回调
        (proxy, method, args) -> {
            // 判断setName方法且是String
            if (method.getName().equals("setName") && args[0] != null && args[0] instanceof String) {
                String name = (String)args[0];
                // 修改参数
                args[0] = name + " 你真棒！！！";
            }
            return method.invoke(object, args);
        });

    proxyInstance.setName("JiaJu Zhuang");
    log.info("输出结果:{},{}", proxyInstance.getName(), proxyInstance.getClass());
    Assertions.assertEquals("JiaJu Zhuang 你真棒！！！", proxyInstance.getName());
}
```

终于在项目的根目录 `jdk/proxy1.$Proxy0` 找到了代理类 ,他继承了 Proxy 类，并实现了我们的接口 JdkProxyDemoInterfaceDTO。

```java

public final class $Proxy0 extends Proxy implements JdkProxyDemoInterfaceDTO {
    private static final Method m0;
    private static final Method m1;
    private static final Method m2;
    private static final Method m3;
    // 对象setName方法
    private static final Method m4;

    public $Proxy0(InvocationHandler var1) {
        super(var1);
    }

    public final int hashCode() {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final boolean equals(Object var1) {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String getName() {
        try {
            return (String)super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    // 这里就是我们调用的方法
    public final void setName(String var1) {
        try {
            // 直接调用父类的 InvocationHandler的invoke方法
            super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        ClassLoader var0 = $Proxy0.class.getClassLoader();

        try {
            m0 = Class.forName("java.lang.Object", false, var0).getMethod("hashCode");
            m1 = Class.forName("java.lang.Object", false, var0).getMethod("equals", Class.forName("java.lang.Object", false, var0));
            m2 = Class.forName("java.lang.Object", false, var0).getMethod("toString");
            m3 = Class.forName("com.github.zhuangjiaju.easytools.test.demo.jdk.JdkProxyDemoInterfaceDTO", false, var0).getMethod("getName");
            // m4 就是我们对应的 Method
            m4 = Class.forName("com.github.zhuangjiaju.easytools.test.demo.jdk.JdkProxyDemoInterfaceDTO", false, var0).getMethod("setName", Class.forName("java.lang.String", false, var0));
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }

    private static MethodHandles.Lookup proxyClassLookup(MethodHandles.Lookup var0) throws IllegalAccessException {
        if (var0.lookupClass() == Proxy.class && var0.hasFullPrivilegeAccess()) {
            return MethodHandles.lookup();
        } else {
            throw new IllegalAccessException(var0.toString());
        }
    }
}


```

是不是很简单？实际上他初始化了`setName` 的 Method 为 m4 ，然后调用 setName 的时候直接调用 InvocationHandler.invoke 就可以，然后就到我们自己写的InvocationHandler来了。

### 总结

通过这篇文章给大家普及了 Jdk 动态代理的基础知识，让 Jdk 动态代理变的不在神秘，大家有时间就去动手试试吧，试过才是自己的。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)