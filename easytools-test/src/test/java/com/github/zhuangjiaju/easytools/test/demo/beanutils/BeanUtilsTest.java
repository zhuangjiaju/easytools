package com.github.zhuangjiaju.easytools.test.demo.beanutils;

import java.util.List;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanMap;

/**
 * BeanUtils 案例
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class BeanUtilsTest extends SimpleBaseTest {

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
            org.springframework.beans.BeanUtils.copyProperties(beanUtilsDemo, newBeanUtilsDemo);
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

}
