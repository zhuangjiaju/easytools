package com.github.zhuangjiaju.easytools.web.web.contoller.mapperstruct;

import com.alibaba.fastjson2.JSON;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct.MapperStructDTO;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct.MapperStructDemoService;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.DataResult;
import com.github.zhuangjiaju.easytools.web.web.contoller.mapperstruct.vo.MapperStructQueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo/MapperStruct转换器
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/mapper-struct")
public class MapperStructWebController {
    private final MapperStructDemoService mapperStructDemoService;
    private final MapperStructWebMapper mapperStructWebMapper;

    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    @GetMapping("query")
    public DataResult<MapperStructQueryVO> query(@RequestParam("id") Long id) {
        MapperStructDTO data = mapperStructDemoService.queryExistent(id);
        // 转换需求如下：
        // 1. 将一个 json 字符串 转成一个数组
        // 2. 将一个 用户的DTO对象转成VO对象
        // 3. 放入一个当前的日期
        // 4. 放入一个唯一id
        MapperStructQueryVO vo = mapperStructWebMapper.dto2vo(data);
        log.info("转换前:{}，转换后:{}", JSON.toJSONString(data), JSON.toJSONString(vo));
        return DataResult.of(vo);
    }

}
