# 手把手带你写一个精简版 HashMap 的 get 方法

## 前言

HashMap 大家工作中遇到的太多了，已经成了必须使用的类了， 在面试的时候 HashMap 基本是必问题，但是很多同学只是打开看过原理，没有真正的去研究过。

里面是大佬写代码，为了性能和我们的业务代码写法差别很大，今天我带大家手写一个简单的 get 和 remove 方法，保证用大家看得懂的代码来写。

需要看 put 方法实现的可以点开首页自己查找。

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/hashmap/HashMapTest.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/hashmap/HashMapTest.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/hashmap/HashMapTest.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-test/src/test/java/com/github/zhuangjiaju/easytools/test/demo/hashmap/HashMapTest.java)

### HashMap 的 put 方法

没看过 `精简版 HashMap 的 put 方法` 的自己要先去看下，基础知识我就不重复讲解了。

get 方法比较简单，根据 key 的hash值取模，然后找到对应的数组的节点，然后去节点拿到链表一个个比较过去相等返回即可。

```java

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

```

测试案例:

```java
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

```

### HashMap 的 remove 方法

remove 方法比较简单，根据 key 的hash值取模，然后找到对应的数组的节点，然后移除掉链表上面的这个节点即可，当然移除的时候得分多种情况。

```java

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

```

测试案例:

```java
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

```

输出结果:

```text
20:27:36.525 [main] INFO com.github.zhuangjiaju.easytools.test.demo.hashmap.HashMapTest -- 放置后的数组:[{"hash":101943488,"key":"key12","value":"value12"},{"hash":101943489,"key":"key13","value":"value13"},{"hash":101943490,"key":"key14","value":"value14"},null,null,null,null,null,null,null,null,null,null,null,null,null,null,{"hash":3288497,"key":"key0","value":"value0"},{"hash":3288498,"key":"key1","value":"value1"},{"hash":3288499,"key":"key2","value":"value2"},{"hash":3288500,"key":"key3","value":"value3"},{"hash":3288501,"key":"key4","value":"value4"},{"hash":3288502,"key":"key5","value":"value5"},{"hash":3288503,"key":"key6","value":"value6"},{"hash":3288504,"key":"key7","value":"value7"},{"hash":3288505,"key":"key8","value":"value8"},{"hash":3288506,"key":"key9","value":"value9"},null,null,null,{"hash":101943486,"key":"key10","value":"value10"},{"hash":101943487,"key":"key11","value":"value11"}]
20:27:36.531 [main] INFO com.github.zhuangjiaju.easytools.test.demo.hashmap.HashMapTest -- 移除后的数组:[{"hash":101943488,"key":"key12","value":"value12"},{"hash":101943489,"key":"key13","value":"value13"},{"hash":101943490,"key":"key14","value":"value14"},null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,{"hash":101943486,"key":"key10","value":"value10"},{"hash":101943487,"key":"key11","value":"value11"}]

```

大家可以清晰的看到，本来15个数据，移除10个以后只剩下5个数据了。

### 总结

最近2次课程带大家手写了HashMap的put、get、remove 几个核心的方法，大家一定要自己去尝试写着试试，这样才不会忘记。

下一节会带大家看看真正的HashMap 源码，敬请等待。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
