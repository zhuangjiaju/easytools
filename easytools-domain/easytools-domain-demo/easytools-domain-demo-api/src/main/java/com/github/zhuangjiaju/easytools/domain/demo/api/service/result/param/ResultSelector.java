package com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 选择器
 *
 *@author Jiaju Zhuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResultSelector {

    /**
     * 空
     */
    private Boolean empty;
}
