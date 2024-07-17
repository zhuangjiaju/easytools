package com.github.zhuangjiaju.easytools.web.web.contoller.mapperstruct;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct.MapperStructDTO;
import com.github.zhuangjiaju.easytools.web.common.mapper.BaseCommonWebMapper;
import com.github.zhuangjiaju.easytools.web.common.mapper.CommonWebMapper;
import com.github.zhuangjiaju.easytools.web.web.contoller.mapperstruct.vo.MapperStructQueryVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 * 转换器
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring", uses = {CommonWebMapper.class}, imports = {DateUtil.class})
public abstract class MapperStructWebMapper extends BaseCommonWebMapper {

    /**
     * 转换
     *
     * @param user
     * @return
     */
    @Mappings({
        @Mapping(target = "currentDate", expression = "java(DateUtil.date())"),
        @Mapping(target = "userIdList", source = "userIdJson", qualifiedByName = "commonJsonString2LongList"),
    })
    public abstract MapperStructQueryVO dto2vo(MapperStructDTO user);

    @AfterMapping
    protected void afterMapperStructQuery(@MappingTarget MapperStructQueryVO target, MapperStructDTO source) {
        if (target == null) {
            return;
        }
        target.setUuid(UUID.fastUUID().toString());
    }
}

