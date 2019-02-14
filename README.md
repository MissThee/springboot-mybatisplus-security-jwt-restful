

# springboot-mybatis-shiro-jwt-restful
springboot，mybatis多数据源，tkmapper，自定义mybatis generator，shiro，jwt，socket.io，restful，excel导出导入 空项目
  
# 简介

使用TestDB.sql文件可导入项目所需基础表，及表格导出示例表
## 基础结构
```shell
root
├─lib
│      mybatis-generator-lombok-plugin-1.0-SNAPSHOT.jar    //mybatis生成自定义格式需引用的jar
│      mysql-connector-java-5.1.44-bin.jar    //mysql驱动
│      ojdbc6.jar    //oracle驱动
│
├─log    测试时日志生成目录
│  │  log_debug.log
│  │  log_debug_mybatis1.log
│  │  log_debug_mybatis2.log
│  │  log_debug_request.log
│  │  log_error.log
│  │  log_info.log
│  └─ log_warn.log
│   
├─src
│  └─main
│      ├─java
│      │  └─server
│      │      │  WebApplication.java    //启动类
│      │      │
│      │      ├─config
│      │      │  ├─cors    //跨域
│      │      │  │      CorsConfig.java    //跨域配置
│      │      │  │
│      │      │  ├─db    //多数据库配置
│      │      │  │      PrimaryDBConfig.java    //第一数据库配置
│      │      │  │      SecondaryDBConfig.java    //第二数据库配置
│      │      │  │
│      │      │  ├─exception    //错误统一捕获处理
│      │      │  │      ExceptionController.java    //捕获controller中错误，格式化输出
│      │      │  │      MyErrorController.java    //自定义错误处理，主要用于地自定义404返回
│      │      │  │
│      │      │  ├─json    //json处理
│      │      │  │      SpringFastJsonConfig.java    //springboot改为使用FastJson配置
│      │      │  │
│      │      │  ├─log    //日志相关
│      │      │  │  ├─controller
│      │      │  │  │      ControllerLogger.java    //controller访问日志
│      │      │  │  │
│      │      │  │  └─format    //自定义日志样式（格式，颜色等。用于自定义分类输出日志后，在控制台还原springboot默认日志样式）
│      │      │  │          EasyHighlightingCompositeConverter.java
│      │      │  │          EasyPatternLayout.java
│      │      │  │          ProcessIdClassicConverter.java
│      │      │  │
│      │      │  ├─mq    //消息队列（本项目使用rabbitmq测试）
│      │      │  │  │  MqConnectionConfig.java    //连接配置
│      │      │  │  │  MqConsumer.java    //消费者配置
│      │      │  │  │  MqProductor.java    //生产者配置
│      │      │  │  │
│      │      │  │  └─init    //初始化配置（路由器，队列）
│      │      │  │          DirectExchangeConfig.java
│      │      │  │          FanoutExchangeConfig.java
│      │      │  │          TopicExchangeConfig.java
│      │      │  │
│      │      │  ├─security    //身份认证配置（shiro实现，前后端分离，将token存放于header中使用）
│      │      │  │      JavaJWT.java    //自定义认证方式。实现了给当前用户赋予权限，角色。进行验证
│      │      │  │      MyJWTFilter.java    //session的reddis支持
│      │      │  │      MyRealm.java    //shiro配置。注入自定义方法，增加注解支持等
│      │      │  │      ShiroConfig.java    //登入shiro方法
│      │      │  │
│      │      │  └─tkmapper    //mybatis通用mapper
│      │      │      ├─cache    //二级缓存redis支持
│      │      │      │      ApplicationContextHolder.java    
│      │      │      │      DBRedisTemplateConfig.java    
│      │      │      │      RedisCache.java    
│      │      │      │
│      │      │      ├─common    //通用mapper类
│      │      │      │      CommonMapper.java    //继承此类，实现基本sql语句方法
│      │      │      │
│      │      │      └─custom    //自定义额外通用方法
│      │      │              MyExampleMapper.java
│      │      │              MyExampleProvider.java
│      │      │              MyInsertMapper.java
│      │      │              MyInsertProvider.java
│      │      │              MySelectMapper.java
│      │      │              MySelectProvider.java
│      │      │              OrderStr.java
│      │      │
│      │      ├─controller
│      │      │  ├─example    //接口测试例子
│      │      │  │      ExampleController.java
│      │      │  │      MqCController.java
│      │      │  │      MqPController.java
│      │      │  │
│      │      │  ├─login    //登录/拉取用户信息方法
│      │      │  │      IndexController.java
│      │      │  │      LoginController.java
│      │      │  │
│      │      │  └─sheet    //表格导出接口例子
│      │      │          ComplexSheet.java
│      │      │          SimpleSheet.java
│      │      │
│      │      ├─db    //数据库相关类
│      │      │  ├─primary
│      │      │  │  ├─dto    //dto
│      │      │  │  │  └─login
│      │      │  │  │          LoginDTO.java
│      │      │  │  │
│      │      │  │  ├─mapper    //mapper（生成）
│      │      │  │  │  └─basic
│      │      │  │  │          PermissionMapper.java
│      │      │  │  │          RoleMapper.java
│      │      │  │  │          RolePermissionMapper.java
│      │      │  │  │          UserMapper.java
│      │      │  │  │          UserRoleMapper.java
│      │      │  │  │
│      │      │  │  └─model    //model（生成）
│      │      │  │      ├─basic
│      │      │  │      │      Permission.java
│      │      │  │      │      Role.java
│      │      │  │      │      RolePermission.java
│      │      │  │      │      User.java
│      │      │  │      │      UserRole.java
│      │      │  │      │
│      │      │  │      └─sheet
│      │      │  │              ComplexSheetData.java
│      │      │  │              ComplexSheetForm.java
│      │      │  │              SimpleSheet.java
│      │      │  │
│      │      │  └─secondary
│      │      │      ├─mapper
│      │      │      └─model
│      │      ├─service
│      │      │  ├─imp
│      │      │  |  ├─basic
│      │      │  │  │       UserImp.java
│      │      │  │  └─login
│      │      │  │          LoginImp.java
│      │      │  │
│      │      │  └─interf
│      │      │     ├─basic
│      │      │     │       UserImp.java
│      │      │     └─login
│      │      │             LoginImp.java
│      │      │
│      │      ├─socketio    //socketio相关
│      │      │  │  MessageEventHandler.java
│      │      │  │  MyCommandLineRunner.java
│      │      │  │  SocketIOServerConfig.java
│      │      │  │
│      │      │  └─model    //socketio辅助实体类
│      │      │          AckModel.java
│      │      │          MessageModel.java
│      │      │
│      │      └─tool    //封装工具类
│      │          │  ApplicationContextHelper.java    //获取bean
│      │          │  ExcelExport.java    //表格导出
│      │          │  ExcelImport.java    //表格导入
│      │          │  FileRec.java    //文件上传
│      │          │  ListCompute.java    //List<Model>增加合计/平均行
│      │          │  Res.java    //返回值包装类
│      │          │  ResponseOut.java    //使用流返回工具类（未用）
│      │          │  TreeData.java    //树形数据构建工具
│      │          │
│      │          └─Independent    //测试demo
│      │              │  axios-file-download-MT.html    //文件下载
│      │              │  ExcelReadTool.java    //表格读取工具
│      │              │  MapperXmlToDTOGenerator.java    //mapper.xml转实体类代码工具
│      │              │  socketio-test.html    //socket简易聊天
│      │              │  start-server8096.bat    //启动脚本
│      │              │
│      │              └─winsw    //windows服务注册工具
│      │                      ser-install.bat
│      │                      ser-restart.bat
│      │                      ser-start.bat
│      │                      ser-stop.bat
│      │                      ser-uninstall.bat
│      │                      trans2Serv.exe
│      │                      trans2Serv.xml
│      │
│      └─resources
│          │  application.properties
│          │  application.yml.bak
│          │  generatorConfig.xml
│          │  logback-spring.xml    //自定义日志配置
│          │
│          ├─mybatis
│          │  │  mybatis.cfg.xml
│          │  │
│          │  └─mapper
│          │      ├─primary
│          │      │  └─generate
│          │      └─secondary
│          │          └─generate
│          └─static
│              │  mybatis-generator-lombok-plugin-master.zip    //自定义mybatis生成工具相关类源码，可自行定制，打包jar包
│              │  testdb.sql    //测试用基础数据库表
│              │
│              └─files    //资源文件目录
│                      ces.png
│
└─target
    └─generated-sources
        └─annotations    //metamodel生成目录
```
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
`401`：未登录，需前端将用户跳转至登录界面  
`403`：无权限，已登录用户无指定权限的接口时返回此状态码  
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

#### 本项目使用以下方法 ,可统一参数传递与后台获取方式，有利于快速开发  

| http方法 | 资源操作 | 幂等 | 安全 |  
| :------: | :------: | :------: | :------: |   
| POST | SELECT | √ | √ | 
| PUT | INSERT | × | × |  
| PATCH | UPDATE | √ | × |  
| DELETE | DELETE | √ | × |   
