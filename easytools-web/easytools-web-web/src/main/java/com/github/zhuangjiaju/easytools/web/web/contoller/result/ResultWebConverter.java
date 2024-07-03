package com.github.zhuangjiaju.easytools.web.web.contoller.result;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.dto.ResultDTO;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultCreateParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultPageQueryParam;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.request.ResultCreateRequest;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.request.ResultPageQueryRequest;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.vo.ResultPageQueryVO;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.vo.ResultQueryVO;
import org.mapstruct.Mapper;

/**
 * 转换器
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring")
public abstract class ResultWebConverter {

    /**
     * 转换
     *
     * @param request
     * @return
     */
    public abstract ResultCreateParam request2param(ResultCreateRequest request);

    /**
     * 转换
     *
     * @param dto
     * @return
     */
    public abstract ResultQueryVO dto2voQuery(ResultDTO dto);

    /**
     * 转换
     *
     * @param dto
     * @return
     */
    public abstract ResultPageQueryVO dto2voPageQuery(ResultDTO dto);

    /**
     * 转换
     *
     * @param request
     * @return
     */
    public abstract ResultPageQueryParam request2param(ResultPageQueryRequest request);
}
