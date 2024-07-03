package com.github.zhuangjiaju.easytools.domain.demo.api.service.result.dto;

import java.io.Serializable;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 返回结果服务
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * 结果
     */
    private String result;
}

