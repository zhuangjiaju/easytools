package com.github.zhuangjiaju.easytools.tools.base.enums;

/**
 * 排序方向的枚举
 *
 * @author Jiaju Zhuang
 */
public enum OrderByDirectionEnum implements BaseEnum<String> {

    /**
     * 升序
     */
    ASC,
    /**
     * 降序
     */
    DESC;

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
