package com.github.zhuangjiaju.easytools.web.web.contoller.result.vo;

import java.io.Serializable;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 结果分页返回
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResultPageQueryVO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * 结果
     */
    private String result;
}

