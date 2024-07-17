package com.github.zhuangjiaju.easytools.web.common.mapper;

import java.util.List;

import com.alibaba.fastjson2.JSON;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

/**
 * 通用的web转换器
 *
 * @author Jiaju Zhuang
 */

@Mapper(componentModel = "spring")
public abstract class CommonWebMapper {

    /**
     * 将一个json 数组转成
     *
     * @param jsonString
     * @return
     */
    @Named("commonJsonString2LongList")
    public List<Long> commonJsonString2LongList(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        return JSON.parseArray(jsonString, Long.class);
    }
}

