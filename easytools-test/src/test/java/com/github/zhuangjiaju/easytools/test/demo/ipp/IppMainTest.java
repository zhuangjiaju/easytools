package com.github.zhuangjiaju.easytools.test.demo.ipp;

import lombok.extern.slf4j.Slf4j;

/**
 * 用 ASM Bytecode Viewer 插件来查看字节码
 * main 方法测试
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class IppMainTest {

    public static void main(String[] args) {
        int i = 0;
        System.out.println("--1--");
        // IINC 1 1  将指定int型变量增加指定值 线程不安全
        i++;
        System.out.println("--2--");
        //ILOAD 1 将指定的int型本地变量推送至栈顶
        //ICONST_1 将int型1推送至栈顶
        //IADD 将栈顶两int型数值相加并将结果压入栈顶
        //ISTORE 1 	将栈顶int型数值存入指定本地变量
        i = i + 1;
        System.out.println("--3--");
    }
}
