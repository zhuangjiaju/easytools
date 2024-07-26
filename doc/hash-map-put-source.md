# 史上最详细的 HashMap 的 put 方法的源码注释

## 前言

HashMap 大家工作中遇到的太多了，已经成了必须使用的类了， 在面试的时候 HashMap 基本是必问题，但是很多同学只是打开看过原理，没有真正的去研究过。

今天我们就一起来看看 HashMap 的 put 方法的源码，保证你从来没见过这么详细注释的源码。通过这个文章你会了解如下问题：

* 完全 HashMap 的 put 的原理
* 为什么 HashMap 里面 table 数组长度一定是2的次幂？
* HashMap 是如何 resize table 数组的？做了哪些性能优化？

## 最佳实践

### 直接上案例

案例地址GitHub： [https://github.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java](https://github.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java)

案例地址gitee： [https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java](https://gitee.com/zhuangjiaju/easytools/blob/main/easytools-source-code/java21/src/main/java/java/util/HashMap.java)

### 首先我们看 HashMap 的构造方法

实际上他什么都没做 所以new HashMap() 的时候并不会初始化 table 数组，性能也是杠杠的。

```java
    /**
 * Constructs an empty {@code HashMap} with the default initial capacity
 * (16) and the default load factor (0.75).
 */
// HashMap 的构造方法 为了性能他什么都不做
// 在第一次put 的时候会初始化
public HashMap() {
    // 加载因子 在数组中数据占比超过这个值 会进行数组扩容
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```

### 打开 put 方法看看

直接调用了 putVal

```java
   /**
 * Associates the specified value with the specified key in this map.
 * If the map previously contained a mapping for the key, the old
 * value is replaced.
 *
 * @param key key with which the specified value is to be associated
 * @param value value to be associated with the specified key
 * @return the previous value associated with {@code key}, or
 *         {@code null} if there was no mapping for {@code key}.
 *         (A {@code null} return can also indicate that the map
 *         previously associated {@code null} with {@code key}.)
 */
// 往HashMap中放入一个值 可以放入null
// 如果key已经存在 则会覆盖原来的值 ，且返回原来的值
// 如果key不存在 则会插入数据 ，且返回null
public V put(K key, V value) {
    // 计算key的hash值 并调用putVal方法
    return putVal(hash(key), key, value, false, true);
}
```

Node节点，用于放到 table 数组里面：

```java
   /**
 * Basic hash bin node, used for most entries.  (See below for
 * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
 *
 */
// HashMap 的节点。 table数组上面存储的就是当前节点
static class Node<K, V> implements Map.Entry<K, V> {
    // 当前节点 key 的hash 值 ，存储了下来方便不用每次计算
    final int hash;
    // HashMap的 key
    final K key;
    // HashMap 的 value
    V value;
    // 下一个节点，是一个列表结构
    Node<K, V> next;

    Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey() {return key;}

    public final V getValue() {return value;}

    public final String toString() {return key + "=" + value;}

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;

        return o instanceof Map.Entry<?, ?> e
            && Objects.equals(key, e.getKey())
            && Objects.equals(value, e.getValue());
    }
}
```

这里贴一个比较棘手的问题： HashHap 是如何计算对应数组的下标的？

```java
/**
 * 问题1. HashHap 是如何计算对应数组的下标的？
 * 用  (n -1) & hash ，他的效果约等于 hash % n ，但是效率更好。
 * 为什么 (n -1) & hash 就能计算出对应数组的下标呢？
 * 这个设计的非常巧妙，HashMap在扩容和设置容量的时候，都会确保 capacity 也就是数组的容量一定是2的N次方。
 * 假设 n=16 那么 n-1 = 15 =
 * n-1  = 00000000 00000000 00000000 00001111
 * hash = 00000000 00000000 00000000 00010010 这里的hash是一个任意的int 类型
 * 最终 2个做与运算， 高位一定是0 ，所以只要看最后四位即可，实际效果和 hash % 16 一样
 *
 * @param <K>
 * @param <V>
 */
```

这个是最最核心的方案，注释写很详细。核心是往 table 数组里面 放入Node 节点。

```java
   /**
 * Implements Map.put and related methods.
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to put
 * @param onlyIfAbsent if true, don't change existing value
 * @param evict if false, the table is in creation mode.
 * @return previous value, or null if none
 */
// 往HashMap 放入一个值
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
    boolean evict) {
    // tab 代表我们的table数组 用于存储整个HashMapd的数据，用数据和链表的形式存储
    // p 当前节点 在每个数组上面存储了 Node, 并且Node是一个链表结构，他的next 表示链表的下一个
    // n 表示数组的长度
    // i 表示当前节点的位置
    Node<K, V>[] tab;
    Node<K, V> p;
    int n, i;
    // 如果table为空 或者 table的长度为0 则进行初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        // 这里面的 resize 实际上是初始化table数组
        n = (tab = resize()).length;
    // 计算当前节点可以放到table数组的哪个位置
    // 怎么计算的 参考 参考本文件开头注释 问题1
    // p==null 代表table数组的这个位置为空 直接新建一个node ，放入即可
    // 这个时候链表长度为1
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // 表table数组的这个位置不为空 所以我们要操作链表了
        // e 一个临时变量 用于存放节点
        // k 一个临时变量 用于存储节点的key
        Node<K, V> e;
        K k;
        // 首先比较数组的里面的链表的第一个节点和我们要放入的节点是否相等
        // 为什么要第一个节点 单独拿出来？
        // 因为HashMap 里面 table 的链表散列做的特别好，绝大部分都只有1个节点，所以单独拿出来性能最好
        // 为什么要先比较 hash， 再== 比较，最后再equals ，搞这么麻烦？
        // 还是为了性能 equals的性能相对较差，所以节点比较会先比较 hash 值，然后再==比较 ，尽可能少的使用 equals 去比较
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
            // 红黑树 这里不多讲了 有兴趣的自己去搜索下
        else if (p instanceof TreeNode)
            e = ((TreeNode<K, V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 第一个节点匹配不上，根据链表一个个往下找
            for (int binCount = 0; ; ++binCount) {
                // 下一个节点为空 代表链表结束了
                // 这里注意 e 变成了链表的下一个节点
                if ((e = p.next) == null) {
                    // 直接把节点挂到链表的最后一个节点即可
                    p.next = newNode(hash, key, value, null);
                    // 链表节点超过8个 转红黑色数
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 如果找到了相同的key 则直接跳出循环
                // 这个时候 e 是链表的下一个节点
                // p 是链表的当前节点
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                // 没有找到尾部 也没找打相同的
                // 把p设置成下一个节点 继续循环
                p = e;
            }
        }
        // e 不为空 代表 我们找到了相同的key
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            // onlyIfAbsent 默认false
            if (!onlyIfAbsent || oldValue == null)
                // 所以默认都是将新的value 覆盖旧的value
                e.value = value;
            // 回调节点插入成功函数 没啥用
            afterNodeAccess(e);
            // 替换成功 返回旧的value
            return oldValue;
        }
    }
    // 操作次数+1
    ++modCount;
    // 大小+1 ，如果超过阀值 则进行扩容
    if (++size > threshold)
        // 扩容
        resize();
    // 回调节点插入成功函数 没啥用
    afterNodeInsertion(evict);
    // 返回为空 代表新插入了节点，而不是修改了节点的内容
    return null;
}
```

### resize 方法我们要单独拿出来看看

核心是table数组长度*2 ，并迁移数据。但是做了非常多的细节优化。非常值得一读的源码。

```java
/**
 * Initializes or doubles table size.  If null, allocates in
 * accord with initial capacity target held in field threshold.
 * Otherwise, because we are using power-of-two expansion, the
 * elements from each bin must either stay at same index, or move
 * with a power of two offset in the new table.
 *
 * @return the table
 */
// 重新计算 table 数组大小
// 2个作用
// 1. 初始化table 数组
// 2. table数组长度*2 ，并迁移数据。
// 原因是HashMap是数组+链表的形式存储数据,如果所有数据放到几个数组的位置上面，会导致链表很长，所以需要重新计算数组大小，确保数组上面存储的数据足够散列
final Node<K, V>[] resize() {
    // 旧的数组
    Node<K, V>[] oldTab = table;
    // 旧数组的长度
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    // 旧的阀值 超过阀值会进行扩容
    int oldThr = threshold;
    // newCap 新在数组的长度
    // newThr 新的阀值
    int newCap, newThr = 0;
    // 如果旧的数组长度大于0 代表不是初始化
    if (oldCap > 0) {
        // 超过数组的最大长度 则取  Integer.MAX_VALUE
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 新的数组长度 等于 旧的长度左移1位 相等于 乘以2
        // 新的阀值也一样 旧的乘以2
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
            oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    } else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {
        // zero initial threshold signifies using defaults
        // 代表是初始化 取默认值即可
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 如果新的阀值为0 则计算新的阀值
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
            (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes", "unchecked"})
    // 新table 数组 用于存储所有的数据
    Node<K, V>[] newTab = (Node<K, V>[])new Node[newCap];
    table = newTab;
    // 旧的不为空 扩容的时候都不为空
    if (oldTab != null) {
        // 迭代旧的 table 数组的所有数据
        for (int j = 0; j < oldCap; ++j) {
            // 旧的数组上面的一个节点
            Node<K, V> e;
            // 节点不为空 则需要进行迁移
            // 为空的情况是因为 数据 在  table 数组是不满的，也就是说 table 数组 有些地方是没有数据的
            if ((e = oldTab[j]) != null) {
                // 清空旧的数组 我的理解是了方便GC
                oldTab[j] = null;
                // 下一节为空 代表链表只有一个节点，直接迁移即可
                if (e.next == null)
                    // 为啥新的位置直接可以放了？不怕冲突？
                    // 必须 先搞懂为 e.hash & (newCap - 1) 能计算出table数组的位置
                    // 参考本文件开头注释 问题1
                    // 好了,我们看懂了 e.hash & (newCap - 1) 能计算出table数组的位置，由于扩容一定是2倍扩容，
                    // 所以计算出来的位置 只有2个地方要么是原来的位置，要么是原来的位置+oldCap ,所以不可能冲突，所以直接放就行
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    // 红黑树 这里不多讲了 有兴趣的自己去搜索下
                    ((TreeNode<K, V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    // 看懂 文件开头注释 问题1 再来看这个
                    // 我们知道了 所有的数据只会放到原来的位置 或者 原来的位置+oldCap
                    // loHead 原来的位置的头节点
                    // loTail 原来的位置的尾节点
                    Node<K, V> loHead = null, loTail = null;
                    // hiHead 原来的位置+oldCap的头节点
                    // hiTail 原来的位置+oldCap的尾节点
                    Node<K, V> hiHead = null, hiTail = null;
                    // 链表的下一个节点
                    Node<K, V> next;
                    do {
                        // 链表的下一个节点赋值
                        next = e.next;
                        // 如果hash值和旧的容量进行与运算等于0 代表在原来的位置
                        if ((e.hash & oldCap) == 0) {
                            //  原来的位置的头节点 为空 则放入当前节点
                            if (loTail == null)
                                loHead = e;
                            else
                                //  原来的位置的头节点 不为空 则放入到尾节点的next
                                loTail.next = e;
                            // 尾节点等于当前节点
                            loTail = e;
                        } else {
                            // 代表放到 原来的位置+oldCap
                            //  原来的位置+oldCap 的头节点 为空 则放入当前节点
                            if (hiTail == null)
                                hiHead = e;
                            else
                                //   原来的位置+oldCap 的头节点 不为空 则放入到尾节点的next
                                hiTail.next = e;
                            // 尾节点等于当前节点
                            hiTail = e;
                        }
                        // 一直将链表的下一个节点赋值给当前节点 直到下一个节点为空
                    } while ((e = next) != null);

                    if (loTail != null) {
                        // 原来的位置的尾节点 可能还指向旧的节点 所以直接清空
                        loTail.next = null;
                        // 放入新的table数组 中的 原来的位置 放入 原来的位置的头结点
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        // 原来的位置+oldCap 的尾节点 可能还指向旧的节点 所以直接清空
                        hiTail.next = null;
                        // 放入新的table数组 中的 原来的位置+oldCap 放入 原来的位置+oldCap的头结点
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

### 总结

今天带大家看了 HashMap put 方法的源码，大家需要花点时间认真去看下，讲解的非常详细，需要静下心来仔细阅读。

大家如果需要注释的源码，可以看文章开头的地址去获取。

下一节会给大家讲解 HashMap 的 get 方法的源码，大家敬请期待。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
