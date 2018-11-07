# 开发 javaWeb 框架 #
    自己手写一个 Javaweb 框架，仿 spring 和 springMvc 实现相同功能
## 基础功能 ##
- 快速搭建框架
- 加载读取配置文件
- 加载指定注解的类
- 实现 IOC 
- 实现 Aop : 仿照 spring + aspect 的 aop , 自定义注解,自己利用代理实现 aop 功能
- 实现数据库事务
## 使用方式 ##

1. 需要在你的项目中加入框架需要的配置文件 smart.properties
1. Service 表示业务层注解,Controller 表示 web层注解,Inject bean 注入注解，Transaction 事务注解
1. 下载项目安装到本地 mvn install 。 在你自己开发的项目 pom 文件导入此项目 `<dependency>
            <groupId>com.bcc</groupId>
            <artifactId>myspring</artifactId>
            <version>1.0.0</version>
        </dependency>`
1. Action 注解的使用,加在 controller 的方法上 @Action("请求方法:/请求路径")  eg. @Action("get:/getById")
1. Cotroller 里面方法的写法 . 如果想返回页面,返回值为 ModelAndView 对象 ; 只想返回数据,则返回 ServerResponse 对象 . 想要传入参数 , 在方法参数里面放 RequestParam 对象即可 , 目前只支持将参数映射到 map 中 ,以 key-value 形式使用 , 不支持对象
1. 自定义 AOP 的使用 ，切面类继承 AbstractAspect 类，重写想要拦截的方法。在切面类上加上 @Aspect(Controller.class) , 想要拦截那个类就在那个类上加上 @Controller 注解，或者其它各种自定义注解都可以
1. 事务的使用 : 目前只支持 添加了 @Service 注解的类的事务,在 service 类的某个方法上加 @Transaction 注解即可实现事务
## 优化提高 ##
1. 
