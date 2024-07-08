package com.github.zhuangjiaju.easytools.test.demo.cglib;

import java.lang.reflect.Field;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * cglib测试
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class CglibTest extends SimpleBaseTest {

    private static int count = 0;
    private static final int RUN_COUNT = 10 * 10000;

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
        log.info("输出结果:{}", cglibDemo.getName());
        Assertions.assertEquals("JiaJu Zhuang 你真棒！！！", cglibDemo.getName());
    }

}
