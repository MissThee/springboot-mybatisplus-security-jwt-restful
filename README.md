# 三厂项目
## 简介
基础结构
```shell
src
└─main
    ├─java
    │  └─server
    │      ├─WebApplication.java
    │      ├─config    配置类目录
    │      │  ├─CorsConfig.java    跨域配置
    │      │  ├─PrimaryDBConfig.java    主数据库配置
    │      │  └─SecondaryDBConfig.java    第二数据库配置
    │      ├─controller    controller目录
    │      │  └─example    例子目录
    │      │    ├─ExampleController.java    访问例子
    │      │    └─ExampleLoginController.java    登录例子，可获取token
    │      ├─db    数据库目录
    │      │  ├─common    通用类
    │      │  │  └─CommonMapper.java
    │      │  ├─primary    主数据库目录
    │      │  │  ├─mapper
    │      │  │  │  └─CLoginMapper.java
    │      │  │  └─model
    │      │  │      └─CLogin.java
    │      │  └─secondary    第二数据库目录
    │      │      ├─mapper
    │      │      │  └─CLoginMapper.java
    │      │      └─model
    │      │          └─CLogin.java
    │      ├─exception    异常处理
    │      │   ├─CustomErrorController.java    自定义异常返回
    │      │   ├─ErrorLogPrinter.java    错误日志打印工具类
    │      │   └─ExceptionController.java    controller异常拦截、返回
    │      ├─log    日志工具目录
    │      │  └─ControllerLog.java    controller请求、返回值日志输出
    │      ├─security    安全框架目录
    │      │  ├─JavaJWT.java
    │      │  ├─MyJWTFilter.java
    │      │  ├─MyRealm.java
    │      │  ├─MyWebSessionManager.java
    │      │  ├─ResponseOut.java
    │      │  └─ShiroConfig.java    shiro配置
    │      ├─service    service目录
    │      │  ├─CLoginService.java
    │      │  └─imp
    │      │     └─CLoginServiceImp.java
    │      └─tool    通用工具类目录
    │         ├─FileRec.java    接收上传文件工具类
    │         └─Res.java    返回值结构包装工具类
    └─resources
        │  ├─application.properties    配置文件(先尽量使用properties格式)
        │  ├─generatorPrimaryConfig.xml    mybatis生成工具配置（Primary）
        │  └─generatorSecondaryConfig.xml    mybatis生成工具配置（Secondary）
        │                                    以上两个配置文件在pom文件中设置启用哪一个
        ├─mybatis    mybatis文件目录
        │  ├─mybatis.cfg.xml    mybatis配置(配置大部分在bean中完成了)
        │  └─mapper    mapper目录
        │      ├─primary    主数据库mapper.xml目录
        │      │  ├─custom    自定义的mapper
        │      │  │  └─CloginMapper.xml 
        │      │  └─generate   生成的mapper
        │      └─secondary
        │          ├─custom
        │          │  └─CloginMapper.xml 
        │          └─generate
        ├─static    静态资源目录
        │  └─files    静态资源访问目录 
        │       └─ces.png
        └─templates
```
## 返回值格式
返回值格式 ：  
返回类型Res.java
```shell
{
    "_msg":"",     附加消息,无附加消息加入空字符串
    "result":ture, 返执行结果，以判断执行成功或失败，失败将失败原因写至_msg中
    "data":{}      需返回数据时将数据以此格式返回
}
```
返回状态码：  
`200`：成功  
`401`：未登录，需前端将用户跳转至登录界面  
`403`：无权限，已登录用户无指定权限的接口时返回此状态码  
`500`：服务器内部错误，_msg附加信息会显示简要异常输出信息  

## RestFul风格接口规范

| http方法 | 资源操作 | 幂等 | 安全 |
| :------: | :------: | :------: | :------: |
| GET | SELECT | √ | √ |
| POST | INSERT | × | × |
| PUT | UPDATE | √ | × |
| DELETE | DELETE | √ | × | 

幂等性：对统一REST接口的多次访问，得到的资源状态是相同的。  
安全性：对该REST接口访问，不会使服务器资源的状态发生变化。  
以上规范仅供参考。  

#### 推荐使用以下方法 ,可统一参数传递与后台获取方式，有利于快速开发  

| http方法 | 资源操作 |  
| :------: | :------: |   
| POST | SELECT |  
| PUT | INSERT |  
| PATCH | UPDATE |  
| DELETE | DELETE |  