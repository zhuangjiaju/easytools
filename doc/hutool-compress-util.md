# Java 中如何支持任意格式的压缩和解压缩

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

虽然Hutool基于JDK提供了`**ZipUtil**`
用于压缩或解压ZIP相关文件，但是对于7zip、tar等格式的压缩依旧无法处理，于是基于`**commons-compress**`
做了进一步封装：`**CompressUtil**`。
此工具支持的格式有：
对于流式压缩支持：

- GZIP
- BZIP2
- XZ
- XZ
- PACK200
- SNAPPY_FRAMED
- LZ4_BLOCK
- LZ4_FRAMED
- ZSTANDARD
- DEFLATE

对于归档文件支持：

- AR
- CPIO
- JAR
- TAR
- ZIP
- 7z

## 最佳实践

### 引入pom

```xml

<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <!-- 请查看最新版本 https://mvnrepository.com/artifact/cn.hutool/hutool-all -->
    <version>5.8.26</version>
</dependency>
```

### [压缩文件](https://www.hutool.cn/docs/#/extra/%E5%8E%8B%E7%BC%A9/%E5%8E%8B%E7%BC%A9%E5%B0%81%E8%A3%85-CompressUtil?id=%e5%8e%8b%e7%bc%a9%e6%96%87%e4%bb%b6)

我们以7Zip为例：

```java
final File file = FileUtil.file("d:/test/compress/test.7z");
CompressUtil.

createArchiver(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.SEVEN_Z, file)
    .

add(FileUtil.file("d:/test/someFiles"));
    .

finish()
    .

close();
```

其中`**ArchiveStreamFactory.SEVEN_Z**`就是自定义的压缩格式，可以自行选择
add方法同时支持文件或目录，多个文件目录多次调用add方法即可。
有时候我们不想把目录下所有的文件放到压缩包，这时候可以使用add方法的第二个参数`**Filter**`，此接口用于过滤不需要加入的文件。

```java
CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.SEVEN_Z, zipFile)
    .

add(FileUtil.file("d:/Java/apache-maven-3.6.3"), (file)->{
    if("invalid".

equals(file.getName())){
    return false;
    }
    return true;
    })
    .

finish().

close();
```

### [解压文件](https://www.hutool.cn/docs/#/extra/%E5%8E%8B%E7%BC%A9/%E5%8E%8B%E7%BC%A9%E5%B0%81%E8%A3%85-CompressUtil?id=%e8%a7%a3%e5%8e%8b%e6%96%87%e4%bb%b6)

我们以7Zip为例：

```java
Extractor extractor = CompressUtil.createExtractor(
    CharsetUtil.defaultCharset(),
    FileUtil.file("d:/test/compress/test.7z"));

extractor.

extract(FileUtil.file("d:/test/compress/test2/"));
```

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。   
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)   
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)
