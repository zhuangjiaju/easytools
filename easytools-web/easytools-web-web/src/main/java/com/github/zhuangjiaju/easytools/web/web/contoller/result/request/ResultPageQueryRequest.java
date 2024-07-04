package com.github.zhuangjiaju.easytools.web.web.contoller.result.request;

import com.github.zhuangjiaju.easytools.tools.base.wrapper.request.PageQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 分页查询
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPageQueryRequest extends PageQueryRequest {

    /**
     * 结果
     */
    private String result;
}
