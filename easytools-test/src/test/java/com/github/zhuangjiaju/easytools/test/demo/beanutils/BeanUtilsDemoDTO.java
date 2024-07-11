package com.github.zhuangjiaju.easytools.test.demo.beanutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
