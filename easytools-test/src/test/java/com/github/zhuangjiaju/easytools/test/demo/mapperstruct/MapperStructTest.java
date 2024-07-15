package com.github.zhuangjiaju.easytools.test.demo.mapperstruct;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsDemoDTO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanMap;

/**
 * MapperStruct 案例
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class MapperStructTest extends SimpleBaseTest {

    /**
     * MapperStruct 使用的demo
     */
    @Test
    public void demo() throws Exception {
        // MapperStructSourceDTO  存在 id(String) firstName(String) lastName(String) age(String)
        // MapperStructTargetDTO  存在 id(Long) name(lastName+firstName) age(Integer)
        // 我们的需求变得复杂了 不仅仅存在 字符在转成数字 还存在字符串拼接 这个显然 BeanUtils 搞不定了
        MapperStructSourceDTO source = new MapperStructSourceDTO();
        source.setId("1");
        source.setLastName("Zhuang");
        source.setFirstName("Jiaju");
        source.setAge("18");

        // 转换对象
        MapperStructTargetDTO target = MapperStructMapper.INSTANCE.dto2dto(source);
        log.info("newBeanUtilsDemo: {}", JSON.toJSONString(target));
    }

    /**
     * 调用N次对象复制
     * 然后比对 Apache BeanUtils、MapperStruct、直接get/set 的性能
     */
    @Test
    public void copy() throws Exception {
        // 随机生成1个对象 然后测试
        doCopy(randomList(1));
        // 随机生成100个对象 然后测试
        doCopy(randomList(100));
        // 随机生成1000个对象 然后测试
        doCopy(randomList(1000));
        // 随机生成10000个对象 然后测试
        doCopy(randomList(10000));
        // 随机生成100000个对象 然后测试
        doCopy(randomList(100000));
    }

    /**
     * 调用对象复制
     *
     * @param randomList
     * @return
     * @throws Exception
     */
    private void doCopy(List<BeanUtilsDemoDTO> randomList) throws Exception {
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

        // MapperStruct
        log.info("MapperStruct 开始处理");
        for (BeanUtilsDemoDTO beanUtilsDemo : randomList) {
            MapperStructMapper.INSTANCE.dto2dto(beanUtilsDemo);
        }
        log.info("MapperStruct 结束处理处理，耗时：{}ms", interval.intervalRestart());

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
         *
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

}
