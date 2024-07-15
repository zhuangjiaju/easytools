package com.github.zhuangjiaju.easytools.test.demo.mapperstruct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * MapperStruct的案例
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructSourceDTO {

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

}
