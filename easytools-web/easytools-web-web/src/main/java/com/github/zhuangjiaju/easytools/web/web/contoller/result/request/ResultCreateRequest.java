package com.github.zhuangjiaju.easytools.web.web.contoller.result.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 创建对象
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResultCreateRequest {

    /**
     * 结果
     */
    private String result;
}
