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
        // 为了简单期间不考虑 null
        int hasCode = key.hashCode();
        int index = hasCode % table.length;
        // 我们找到了可能存储数据的位置
        MyHashMapNode<K, V> node = table[index];
        // 找不到数据
        if (node == null) {
            return null;
        }

        // 这里的数据是一个链表结构，我们需要一个个的遍历
        MyHashMapNode<K, V> tempNode = node;
        while (tempNode != null) {
            // 先判断 hash值是否一样 为了提高性能 无所谓性能可以直接比较 key 是否一样
            // 如果找到了key 一样 直接返回即可
            if (tempNode.getHash() == hasCode && tempNode.getKey().equals(key)) {
                return tempNode.getValue();
            }

            // 继续找下一个节点
            tempNode = tempNode.getNext();
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        // 为了简单期间不考虑 null
        int hasCode = key.hashCode();
        int index = hasCode % table.length;
        // 我们找到了可能存储数据的位置
        MyHashMapNode<K, V> node = table[index];
        // 找不到数据
        if (node == null) {
            return null;
        }

        // 这里的数据是一个链表结构，我们需要一个个的遍历
        MyHashMapNode<K, V> tempNode = node;
        // 上一个节点
        MyHashMapNode<K, V> previousNode = null;

        while (tempNode != null) {
            // 先判断 hash值是否一样 为了提高性能 无所谓性能可以直接比较 key 是否一样
            // 代表我们找到了要删除的节点
            if (tempNode.getHash() == hasCode && tempNode.getKey().equals(key)) {
                // 下一个为空了
                if (tempNode.getNext() == null) {
                    // 上一个节点也为空
                    // 代表这个节点是唯一的节点 直接是删除了
                    if (previousNode == null) {
                        table[index] = null;
                    } else {
                        // 上一个节点不为空 但是下一个节点为空 代表是最后一个节点
                        // 直接把上一个节点的下一个设置成null 这样子就是把最后一个节点也就是本节点删除了
                        previousNode.setNext(null);
                    }
                } else {
                    // 下一个不为空

                    // 上一个节点为空
                    // 代表这个节点是第一个节点
                    // 直接把table的index 设置成下一个节点 这样就把第一个节点也就是本节点删除了
                    if (previousNode == null) {
                        table[index] = tempNode.getNext();
                    } else {
                        // 上一个节点不为空 但是下一个节也不为空
                        // 直接把上一个节点指向下一个节点即可 这样就把中间的一个节点也就是本节点删除了
                        previousNode.setNext(tempNode.getNext());
                    }
                }
                size--;
                return tempNode.getValue();
            }

            // 记录上一个节点
            previousNode = tempNode;
            // 继续找下一个节点
            tempNode = tempNode.getNext();
        }
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
            MyHashMapNode<K, V> tempNode = node;
            while (true) {
                // 先判断 hash值是否一样 为了提高性能 无所谓性能可以直接比较 key 是否一样
                // 如果找到了key 一样 我们把他替换掉 然后退出
                if (tempNode.getHash() == hasCode && tempNode.getKey().equals(key)) {
                    // key 一样 我们直接替换
                    tempNode.setValue(value);
                    break;
                }

                // 如果没有下一个节点了 我们直接放到最后一个节点
                if (tempNode.getNext() == null) {
                    tempNode.setNext(MyHashMapNode.<K, V>builder()
                        .hash(hasCode)
                        .key(key)
                        .value(value)
                        .build());
                    break;
                }
                // 不为空 则继续往后找
                tempNode = tempNode.getNext();
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
                MyHashMapNode<K, V> oldTempNode = oldNode;

                // 旧的下一个节点的下一个节点
                MyHashMapNode<K, V> oldTempNodeNext = null;

                // 这里核心是把数组+链表所有的数据都迭代出来
                while (true) {

                    // 直接获取下一个节点 这里要提早获取 以为要要把 oldNode的next 设置成null
                    oldTempNodeNext = oldTempNode.getNext();

                    // 清空下一个节点 因为要重新挂到新的table 上 重新挂的时候会有新的next
                    oldTempNode.setNext(null);

                    // 重新计算数组下标
                    int newIndex = oldTempNode.getHash() % newTable.length;
                    MyHashMapNode<K, V> newNode = newTable[newIndex];
                    // 这个位置没有数据 我们直接放进去即可
                    if (newNode == null) {
                        newTable[newIndex] = oldTempNode;
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
                    if (oldTempNodeNext == null) {
                        break;
                    }
                    oldTempNode = oldTempNodeNext;
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
