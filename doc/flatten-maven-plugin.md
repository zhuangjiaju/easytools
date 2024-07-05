# java 开源项目是如何将多个 module 版本统一配置的？

## 前言

现在大部分开源项目也包括我们自己的项目，现都会创建model,然后每个model都会配置一个版本，在升级版本的时候需要一个个的去修改。

这里给大家看个例子：

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.github.zhuangjiaju</groupId>
        <artifactId>easytools</artifactId>
        <version>${revision}</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>easytools-tools</artifactId>
    <packaging>pom</packaging>
    <version>1.0.0-SNAPSHOT</version>

    <modules>
        <module>easytools-tools-base</module>
        <module>easytools-tools-common</module>
    </modules>

</project>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<parent>
    <groupId>com.github.zhuangjiaju</groupId>
    <artifactId>easytools</artifactId>
    <version>${revision}</version>
    <relativePath>../pom.xml</relativePath>
</parent>
<modelVersion>4.0.0</modelVersion>
<artifactId>easytools-domain</artifactId>
<packaging>pom</packaging>
<version>1.0.0-SNAPSHOT</version>


<modules>
    <module>easytools-domain-course</module>
    <module>easytools-domain-demo</module>
</modules>

</project>

```

可以看到每个model都都配置了`<version>1.0.0-SNAPSHOT</version>` ，升级版本的的时候一个改过去非常麻烦。有没有上面办法能配置到一个地方呢？

## 最佳实践

思路就是使用 `flatten-maven-plugin` 插件，这个插件可以配置配置一个变量，然后在 maven 的 `process-resources`
阶段生成一个临时是`.flattened-pom.xml`，这个文件已经将所有的版本改好了，然后maven真正处理的pom
用的就是`.flattened-pom.xml` ，这样就可以在一个地方配置版本，所有pom共享了。

### 直接上案例

案例地址： [https://github.com/zhuangjiaju/easytools/blob/main/pom.xml](https://github.com/zhuangjiaju/easytools/blob/main/pom.xml)

### 配置信息

在父`pom.xml` 加入如下配置

* 加入变量 `<revision>1.0.0-SNAPSHOT</revision>`
* 所有的需要使用版本的地方使用`${revision}` 变量来替换真正的版本
* 最后引入插件 `flatten-maven-plugin` 插件

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.github.zhuangjiaju</groupId>
    <artifactId>easytools</artifactId>
    <packaging>pom</packaging>
    <!-- 配置当前项目的版本使用${revision}变量来替换 -->
    <version>${revision}</version>
    <name>easytools</name>

    <properties>
        <!-- 当前项目的版本 用了flatten 仅修改这一个地方即可-->
        <revision>1.0.0-SNAPSHOT</revision>
        .....
    </properties>

    <modules>
        .....
    </modules>

    <dependencyManagement>
        <dependencies>
            <!-- 在maven管理包的时候 也可以使用${revision}来定义版本 -->
            <dependency>
                <groupId>com.github.zhuangjiaju</groupId>
                <artifactId>easytools-tools-common</artifactId>
                <version>${revision}</version>
            </dependency>
            .....
        </dependencies>
    </dependencyManagement>


    <build>
        <plugins>
            <!-- 支持maven revision 来配置统版本-->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>flatten-maven-plugin</artifactId>
                <version>1.6.0</version>
                <configuration>
                    <updatePomFile>true</updatePomFile>
                    <flattenMode>oss</flattenMode>
                </configuration>
                <executions>
                    <execution>
                        <id>flatten</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>flatten</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>flatten.clean</id>
                        <phase>clean</phase>
                        <goals>
                            <goal>clean</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

```

子`pom.xml`配置如下:

```xml 
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.github.zhuangjiaju</groupId>
        <artifactId>easytools</artifactId>
        <version>${revision}</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>easytools-tools</artifactId>
    <packaging>pom</packaging>

    <modules>
        <module>easytools-tools-base</module>
        <module>easytools-tools-common</module>
    </modules>

</project>

```

自动生成的`.flattened-pom.xml` 如下，已经给我们加好版本了。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.github.zhuangjiaju</groupId>
  <artifactId>easytools-tools</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>pom</packaging>
</project>

```

### 总结

通过以上案例，我们就只要修改 `revision` 一个值，全局的版本都会变了，是不是很方便呀？可以到自己的项目里面实战时试试吧。

## 写在最后

给大家推荐一个非常完整的Java项目搭建的最佳实践,也是本文的源码出处，由大厂程序员&EasyExcel作者维护，地址：[https://github.com/zhuangjiaju/easytools](https://github.com/zhuangjiaju/easytools)