package com.github.zhuangjiaju.easytools.web.web.contoller.mapperstruct.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import com.github.zhuangjiaju.easytools.web.common.controller.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 结果查询返回
 *
 * @author Jiaju Zhuang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MapperStructQueryVO implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id的json
     */
    private List<Long> userIdList;

    /**
     * 创建人
     */
    private UserVO createUser;

    /**
     * 当前日期
     */
    private Date currentDate;

    /**
     * 唯一id
     */
    private String uuid;
}

