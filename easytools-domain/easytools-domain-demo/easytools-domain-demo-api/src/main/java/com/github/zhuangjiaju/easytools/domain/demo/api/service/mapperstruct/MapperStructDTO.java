package com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct;

import java.io.Serializable;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.user.UserDTO;
import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * MapperStruct 对象
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructDTO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id的json
     */
    private String userIdJson;

    /**
     * 创建人
     */
    private UserDTO createUser;
}

