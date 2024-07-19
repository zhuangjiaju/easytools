package com.github.zhuangjiaju.easytools.test.demo.hashmap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * Hash Map 的案例
 *
 * @author JiaJu Zhuang
 */
@Getter
public class MyHashMap<K, V> implements Map<K, V> {
    /**
     * 当前数量
     */
    private int size;
    /**
     * 为了方便 我们直接默认长度16的数组了
     */
    private MyHashMapNode<K, V>[] table = new MyHashMapNode[16];

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        // 为了简单期间不考虑 null
        // 放到table 数组的哪个位置 真正的hashmap 算法不一样 我们偷懒
        int hasCode = key.hashCode();
        int index = hasCode % table.length;
        // 我们找到了我们需要放的位置
        MyHashMapNode<K, V> node = table[index];
        // 这个位置没有数据 我们直接放进去即可 非常轻松
        if (node == null) {
            table[index] = MyHashMapNode.<K, V>builder()
                .hash(hasCode)
                .key(key)
                .value(value)
                .build();
        } else {
            // 已经有了 我们要放到最后一个node 的最后,所以需要一个个node 的遍历
            // 真正的HashMap 还会转红黑树，我们就么必要了
            MyHashMapNode<K, V> nextNode = node;
            while (true) {
                // 先判断 hash值是否一样 为了提高性能 无所谓性能可以直接比较 key 是否一样
                // 如果找到了key 一样 我们把他替换掉 然后退出
                if (nextNode.getHash() == hasCode && nextNode.getKey().equals(key)) {
                    // key 一样 我们直接替换
                    nextNode.setValue(value);
                    break;
                }

                // 如果没有下一个节点了 我们直接放到最后一个节点
                if (nextNode.getNext() == null) {
                    nextNode.setNext(MyHashMapNode.<K, V>builder()
                        .hash(hasCode)
                        .key(key)
                        .value(value)
                        .build());
                    break;
                }
                // 不为空 则继续往后找
                nextNode = nextNode.getNext();
            }

        }

        // 尝试重新扩容 当table 存储超过75%的时候 我们需要重新扩容，确保每次key 计算hash 值能直接命中，而不需要一个比较过去
        if (size > table.length * 0.75) {
            // 直接扩容成2倍
            MyHashMapNode<K, V>[] newTable = new MyHashMapNode[table.length * 2];
            // 遍历所有旧的数据
            // 所有的子节点都要迭代掉
            for (MyHashMapNode<K, V> oldNode : table) {
                // 空数据不管
                if (oldNode == null) {
                    continue;
                }
                // 旧的下一个节点
                MyHashMapNode<K, V> oldNodeNext = oldNode;

                // 这里核心是把数组+链表所有的数据都迭代出来
                while (true) {
                    // 直接获取下一个节点 这里要提早获取 以为要要把 oldNode的next 设置成null
                    oldNodeNext = oldNodeNext.getNext();

                    // 清空下一个节点 因为要重新挂到新的table 上 重新挂的时候会有新的next
                    oldNode.setNext(null);

                    // 重新计算数组下标
                    int newIndex = oldNode.getHash() % newTable.length;
                    MyHashMapNode<K, V> newNode = newTable[newIndex];
                    // 这个位置没有数据 我们直接放进去即可
                    if (newNode == null) {
                        newTable[newIndex] = oldNode;
                    } else {
                        // 已经有了 我们要放到最后一个node 的最后,所以需要一个个node 的遍历
                        // 真正的HashMap 还会转红黑树，我们就么必要了
                        MyHashMapNode<K, V> newNextNode = newNode;
                        while (true) {
                            // 如果没有下一个节点了 我们直接放到最后一个节点
                            if (newNextNode.getNext() == null) {
                                newNextNode.setNext(MyHashMapNode.<K, V>builder()
                                    .hash(hasCode)
                                    .key(key)
                                    .value(value)
                                    .build());
                                break;
                            }
                            // 不为空 则继续往后找
                            newNextNode = newNextNode.getNext();
                        }
                    }

                    // 最后一个节点了 结束循环
                    if (oldNodeNext == null) {
                        break;
                    }
                }
            }
            // 替换旧的table
            table = newTable;
        }
        // 条数加+1
        size++;
        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
