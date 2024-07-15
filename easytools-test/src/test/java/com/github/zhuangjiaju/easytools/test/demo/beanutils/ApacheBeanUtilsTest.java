package com.github.zhuangjiaju.easytools.test.demo.beanutils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

/**
 * Apache BeanUtils 案例
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class ApacheBeanUtilsTest extends SimpleBaseTest {

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

    /**
     * 自己写一个简单的 BeanUtils
     */
    @Test
    public void custom() throws Exception {
        BeanUtilsDemoDTO beanUtilsDemo = new BeanUtilsDemoDTO();
        beanUtilsDemo.setId("id");
        beanUtilsDemo.setFirstName("firstName");

        BeanUtilsDemoDTO newBeanUtilsDemo = new BeanUtilsDemoDTO();
        MyApacheBeanUtils.copyProperties(newBeanUtilsDemo, beanUtilsDemo);
        log.info("newBeanUtilsDemo: {}", newBeanUtilsDemo);
    }

    public static class MyApacheBeanUtils {

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

}
