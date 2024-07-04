package com.github.zhuangjiaju.easytools.web.web.contoller.exception;

import com.github.zhuangjiaju.easytools.tools.base.excption.BusinessException;
import com.github.zhuangjiaju.easytools.tools.base.excption.CommonExceptionEnum;
import com.github.zhuangjiaju.easytools.tools.base.excption.SystemException;
import com.github.zhuangjiaju.easytools.tools.base.wrapper.result.ActionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo/异常模板
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/result")
public class ExceptionWebController {

    /**
     * 测试业务异常
     *
     * @return
     */
    @GetMapping("business-exception")
    public ActionResult businessException() {
        throw BusinessException.of("业务异常");
    }

    /**
     * 测试系统异常
     *
     * @return
     */
    @GetMapping("system-exception")
    public ActionResult systemException() {
        throw SystemException.of("系统异常");
    }

    /**
     * 坏的案例
     *
     * @return
     */
    @RequestMapping(value = "/base-case")
    public ActionResult baseCase() {
        try {
            // 业务处理
            // ...
        } catch (BusinessException e) {
            log.info("业务发生异常", e);
            return ActionResult.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.info("业务发生异常", e);
            return ActionResult.fail(CommonExceptionEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        return ActionResult.isSuccess();
    }

}
