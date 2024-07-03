package com.github.zhuangjiaju.easytools.domain.demo.api.service.result;

import java.util.List;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.dto.ResultDTO;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultCreateParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultPageQueryParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultQueryParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultSelector;
import com.github.zhuangjiaju.easytools.tools.base.excption.DataNotExistException;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.PageResult;

/**
 * 返回结果服务
 *
 *@author Jiaju Zhuang
 */
public interface ResultDemoService {

    /**
     * 创建一条数据
     *
     * @param param 创建参数
     * @return id
     */
    Long create(@Valid @NotNull ResultCreateParam param);


    /**
     * 查询一条数据，不存在会抛出异常
     *
     * @param id       主键id
     * @param selector 选择器
     * @return 必定返回一条数据
     * @throws DataNotExistException 找不到数据的异常
     */
    ResultDTO queryExistent(@Valid @NotNull Long id, @Nullable ResultSelector selector);


    /**
     * 查询一条数据
     *
     * @param id       主键id
     * @param selector 选择器
     * @return 返回一条数据，可能为空
     */
    ResultDTO query(@Nullable Long id, @Nullable ResultSelector selector);


    /**
     * 根据参数查询列表数据
     *
     * @param param    查询参数
     * @param selector 选择器
     * @return 返回查询数据，最多500条
     */
    List<ResultDTO> listQuery(@Valid @NotNull ResultQueryParam param, @Nullable ResultSelector selector);

    /**
     * 根据参数分页查询列表数据
     *
     * @param param    查询参数
     * @param selector 选择器
     * @return 返回分页查询数据
     */
    PageResult<ResultDTO> pageQuery(@Valid @NotNull ResultPageQueryParam param, @Nullable ResultSelector selector);

}

