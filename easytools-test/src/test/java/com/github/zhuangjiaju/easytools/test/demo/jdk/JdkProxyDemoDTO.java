package com.github.zhuangjiaju.easytools.test.demo.jdk;

/**
 * jdk动态代理的案例
 *
 * @author Jiaju Zhuang
 */
public class JdkProxyDemoDTO implements JdkProxyDemoInterfaceDTO {
    /**
     * 名字
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
