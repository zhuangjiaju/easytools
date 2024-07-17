package com.github.zhuangjiaju.easytools.domain.demo.api.service.user;

import java.io.Serializable;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;
}

