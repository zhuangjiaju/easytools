package com.github.zhuangjiaju.easytools.test.demo.hashmap;

import com.alibaba.fastjson2.JSON;

import com.github.zhuangjiaju.easytools.test.common.SimpleBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Hash Map 的案例
 *
 * @author JiaJu Zhuang
 */
@Slf4j
public class HashMapTest extends SimpleBaseTest {
    /**
     * 侧测试我们自己写的HashMap
     * 在里面放2个值 看看效果
     */
    @Test
    public void putTest() throws Exception {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        myHashMap.put("a", "a");
        myHashMap.put("b", "b");

        log.info("放置后的数组:{}", JSON.toJSONString(myHashMap.getTable()));
        Assertions.assertEquals(2, myHashMap.getSize());

        // 迭代所有节点
        for (MyHashMapNode<String, String> node : myHashMap.getTable()) {
            if (node == null) {
                continue;
            }
            Assertions.assertTrue(StringUtils.equalsAny(node.getKey(), "a", "b"));
            Assertions.assertTrue(StringUtils.equalsAny(node.getValue(), "a", "b"));
        }
    }

    /**
     * 测试扩容的代码
     * 我们放15个值 看看效果，会触发一次扩容
     */
    @Test
    public void resizeTest() throws Exception {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        // 放入15条 理论上来扩容过一次
        for (int i = 0; i < 15; i++) {
            myHashMap.put("key" + i, "value" + i);
        }
        log.info("放置后的数组:{}", JSON.toJSONString(myHashMap.getTable()));
        Assertions.assertEquals(15, myHashMap.getSize());
        Assertions.assertEquals(32, myHashMap.getTable().length);
    }

    /**
     * 测试 HashMap 的 get 方法
     */
    @Test
    public void getTest() throws Exception {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        // 放入15条 理论上来扩容过一次
        for (int i = 0; i < 15; i++) {
            myHashMap.put("key" + i, "value" + i);
        }
        log.info("放置后的数组:{}", JSON.toJSONString(myHashMap.getTable()));
        Assertions.assertEquals(15, myHashMap.getSize());
        Assertions.assertEquals(32, myHashMap.getTable().length);
        // 20条数据测试下
        for (int i = 0; i < 15; i++) {
            Assertions.assertEquals("value" + i, myHashMap.get("key" + i));
        }

        Assertions.assertEquals(15, myHashMap.getSize());
        Assertions.assertEquals(32, myHashMap.getTable().length);
    }

    /**
     * 测试 HashMap 的 remove 方法
     */
    @Test
    public void removeTest() throws Exception {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        // 放入15条
        for (int i = 0; i < 15; i++) {
            myHashMap.put("key" + i, "value" + i);
        }
        log.info("放置后的数组:{}", JSON.toJSONString(myHashMap.getTable()));
        Assertions.assertEquals(15, myHashMap.getSize());
        Assertions.assertEquals(32, myHashMap.getTable().length);
        // 移除10条数据
        for (int i = 0; i < 10; i++) {
            myHashMap.remove("key" + i);
        }
        log.info("移除后的数组:{}", JSON.toJSONString(myHashMap.getTable()));

        Assertions.assertEquals(5, myHashMap.getSize());
        Assertions.assertEquals(32, myHashMap.getTable().length);
    }
}
