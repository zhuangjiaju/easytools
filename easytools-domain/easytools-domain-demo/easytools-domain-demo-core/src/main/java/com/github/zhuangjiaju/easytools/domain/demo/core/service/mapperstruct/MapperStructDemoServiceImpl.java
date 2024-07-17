package com.github.zhuangjiaju.easytools.domain.demo.core.service.mapperstruct;

import com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct.MapperStructDTO;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.mapperstruct.MapperStructDemoService;
import com.github.zhuangjiaju.easytools.domain.demo.api.service.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * MapperStruct 服务
 *
 * @author Jiaju Zhuang
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MapperStructDemoServiceImpl implements MapperStructDemoService {

    @Override
    public MapperStructDTO queryExistent(Long id) {
        return MapperStructDTO.builder()
            .id(id)
            .userIdJson("[\"1\",\"2\"]")
            .createUser(UserDTO.builder()
                .id(1L)
                .name("Jiaju Zhuang")
                .build())
            .build();
    }
}

