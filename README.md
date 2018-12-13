

# springboot-mybatis-shiro-jwt-restful
springboot，mybatis多数据源，tkmapper，自定义mybatis generator，shiro，jwt，socket.io，restful，excel导出导入 空项目
  
# 简介

使用TestDB.sql文件可导入项目所需基础表，及表格导出示例表
## 基础结构
```shell
src
└─main
    ├─java
    │  └─server
    │      │  WebApplication.java    启动类
    │      │
    │      ├─config    配置文件    
    │      │  │   PrimaryDBConfig.java    第一数据库配置
    │      │  │   SecondaryDBConfig.java    第二数据库配置
    │      │  │   SpringJsonConfig.java    fastJson配置 
    │      │  │   ControllerLog.java    访问日志
    │      │  │
    │      │  ├─exception    异常处理
    │      │  │      CustomErrorController.java    自定义异常信息返回格式（主要用于格式化404错误返回信息）
    │      │  │      ErrorLogPrinter.java    错误日志
    │      │  │      ExceptionController.java    controller异常信息捕获统一处理
    │      │  │
    │      │  └─security    身份认证相关
    │      │         JavaJWT.java    JavaJWT工具类
    │      │         MyJWTFilter.java    自定义过滤器
    │      │         MyRealm.java    自定义Realm
    │      │         MyWebSessionManager.java    自定义SessionManager
    │      │         ShiroConfig.java    shiro配置
    │      │       
    │      ├─controller    
    │      │  ├─example
    │      │  │      ExampleController.java    测试权限示例
    │      │  │
    │      │  ├─login
    │      │  │      IndexController.java    token获取用户信息
    │      │  │      LoginController.java    账号密码登录
    │      │  │
    │      │  └─sheet
    │      │         ComplexSheet.java    复杂表头表格
    │      │         SimpleSheet.java    简单表头表格
    │      │
    │      ├─db
    │      │  ├─common
    │      │  │      CommonMapper.java    通用mapper，以下为自定义扩展方法
    │      │  │      MyExampleMapper.java
    │      │  │      MyExampleProvider.java
    │      │  │      MyInsertMapper.java
    │      │  │      MyInsertProvider.java
    │      │  │      MySelectMapper.java
    │      │  │      MySelectProvider.java
    │      │  │
    │      │  ├─primary    第一数据库相关文件
    │      │  │  ├─mapper
    │      │  │  └─model
    │      │  │
    │      │  └─secondary    第二数据库相关文件
    │      │      ├─mapper
    │      │      └─model
    │      │
    │      ├─dto
    │      │
    │      ├─service    
    │      │  └─imp
    │      │
    │      ├─socketio    socketio使用demo 
    │      │
    │      └─tool
    │
    └─resources
        │  application.properties
        │  generatorConfig.xml
        │
        ├─mybatis
        │  │  mybatis.cfg.xml
        │  │
        │  └─mapper
        │      ├─primary    第一数据库相关文件
        │      │  ├─custom    自定义方法
        │      │  └─generate    工具生成的方法
        │      └─secondary
        │          ├─custom
        │          └─generate
        └─static
            └─files    静态文件目录（建议再shiro中配置白名单，避免多余验证）
               
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
