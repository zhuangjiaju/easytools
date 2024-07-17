package com.github.zhuangjiaju.easytools.web.common.mapper;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.user.UserDTO;
import com.github.zhuangjiaju.easytools.web.common.controller.user.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * 结果查询返回
 *
 * @author Jiaju Zhuang
 */
@Mapper(componentModel = "spring")
public abstract class BaseCommonWebMapper {

    /**
     * 用户对象转成vo
     *
     * @param user
     * @return
     */
    public abstract UserVO _userDto2UserVo(UserDTO user);
}

