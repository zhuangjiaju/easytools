package com.github.zhuangjiaju.easytools.test.demo.hashmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * HashMap 的 Node 节点
 *
 * @param <K>
 * @param <V>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MyHashMapNode<K, V> {
    /**
     * key 的hash 值
     */
    private int hash;
    /**
     * map 的key
     */
    private K key;
    /**
     * map 的value
     */
    private V value;
    /**
     * 下一个节点
     */
    private MyHashMapNode<K, V> next;
}
