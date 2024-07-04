package com.github.zhuangjiaju.easytools.web.web.contoller.result;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.ResultDemoService;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.DataResult;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.PageResult;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.request.ResultCreateRequest;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.request.ResultPageQueryRequest;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.vo.ResultPageQueryVO;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.vo.ResultQueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo/结果模板
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/result")
public class ResultWebController {
    private final ResultDemoService resultDemoService;
    private final ResultWebConverter resultWebConverter;

    /**
     * 创建一条数据
     *
     * @param request 创建参数
     * @return id
     */
    @PostMapping("create")
    public DataResult<Long> create(@Valid @RequestBody ResultCreateRequest request) {
        return DataResult.of(resultDemoService.create(resultWebConverter.request2param(request)));
    }

    /**
     * 查询一条数据
     *
     * @param id 主键
     * @return
     */
    @GetMapping("query")
    public DataResult<ResultQueryVO> query(@Valid @NotNull Long id) {
        return DataResult.of(resultWebConverter.dto2voQuery(resultDemoService.queryExistent(id, null)));
    }

    /**
     * 分页查询列表数据
     *
     * @param request request
     * @return
     */
    @GetMapping("page-query")
    public PageResult<ResultPageQueryVO> pageQuery(@Valid ResultPageQueryRequest request) {
        return resultDemoService.pageQuery(resultWebConverter.request2param(request), null)
            .map(resultWebConverter::dto2voPageQuery);
    }

}
