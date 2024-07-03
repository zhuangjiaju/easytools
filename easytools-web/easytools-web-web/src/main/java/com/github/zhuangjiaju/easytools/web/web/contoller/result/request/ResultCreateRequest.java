package com.github.zhuangjiaju.easytools.web.web.contoller.result.request;

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
public class ResultCreateRequest {

    /**
     * 结果
     */
    private String result;
}
