package com.github.zhuangjiaju.easytools.domain.demo.core.service.result;

import java.util.List;

import jakarta.annotation.Nullable;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.ResultDemoService;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.dto.ResultDTO;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultCreateParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultPageQueryParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultQueryParam;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.param.ResultSelector;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 返回结果服务
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ResultDemoServiceImpl implements ResultDemoService {

    @Override
    public Long create(ResultCreateParam param) {
        return null;
    }

    @Override
    public ResultDTO queryExistent(Long id, @Nullable ResultSelector selector) {
        return null;
    }

    @Override
    public ResultDTO query(@Nullable Long id, @Nullable ResultSelector selector) {
        return null;
    }

    @Override
    public List<ResultDTO> listQuery(ResultQueryParam param, @Nullable ResultSelector selector) {
        return null;
    }

    @Override
    public PageResult<ResultDTO> pageQuery(ResultPageQueryParam param, @Nullable ResultSelector selector) {
        return null;
    }
}

