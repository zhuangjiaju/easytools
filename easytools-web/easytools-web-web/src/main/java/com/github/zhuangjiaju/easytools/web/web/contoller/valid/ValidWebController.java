package com.github.zhuangjiaju.easytools.web.web.contoller.valid;

import com.alibaba.fastjson2.JSON;

import cn.hutool.core.lang.Validator;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.result.ResultDemoService;
import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import com.github.zhuangjiaju.easytools.web.web.contoller.result.ResultWebConverter;
import com.github.zhuangjiaju.easytools.web.web.contoller.valid.request.ValidRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo/校验模板
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/valid")
public class ValidWebController {
    private final ResultDemoService resultDemoService;
    private final ResultWebConverter resultWebConverter;

    /**
     * 校验数据
     *
     * @param request 参数
     * @return id
     */
    @PostMapping("valid")
    public ActionResult valid(@Valid @RequestBody ValidRequest request) {
        // 做业务逻辑
        log.info("请求参数:{}", JSON.toJSONString(request));
        return ActionResult.isSuccess();
    }

    /**
     * 坏的案例
     * 这里会有很多冗余的代码
     * 而且如果写成所有异常一起提示更复杂
     *
     * @return
     */
    @RequestMapping(value = "/base-case")
    public ActionResult baseCase(@RequestBody ValidRequest request) {
        if (request.getId() == null) {
            return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "id不能为空");
        }
        if (!Validator.isEmail(request.getEmail())) {
            return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "邮箱格式不正确");
        }
        if (StringUtils.length(request.getName()) > 10) {
            return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "名字最长为10个字符串");
        }
        if (!Validator.isMobile(request.getMobile())) {
            return ActionResult.fail(CommonExceptionEnum.INVALID_PARAMETER, "手机格式不正确");
        }
        // 做业务逻辑
        log.info("请求参数:{}", JSON.toJSONString(request));
        return ActionResult.isSuccess();
    }
}
