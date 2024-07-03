package com.github.zhuangjiaju.easytools.tools.base.wrapper;

import java.io.Serializable;

import com.github.zhuangjiaju.easytools.tools.base.constant.EasyToolsConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装一个对象
 *
 * @author Jiaju Zhuang
 */
@Getter
@Setter
public class ObjectWrapper<T> implements Serializable {
    private static final long serialVersionUID = EasyToolsConstant.SERIAL_VERSION_UID;

    private T value;

    public ObjectWrapper(T value) {
        this.value = value;
    }

    public static <T> ObjectWrapper<T> of(T value) {
        return new ObjectWrapper<>(value);
    }

    public static <T> ObjectWrapper<T> ofNull() {
        return new ObjectWrapper<>(null);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
