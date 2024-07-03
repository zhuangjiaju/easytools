package com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param;

import com.github.zhuangjiaju.easytools.tools.base.wrapper.param.PageQueryParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 单表分页查询
 *
 * @author 是仪
 */
@Data
@NoArgsConstructor
public class ResultPageQueryParam extends PageQueryParam {
    /**
     * 结果
     */
    @NonNull
    private String result;
}
