# 史上最详细的 HashMap 的 get 方法的源码注释

## 前言

HashMap 大家工作中遇到的太多了，已经成了必须使用的类了， 在面试的时候 HashMap 基本是必问题，但是很多同学只是打开看过原理，没有真正的去研究过。

今天我们就一起来看看 HashMap 的 get/remove 方法的源码，保证你从来没见过这么详细注释的源码。通过这个文章你会了解如下问题：

* 完全熟悉 HashMap 的 get/remove 的原理
* HashMap 是怎么用最高的性能来实现移除操作的？

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java)

### HashMap 的 get 方法

没看过 `史上最详细的 HashMap 的 put 方法的源码注释` 的自己要先去看下，可以在主页找到。

直接调用 getNode

```java
    /**
 * Returns the value to which the specified key is mapped,
 * or {@code null} if this map contains no mapping for the key.
 *
 * <p>More formally, if this map contains a mapping from a key
 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
 * key.equals(k))}, then this method returns {@code v}; otherwise
 * it returns {@code null}.  (There can be at most one such mapping.)
 *
 * <p>A return value of {@code null} does not <i>necessarily</i>
 * indicate that the map contains no mapping for the key; it's also
 * possible that the map explicitly maps the key to {@code null}.
 * The {@link #containsKey containsKey} operation may be used to
 * distinguish these two cases.
 *
 * @see #put(Object, Object)
 */
// 去HashMap中取key 对应的value
// 如果key不存在 则返回null
public V get(Object key) {
    Node<K, V> e;
    // 直接调用 getNode 方法 然后返回 Node 的 value
    return (e = getNode(key)) == null ? null : e.value;
}
```

核心逻辑是定位到key 对应 table数组的下标，取出链表以后一个个比较过去，找到对应的值直接返回

```java
    /**
 * Implements Map.get and related methods.
 *
 * @param key the key
 * @return the node, or null if none
 */
final Node<K, V> getNode(Object key) {
    // tab 代表我们的table数组 用于存储整个HashMap的数据，用数据和链表的形式存储
    // first 代表我们的table数组找到指定链表以后的第一个节点
    // n 表示数组的长度
    // k 临时变量 存储各种情况的key
    Node<K, V>[] tab;
    Node<K, V> first, e;
    int n, hash;
    K k;
    // 先判断 tab是否为null 这个代表重来没放过数据 是的话直接返回null
    // tab.length > 0 代表数据已经初始化了
    // 赋值 first 为我们找到指定链表以后的第一个节点 如果为null 也直接返回null
    // 怎么获取指定链表以后的第一个节点的 参考本文件开头注释 问题1
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & (hash = hash(key))]) != null) {
        // 首先比较数组的里面的链表的第一个节点和我们要获取的节点是否相等，相等直接返回了
        // 为什么要第一个节点 单独拿出来？
        // 因为HashMap 里面 table 的链表散列做的特别好，绝大部分都只有1个节点，所以单独拿出来性能最好
        // 为什么要先比较 hash， 再== 比较，最后再equals ，搞这么麻烦？
        // 还是为了性能 equals的性能相对较差，所以节点比较会先比较 hash 值，然后再==比较 ，尽可能少的使用 equals 去比较
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        // 链表还有下一个节点 我们需要继续循环
        if ((e = first.next) != null) {
            // 红黑树 这里不多讲了 有兴趣的自己去搜索下
            if (first instanceof TreeNode)
                return ((TreeNode<K, V>)first).getTreeNode(hash, key);
            // 循环所有节点
            do {
                // 判断节点相等 则直接返回
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
                // 将 e 指向下一个节点
                // 直到e的下一个节点为空
            } while ((e = e.next) != null);
        }
    }
    return null;
}

```

### HashMap 的 remove 方法

直接调用了 removeNode 方法

```java
    /**
 * Removes the mapping for the specified key from this map if present.
 *
 * @param  key key whose mapping is to be removed from the map
 * @return the previous value associated with {@code key}, or
 *         {@code null} if there was no mapping for {@code key}.
 *         (A {@code null} return can also indicate that the map
 *         previously associated {@code null} with {@code key}.)
 */
// 去HashMap中 移除指定key 的value
// 如果key存在 则返回被移除key 指定的value
// 如果key不存在 则返回null
public V remove(Object key) {
    Node<K, V> e;
    // 获取hash 值以后直接调用 removeNode 去移除节点
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}
```

