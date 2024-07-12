# 用 Hutool 一行代码搞定压缩、解压缩

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

用java 代码压缩解压缩还是挺麻烦的，但是用了 ZipUtil 只要一行代码就能搞定压缩、解压缩了。

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

### [Zip](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8E%8B%E7%BC%A9%E5%B7%A5%E5%85%B7-ZipUtil?id=zip)
1. 压缩

`**ZipUtil.zip**` 方法提供一系列的重载方法，满足不同需求的压缩需求，这包括：

- 打包到当前目录（可以打包文件，也可以打包文件夹，根据路径自动判断）
```java
//将aaa目录下的所有文件目录打包到d:/aaa.zip
ZipUtil.zip("d:/aaa");
```

- 指定打包后保存的目的地，自动判断目标是文件还是文件夹
```java
//将aaa目录下的所有文件目录打包到d:/bbb/目录下的aaa.zip文件中
// 此处第二个参数必须为文件，不能为目录
ZipUtil.zip("d:/aaa", "d:/bbb/aaa.zip");

//将aaa目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip");
```

- 可选是否包含被打包的目录。比如我们打包一个照片的目录，打开这个压缩包有可能是带目录的，也有可能是打开压缩包直接看到的是文件。zip方法增加一个boolean参数可选这两种模式，以应对众多需求。
```java
//将aaa目录以及其目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip", true);
```

- 多文件或目录压缩。可以选择多个文件或目录一起打成zip包。
```java
ZipUtil.zip(FileUtil.file("d:/bbb/ccc.zip"), false, 
    FileUtil.file("d:/test1/file1.txt"),
    FileUtil.file("d:/test1/file2.txt"),
    FileUtil.file("d:/test2/file1.txt"),
    FileUtil.file("d:/test2/file2.txt")
);
```

1. 解压

`**ZipUtil.unzip**` 解压。同样提供几个重载，满足不同需求。
```java
//将test.zip解压到e:\\aaa目录下，返回解压到的目录
File unzip = ZipUtil.unzip("E:\\aaa\\test.zip", "e:\\aaa");
```
### [Gzip](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8E%8B%E7%BC%A9%E5%B7%A5%E5%85%B7-ZipUtil?id=gzip)
Gzip是网页传输中广泛使用的压缩方式，Hutool同样提供其工具方法简化其过程。
`**ZipUtil.gzip**` 压缩，可压缩字符串，也可压缩文件 `**ZipUtil.unGzip**` 解压Gzip文件
### [Zlib](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8E%8B%E7%BC%A9%E5%B7%A5%E5%85%B7-ZipUtil?id=zlib)
`**ZipUtil.zlib**` 压缩，可压缩字符串，也可压缩文件 `**ZipUtil.unZlib**` 解压zlib文件
**注意 ZipUtil默认情况下使用系统编码，也就是说：**

1. 如果你在命令行下运行，则调用系统编码（一般Windows下为GBK、Linux下为UTF-8）
2. 如果你在IDE（如Eclipse）下运行代码，则读取的是当前项目的编码（详细请查阅IDE设置，我的项目默认都是UTF-8编码，因此解压和压缩都是用这个编码）
### [常见问题](https://www.hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%8E%8B%E7%BC%A9%E5%B7%A5%E5%85%B7-ZipUtil?id=%e5%b8%b8%e8%a7%81%e9%97%ae%e9%a2%98)

1. 解压时报`**java.lang.IllegalArgumentException:MALFORMED**`错误

基本是因为编码问题，Hutool默认使用UTF-8编码，自定义为其他编码即可（一般为GBK）。
```java
//将test.zip解压到e:\\aaa目录下，返回解压到的目录
File unzip = ZipUtil.unzip("E:\\aaa\\test.zip", "e:\\aaa", CharsetUtil.CHARSET_GBK);
```

1. 压缩并添加密码

Hutool或JDK的Zip工具并不支持添加密码，可以考虑使用[Zip4j](https://github.com/srikanth-lingala/zip4j)完成，以下代码来自Zip4j官网。
```java
ZipParameters zipParameters = new ZipParameters();
zipParameters.setEncryptFiles(true);
zipParameters.setEncryptionMethod(EncryptionMethod.AES);
// Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256); 

List<File> filesToAdd = Arrays.asList(
    new File("somefile"), 
    new File("someotherfile")
);

ZipFile zipFile = new ZipFile("filename.zip", "password".toCharArray());
zipFile.addFiles(filesToAdd, zipParameters);
```


## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)