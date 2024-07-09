package com.github.zhuangjiaju.easytools.test.demo.jdk;

import java.lang.reflect.Proxy;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * jdk动态代理的案例
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class JdkProxyTest extends SimpleBaseTest {

    private static int count = 0;
    private static final int RUN_COUNT = 10 * 10000;

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

}
