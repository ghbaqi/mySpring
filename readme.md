# 开发 javaWeb 框架 #
    自己手写一个 Javaweb 框架，仿 spring 和 springMvc 实现相同功能
## 基础功能 ##
- 快速搭建框架
- 加载读取配置文件
- 加载指定注解的类
- 实现 IOC
- 实现 Aop
- 实现数据库事务
## 优化提高 ##
## 使用方式 ##
1. Service 表示业务层注解,Controller 表示 web层注解,Inject bean 注入注解，Transaction 事务注解
1. 下载项目安装到本地 mvn install 。 在你自己开发的项目 pom 文件导入此项目 `<dependency>
            <groupId>com.bcc</groupId>
            <artifactId>myspring</artifactId>
            <version>1.0.0</version>
        </dependency>`
1. 
