# Java 图片缩放、裁剪、水印神器

## 前言

Hutool 是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

官网:[https://www.hutool.cn/](https://www.hutool.cn/)

### 推荐说明

针对awt中图片处理进行封装，这些封装包括：缩放、裁剪、转为黑白、加水印等操作。

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

### [scale缩放图片](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=scale-%e7%bc%a9%e6%94%be%e5%9b%be%e7%89%87)

提供两种重载方法：其中一个是按照长宽缩放，另一种是按照比例缩放。

```java
ImgUtil.scale(
    FileUtil.file("d:/face.jpg"), 
    FileUtil.

file("d:/face_result.jpg"), 
    0.5f//缩放比例
        );
```

### [cut剪裁图片](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=cut-%e5%89%aa%e8%a3%81%e5%9b%be%e7%89%87)

```java
ImgUtil.cut(
    FileUtil.file("d:/face.jpg"), 
    FileUtil.

file("d:/face_result.jpg"), 
    new

Rectangle(200,200,100,100)//裁剪的矩形区域
);
```

### [slice按照行列剪裁切片（将图片分为20行和20列）](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=slice-%e6%8c%89%e7%85%a7%e8%a1%8c%e5%88%97%e5%89%aa%e8%a3%81%e5%88%87%e7%89%87%ef%bc%88%e5%b0%86%e5%9b%be%e7%89%87%e5%88%86%e4%b8%ba20%e8%a1%8c%e5%92%8c20%e5%88%97%ef%bc%89)

```java
ImgUtil.slice(FileUtil.file("e:/test2.png"),FileUtil.

file("e:/dest/"), 10,10);
```

### [convert图片类型转换，支持GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG等](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=convert-%e5%9b%be%e7%89%87%e7%b1%bb%e5%9e%8b%e8%bd%ac%e6%8d%a2%ef%bc%8c%e6%94%af%e6%8c%81gif-gtjpg%e3%80%81gif-gtpng%e3%80%81png-gtjpg%e3%80%81png-gtgifx%e3%80%81bmp-gtpng%e7%ad%89)

```java
ImgUtil.convert(FileUtil.file("e:/test2.png"),FileUtil.

file("e:/test2Convert.jpg"));
```

### [gray彩色转为黑白](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=gray-%e5%bd%a9%e8%89%b2%e8%bd%ac%e4%b8%ba%e9%bb%91%e7%99%bd)

```java
ImgUtil.gray(FileUtil.file("d:/logo.png"),FileUtil.

file("d:/result.png"));
```

### [pressText添加文字水印](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=presstext-%e6%b7%bb%e5%8a%a0%e6%96%87%e5%ad%97%e6%b0%b4%e5%8d%b0)

```java
ImgUtil.pressText(//
    FileUtil.file("e:/pic/face.jpg"), //
    FileUtil.

file("e:/pic/test2_result.png"), //
    "版权所有",Color.WHITE, //文字
    new

Font("黑体",Font.BOLD, 100), //字体
    0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
        0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
        0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
        );
```

### [pressImage添加图片水印](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=pressimage-%e6%b7%bb%e5%8a%a0%e5%9b%be%e7%89%87%e6%b0%b4%e5%8d%b0)

```java
ImgUtil.pressImage(
    FileUtil.file("d:/picTest/1.jpg"), 
    FileUtil.

file("d:/picTest/dest.jpg"), 
    ImgUtil.

read(FileUtil.file("d:/picTest/1432613.jpg")), //水印图片
    0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
    0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
    0.1f
    );
```

### [rotate旋转图片](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=rotate-%e6%97%8b%e8%bd%ac%e5%9b%be%e7%89%87)

```java
// 旋转180度
BufferedImage image = ImgUtil.rotate(ImageIO.read(FileUtil.file("e:/pic/366466.jpg")), 180);
ImgUtil.

write(image, FileUtil.file("e:/pic/result.png"));
```

### [flip水平翻转图片](https://www.hutool.cn/docs/#/core/%E5%9B%BE%E7%89%87/%E5%9B%BE%E7%89%87%E5%B7%A5%E5%85%B7-ImgUtil?id=flip-%e6%b0%b4%e5%b9%b3%e7%bf%bb%e8%bd%ac%e5%9b%be%e7%89%87)

```java
ImgUtil.flip(FileUtil.file("d:/logo.png"),FileUtil.

file("d:/result.png"));
```

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护。
github地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)
gitee地址：[https://gitee.com/zhuangjiaju/easytools](https://gitee.com/zhuangjiaju/easytools)