核心逻辑是定位到key 对应 table数组的下标，取出链表以后一个个比较过去，找到对应链表的Node以后，直接将这个Node的父节点指向这个Node的子节点，实现删除逻辑

```java
    /**
 * Implements Map.remove and related methods.
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to match if matchValue, else ignored
 * @param matchValue if true only remove if value is equal
 * @param movable if false do not move other nodes while removing
 * @return the node, or null if none
 */
// 移除指定节点
final Node<K, V> removeNode(int hash, Object key, Object value,
    boolean matchValue, boolean movable) {
    // tab 代表我们的table数组 用于存储整个HashMap的数据，用数据和链表的形式存储
    // p 当前节点 在每个数组上面存储了 Node, 并且Node是一个链表结构，他的next 表示链表的下一个
    // n 表示数组的长度
    // index 表示当前链表所在table数组的位置
    Node<K, V>[] tab;
    Node<K, V> p;
    int n, index;
    // 先判断 tab是否为null 这个代表重来没放过数据 是的话直接返回null
    // tab.length > 0 代表数据已经初始化了
    // 赋值 p 为我们找到指定链表以后的第一个节点 如果为null 也直接返回null
    // 怎么获取指定链表以后的第一个节点的 参考本文件开头注释 问题1
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        // node 临时变量 存储最后我们需要返回的Node节点
        // e 临时变量 存储Node节点
        // k 临时变量 存储各种情况的key
        // v 临时变量 存储各种情况的value
        Node<K, V> node = null, e;
        K k;
        V v;
        // 首先比较数组的里面的链表的第一个节点和我们要获取的节点是否相等，相等直接返回了
        // 为什么要第一个节点 单独拿出来？
        // 因为HashMap 里面 table 的链表散列做的特别好，绝大部分都只有1个节点，所以单独拿出来性能最好
        // 为什么要先比较 hash， 再== 比较，最后再equals ，搞这么麻烦？
        // 还是为了性能 equals的性能相对较差，所以节点比较会先比较 hash 值，然后再==比较 ，尽可能少的使用 equals 去比较
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
            // 链表还有下一个节点 我们需要继续循环
        else if ((e = p.next) != null) {
            // 红黑树 这里不多讲了 有兴趣的自己去搜索下
            if (p instanceof TreeNode)
                node = ((TreeNode<K, V>)p).getTreeNode(hash, key);
            else {
                // 循环所有节点
                do {
                    // 判断节点相等 跳出循环
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                            (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    // p 代表我们最终需要返回变量的父节点
                    // e 代表我们循环的节点
                    p = e;
                    // 将 e 指向下一个节点
                    // 直到e的下一个节点为空
                } while ((e = e.next) != null);
            }
        }
        // node 代表我们最终匹配到的节点
        // p 代表我们最终匹配到的节点的父节点
        // matchValue 代表只有value相等的时候才移除 默认为false 所以一般我们不管value是否相等都会移除
        if (node != null && (!matchValue || (v = node.value) == value ||
            (value != null && value.equals(v)))) {
            // 红黑树 这里不多讲了 有兴趣的自己去搜索下
            if (node instanceof TreeNode)
                ((TreeNode<K, V>)node).removeTreeNode(this, tab, movable);
                // 最终找到的节点 = 父节点
                // p 默认设置的就是第一个节点，所以代表我们要移除的节点就是第一个节点
                // 不管下一个节点是否为null 将table 数组的当前位置设置成第一个节点的下一个节点即可
            else if (node == p)
                tab[index] = node.next;
                // 代表不是第一个节点
                // 所以只要把父节点指向当前节点的下一个节点即可
            else
                p.next = node.next;
            // 操作次数+1
            ++modCount;
            // 大小-1
            --size;
            // 回调节点删除成功函数 没啥用
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```

### 总结

今天带大家看了 HashMap get/remove 方法的源码，大家需要花点时间认真去看下，讲解的非常详细，需要静下心来仔细阅读。

大家如果需要注释的源码，可以看文章开头的地址去获取。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
