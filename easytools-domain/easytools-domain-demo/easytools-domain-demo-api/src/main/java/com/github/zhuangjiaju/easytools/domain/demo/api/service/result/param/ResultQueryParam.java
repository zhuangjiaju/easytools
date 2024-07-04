package com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param;

import com.github.zhuangjiaju.easytools.tools.base.wrapper.param.QueryParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 单表普通查询
 * 最多返回500条
 *
 * @author Jiaju Zhuang
 */
@Data
@NoArgsConstructor
public class ResultQueryParam extends QueryParam {

    /**
     * 结果
     */
    @NonNull
    private String result;


}
