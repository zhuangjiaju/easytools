package com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct;

import com.github.zhuangjiaju.easytools.tools.base.excption.DataNotExistException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * MapperStruct 服务
 *
 * @author Jiaju Zhuang
 */
public interface MapperStructDemoService {

    /**
     * 查询一条数据，不存在会抛出异常
     *
     * @param id 主键id
     * @return 必定返回一条数据
     * @throws DataNotExistException 找不到数据的异常
     */
    MapperStructDTO queryExistent(@Valid @NotNull Long id);

}

