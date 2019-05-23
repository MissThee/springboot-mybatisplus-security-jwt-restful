# 简介

开发自用整理，各分支：
- `master`：为整合各种组件空项目，未划为多个分子项目，涉及组件：fastjson、flowable、RabbitMQ、Shiro、JWT、tk-mapper、Socket.IO、poi、swagger2/ui
- `mybatis-plus-module`：项目按功能划分为了多个子项目，基本就是将master中config包下配置及相应功能代码分开了，涉及组件与`master`相似，其中Shiro换为Security，tk-mapper换为mybatis-plus，去除RabbitMQ、graphql。无简介
- `dev`：与`master`相似，其中Shiro换为Security(因为发现shiro用纯注解方式无法实现“拥有xx权限或xx角色可访问”这个逻辑)，com.github.missthee.tool.Independent有开发/部署常用工具的记录，许多混杂的测试demo

## 基础结构
```shell
root
│  TestDB.sql    //测试用sql文件
│
├─lib    //引用的本地jar目录
│      mysql-connector-java-5.1.44-bin.jar
│      ojdbc6.jar
│
├─log_application     //本地测试生成的log
│      log_all.log
│      log_debug_mybatis1.log
│      log_debug_mybatis2.log
│      log_debug_request.log
│      log_error.log
│
├─log_tomcat     //本地测试，tomcat生成的log
│  ├─logs
│  │      access_log.2019-04-17.log
│  │
│  └─work
│      └─Tomcat
│          └─localhost
│              └─ROOT
│
└─src
    └─main
        ├─java
        │  └─com
        │      └─github
        │          └─missthee
        │              │  WebApplication.java    //启动类
        │              │
        │              ├─config    //配置目录
        │              │  ├─cors    //跨域配置
        │              │  │      CorsConfig.java
        │              │  │
        │              │  ├─db    //数据库配置
        │              │  │      PrimaryDBConfig.java
        │              │  │      SecondaryDBConfig.java
        │              │  │
        │              │  ├─exception    //全局异常拦截配置
        │              │  │      ExceptionController.java
        │              │  │      MyErrorController.java
        │              │  │
        │              │  ├─flowable    //flowable配置
        │              │  │      ActDBConfig.java
        │              │  │      MyProcessEngineAutoConfiguration.java
        │              │  │      ResponseConfig.java
        │              │  │
        │              │  ├─json    //fastjson配置
        │              │  │      SpringFastJsonConfig.java
        │              │  │
        │              │  ├─log    //日志输出配置
        │              │  │  ├─aspect
        │              │  │  │      ControllerLogger.java
        │              │  │  │
        │              │  │  ├─builder
        │              │  │  │      GetterAndSetter.java
        │              │  │  │      LogBuilder.java
        │              │  │  │
        │              │  │  └─format
        │              │  │          EasyHighlightingCompositeConverter.java
        │              │  │          EasyPatternLayout.java
        │              │  │          ProcessIdClassicConverter.java
        │              │  │
        │              │  ├─mq    //RabbitMQ配置
        │              │  │  │  MqConnectionConfig.java    //Mq连接配置（已配置在application.properties）
        │              │  │  │  MqConsumer.java    //Mq消费者配置（未启用，使用默认配置）
        │              │  │  │  MqProductor.java    //Mq生产者配置（主要配置rabbitTemplate，手动应答模式）
        │              │  │  │
        │              │  │  └─init    //测试路由、队列初始化
        │              │  │          DirectExchangeConfig.java
        │              │  │          FanoutExchangeConfig.java
        │              │  │          TopicExchangeConfig.java
        │              │  │
        │              │  ├─security    //shiro、jwt配置
        │              │  │  ├─jwt
        │              │  │  │      JavaJWT.java    //jwt工具类
        │              │  │  │      UserInfoForJWT.java    //jwt获取用户信息的接口
        │              │  │  │
        │              │  │  └─shiro
        │              │  │          ExceptionControllerShiro.java    //shiro抛出异常拦截，统一返回
        │              │  │          MyDefaultWebSessionManager.java    //关闭Shiro的session
        │              │  │          MyJWTFilter.java    //自定义身份验证，登录验证依赖JWT
        │              │  │          MyRealm.java    //自定义身份验证，获取用户角色、权限，为验证提供用户信息
        │              │  │          ShiroConfig.java    //Shiro配置，将其他配置类整合进Shiro
        │              │  │          UserInfoForShiro.java    //Shiro获取用户信息的接口
        │              │  │
        │              │  ├─socketio    //socket.io配置
        │              │  │  │  MessageEventHandler.java    //监听事件
        │              │  │  │  MyCommandLineRunner.java    //socket.io启动
        │              │  │  │  SocketIOServerConfig.java   //socket.io配置
        │              │  │  │
        │              │  │  └─model
        │              │  │          AckModel.java    //应答消息结构类
        │              │  │          MessageModel.java    //发送/接收消息结构类
        │              │  │
        │              │  └─tkmapper    //通用mapper，及二级缓存使用redis配置
        │              │      ├─cache    //通用mapper二级缓存使用redis的配置
        │              │      │      DBRedisTemplateConfig.java    //redisTemplate的配置，主要为序列化/反序列化
        │              │      │      MybatisRedisCacheConfig.java    //缓存存进Redis的配置
        │              │      │
        │              │      ├─common    //通用mapper自定义通用类
        │              │      │      CommonMapper.java    //XxxMapper.java继承此类即可使用通用方法
        │              │      │
        │              │      └─custom    //通用mapper自定义扩展通用方法
        │              │              MyInsertMapper.java
        │              │              MyInsertProvider.java
        │              │              MySelectByExampleMapper.java
        │              │              MySelectByExampleProvider.java
        │              │              OrderStr.java
        │              │
        │              ├─controller    //controller
        │              │  ├─example
        │              │  │      AuthController.java
        │              │  │      ExampleController.java
        │              │  │      MqCController.java
        │              │  │      MqPController.java
        │              │  │
        │              │  ├─flowable
        │              │  │      FJSON.java
        │              │  │      ProcessManaController.java
        │              │  │      ProcessUseController.java
        │              │  │
        │              │  ├─login
        │              │  │      LoginController.java
        │              │  │
        │              │  └─sheet
        │              │          ComplexSheet.java
        │              │          SimpleSheet.java
        │              │
        │              ├─db    //数据库
        │              │  └─primary    //第一数据库
        │              │      ├─dto
        │              │      │  └─login
        │              │      │          LoginDTO.java
        │              │      │
        │              │      ├─mapper    //mapper类（由generator生成）
        │              │      │  ├─basic
        │              │      │  │      PermissionMapper.java
        │              │      │  │      RoleMapper.java
        │              │      │  │      RolePermissionMapper.java
        │              │      │  │      UserMapper.java
        │              │      │  │      UserRoleMapper.java
        │              │      │  │
        │              │      │  └─compute
        │              │      │          ComputeMapper.java
        │              │      │
        │              │      └─model    //model类（由generator生成）
        │              │          ├─basic
        │              │          │      Permission.java
        │              │          │      Role.java
        │              │          │      RolePermission.java
        │              │          │      User.java
        │              │          │      UserRole.java
        │              │          │
        │              │          ├─compute
        │              │          │      Compute.java
        │              │          │
        │              │          └─sheet
        │              │                  ComplexSheetData.java
        │              │                  ComplexSheetForm.java
        │              │                  SimpleSheet.java
        │              │
        │              ├─service    //service
        │              │  ├─imp    //service实现类
        │              │  │  ├─basic
        │              │  │  │      UserImp.java
        │              │  │  │
        │              │  │  ├─compute
        │              │  │  │      ComputeImp.java
        │              │  │  │
        │              │  │  └─login
        │              │  │          LoginImp.java
        │              │  │
        │              │  └─interf    //service接口
        │              │      ├─basic
        │              │      │      UserService.java
        │              │      │
        │              │      ├─compute
        │              │      │      ComputeService.java
        │              │      │
        │              │      └─login
        │              │              LoginService.java
        │              │
        │              └─tool    //静态工具类
        │                      ApplicationContextHolder.java    //Bean获取
        │                      FileRec.java    //文件接收
        │                      Res.java    //返回数据包装
        │
        └─resources
            │  application.properties
            │  generatorConfig-FullExample.xml    //mybatis generator配置总示例
            │  generatorConfig.xml    //mybatis generator配置
            │  logback-spring.xml    //log日志输出，分为：prod日志输出为文件；非prod，日志直接打印控制台
            │
            ├─Independent    //测试用的文件
            │  ├─db
            │  │      mybatis_test_db.sql
            │  │
            │  ├─html
            │  │      axios-file-download-MT.html    //测试表格导出的网页
            │  │
            │  └─socket
            │          socketio-test.html    //测试socket.io的网页
            │
            ├─mybatis
            │      mybatis.cfg.xml    //mybatis配置文件（被PrimaryDBConfig、SecondaryDBConfig引用）
            │
            ├─processes
            │      DemoProcess.bpmn    //flowable的流程图文件
            │
            └─static
                └─files    //静态资源访问测试用的文件
                        ces
                        ces.png
                        ces.z

```
## 涉及组件
+ Mybatis、通用mapper、fastJson、swagger2/ui、poi、Shiro、JavaJWT  
+ Socket.IO(默认停用)：可通过`application.properties`中设置`socket.io.enable=false`停用；删除`config`包下的`socketio`及pom中依赖即可完全移除
+ RabbitMQ(默认停用)：可通过`application.properties`中设置`custom.rabbitmq.enable=false`停用；删除`config`包下的`mq`及pom中依赖即可完全移除
+ Flowable：删除`controller`包下的`flowable`，删除`config`包下的`flowable`及pom中依赖即可完全移除
## 使用
1. 克隆本项目到本地
2. 使用根目录中TestDB.sql文件可导入项目所需基础表；测试账号admin，test，test1，test2，test3，密码123
3. 配置`application.properties`数据库链接（若不使用工作流，将Flowable相关文件删除，或先给其配置一个临时数据库）
4. 运行com.github.missthee.WebApplication
5. 测试controller：访问http://host:port/swagger-ui.html可查看接口文档，进行http请求测试
6. 测试excel导出：使用src/resources/Independent/html/axios-file-download-MT.html可测试excel导出
7. 测试socket.io：使用src/resources/Independent/socket/socketio-test.html可测试excel导出
8. 在此项目进行开发增加新功能，几乎只需关注db,controller,service三个目录下的文件及application.properties即可。
## 返回值格式
返回值格式 ：  
返回类型Res.java
```shell
{
    "msg":"",        附加消息,无附加消息加入空字符串
    "result":ture,   返执行结果，以判断执行成功或失败，失败将失败原因写至msg中
    "data":{}        返回数据
}
```
返回状态码：  
`200`：成功  
`401`：未登录。前端需将用户跳转至登录界面  
`403`：无权限。已登录用户，但无权访问接口时返回此状态码  
`500`：服务器内部错误，msg附加信息会显示简要异常输出信息  

## RestFul风格接口规范介绍

| http方法 | 资源操作 | 幂等 | 安全 |
| :------: | :------: | :------: | :------: |
| GET | SELECT | √ | √ |
| POST | INSERT | × | × |
| PUT | UPDATE | √ | × |
| DELETE | DELETE | √ | × | 

幂等性：对统一REST接口的多次访问，得到的资源状态是相同的。  
安全性：对该REST接口访问，不会使服务器资源的状态发生变化。  
以上规范仅供参考。  

#### 本项目使用以下方法，可统一参数传递与后台获取参数方式，所有参数均由application/json格式在body中传输，有利于统一接口风格、快速开发  

| http方法 | 资源操作 | 幂等 | 安全 |  
| :------: | :------: | :------: | :------: |   
| POST | SELECT | √ | √ | 
| PUT | INSERT | × | × |  
| PATCH | UPDATE | √ | × |  
| DELETE | DELETE | √ | × |   
