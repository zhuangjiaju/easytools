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
public class MapperStructTargetDTO {

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 名
     */
    private String name;


    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

}
