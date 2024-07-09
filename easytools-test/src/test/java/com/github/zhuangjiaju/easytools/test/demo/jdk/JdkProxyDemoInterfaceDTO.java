package com.github.zhuangjiaju.easytools.test.demo.jdk;

/**
 * jdk动态代理的接口
 * 这里和 Cglib 不同，必须有接口 不然无法使用
 *
 * @author Jiaju Zhuang
 */
public interface JdkProxyDemoInterfaceDTO {

    String getName();

    void setName(String name);
}
