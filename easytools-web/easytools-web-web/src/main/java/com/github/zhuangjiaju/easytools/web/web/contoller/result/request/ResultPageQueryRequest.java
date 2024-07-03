package com.github.zhuangjiaju.easytools.web.web.contoller.result.request;

import com.github.zhuangjiaju.easytools.tools.base.wrapper.request.PageQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询
 *
 * @author 是仪
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPageQueryRequest extends PageQueryRequest {

    /**
     * 结果
     */
    private String result;
}
