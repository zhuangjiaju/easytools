package cn.easytools.test.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 基础测试类
 * 不宜懒于spring的测试
 *
 * @author Jiaju Zhuang
 **/
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public abstract class SimpleBaseTest {

}
