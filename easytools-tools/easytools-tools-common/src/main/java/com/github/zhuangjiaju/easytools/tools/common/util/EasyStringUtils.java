package com.github.zhuangjiaju.easytools.tools.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * String工具类
 *
 * @author Jiaju Zhuang
 */
public class EasyStringUtils {

    /**
     * 将多个字符串 拼接在一起
     *
     * @param delimiter 分隔符 不能为空
     * @param elements  字符串 可以为空 会忽略空的字符串
     * @return
     */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        if (elements == null) {
            return null;
        }
        List<CharSequence> charSequenceList = Arrays.stream(elements).filter(
            StringUtils::isNotBlank).collect(Collectors.toList());
        if (charSequenceList.isEmpty()) {
            return null;
        }
        return String.join(delimiter, charSequenceList);
    }

    /**
     * 限制一个string字符串的长度 ，超过长度 会用... 替换
     *
     * @param str    字符串
     * @param length 限制长度
     * @return
     */
    public static String limitString(String str, int length) {
        if (Objects.isNull(str)) {
            return null;
        }
        String limitString = StringUtils.substring(str, 0, length);
        if (limitString.length() == length) {
            limitString += "...";
        }
        return limitString;
    }

    /**
     * 根据冒号拼接在一起
     *
     * @param objs 对象
     * @return 拼接完成的数据
     */
    public static String joinWithColon(Object... objs) {
        return StringUtils.join(objs, ":");
    }

}
