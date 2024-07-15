package com.github.zhuangjiaju.easytools.test.demo.mapperstruct;

import com.github.zhuangjiaju.easytools.test.demo.beanutils.BeanUtilsDemoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperStructMapper {

    /**
     * 固定写法 不用管 获取一个 MapperStructMapper 实例
     */
    MapperStructMapper INSTANCE = Mappers.getMapper(MapperStructMapper.class);

    BeanUtilsDemoDTO dto2dto(BeanUtilsDemoDTO dto);

    /**
     * 将 source 转换为 target
     *
     * @param source
     * @return
     */
    @Mappings({
        // id 转存 id 我们实际上可以不写 他自己会转换
        @Mapping(target = "id", source = "id"),
        // lastName+ firstName 转存 name ,需要我们自己写表达式
        @Mapping(target = "name", expression = "java(source.getLastName()+source.getFirstName())"),
        // age 转存 age 我们就不写了 一样的他自己会转换
        //@Mapping(target = "age", source = "age"),
        // email 我们不想要给我转换
        @Mapping(target = "email", ignore = true),
    })
    MapperStructTargetDTO dto2dto(MapperStructSourceDTO source);

}
