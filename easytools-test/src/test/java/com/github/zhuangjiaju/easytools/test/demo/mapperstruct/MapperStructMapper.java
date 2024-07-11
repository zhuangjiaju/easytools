package com.github.zhuangjiaju.easytools.test.demo.mapperstruct;

import com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsDemoDTO;
import org.mapstruct.Mapper;

@Mapper
public interface MapperStructMapper {

    BeanUtilsDemoDTO dto2dto(BeanUtilsDemoDTO dto);

}
