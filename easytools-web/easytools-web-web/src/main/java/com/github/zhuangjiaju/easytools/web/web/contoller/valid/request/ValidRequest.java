package com.github.zhuangjiaju.easytools.web.web.contoller.valid.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * 校验请求
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValidRequest {

    /**
     * id
     * 校验不能为空
     */
    @NotNull
    private Long id;

    /**
     * 名字
     * 校验长度不能超过10
     */
    @NotNull
    @Length(max = 10, message = "名字长度最长为10")
    private String name;

    /**
     * 名字
     * 校验必须是电子邮箱格式
     */
    @NotNull
    @Email
    private String email;

    /**
     * 手机号
     * 校验1开头的11位数字
     */
    @NotNull
    @Pattern(regexp = "^1\\d{10}$")
    private String mobile;
}
