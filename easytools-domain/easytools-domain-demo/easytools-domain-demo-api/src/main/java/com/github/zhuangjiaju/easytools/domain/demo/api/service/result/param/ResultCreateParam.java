package com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建对象
 *
 * @author 是仪
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultCreateParam {

    /**
     * 结果
     */
    private String result;
}
