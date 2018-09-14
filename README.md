# springboot，mybatis多数据库，shiro，jwt，restful 项目基础文件
## 简介
基础结构
```shell
src
└─main
    ├─java
    │  └─server
    │      │  WebApplication.java    启动类
    │      │
    │      ├─config    配置文件    
    │      │      CorsConfig.java    跨域配置
    │      │      PrimaryDBConfig.java    第一数据库配置
    │      │      SecondaryDBConfig.java    第二数据库配置
    │      │      SpringJsonConfig.java    json配置（未使用）
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
    │      │          ComplexSheet.java    复杂表格
    │      │          SimpleSheet.java    简单表格
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
    │      │  │      OrderStr.java
    │      │  │
    │      │  ├─primary    第一数据库相关文件
    │      │  │  ├─mapper
    │      │  │  │  │  PermissionMapper.java
    │      │  │  │  │  RoleMapper.java
    │      │  │  │  │  RolePermissionMapper.java
    │      │  │  │  │  UserMapper.java
    │      │  │  │  │  UserRoleMapper.java
    │      │  │  │  │
    │      │  │  │  └─sheet
    │      │  │  │          FunReportDataOwaterLoopDayResMapper.java
    │      │  │  │          FunReportStationDataCez0ResMapper.java
    │      │  │  │          FunReportStationDataCezResMapper.java
    │      │  │  │          FunReportStationDataCylOilExtraMapper.java
    │      │  │  │
    │      │  │  └─model
    │      │  │      │  Permission.java
    │      │  │      │  Role.java
    │      │  │      │  RolePermission.java
    │      │  │      │  User.java
    │      │  │      │  UserRole.java
    │      │  │      │
    │      │  │      └─sheet
    │      │  │              ReportDataOwaterLoop_Day_Res.java
    │      │  │              ReportStationDataCez0_Res.java
    │      │  │              ReportStationDataCez_Res.java
    │      │  │              ReportStationDataCylOil_Extra.java
    │      │  │
    │      │  └─secondary    第二数据库相关文件
    │      │      ├─mapper
    │      │      └─model
    │      ├─exception    异常处理
    │      │      CustomErrorController.java    自定义异常信息返回格式
    │      │      ErrorLogPrinter.java    错误日志
    │      │      ExceptionController.java    controller异常信息捕获统一处理
    │      │
    │      ├─log
    │      │      ControllerLog.java    访问日志
    │      │
    │      ├─security    身份认证相关
    │      │      JavaJWT.java    JavaJWT工具类
    │      │      MyJWTFilter.java    自定义过滤器
    │      │      MyRealm.java    自定义Realm
    │      │      MyWebSessionManager.java    自定义SessionManager
    │      │      ShiroConfig.java    shiro配置
    │      │
    │      ├─service    
    │      │  │  SheetComplexService.java
    │      │  │  SheetSimpleService.java
    │      │  │  UserService.java
    │      │  │
    │      │  └─imp
    │      │          SheetComplexImp.java
    │      │          SheetSimpleImp.java
    │      │          UserImp.java
    │      │
    │      └─tool
    │              axios-file-download-MT.html    文件下载测试
    │              ExcelReadTool.java    Excel表格内容转代码工具
    │              ExcelUtils.java    Excel导出工具类
    │              FileRec.java    文件接收工具类
    │              ListCompute.java    List加合计、平均行工具类
    │              MapperXmlToDTOGenerator.java    xml转实体类工具类
    │              MenuTree.java    id,parentId树形结构构建工具类
    │              Res.java    返回信息格式化工具类
    │              ResponseOut.java    返回http请求工具类
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
        │      │  │      PermissionMapper.xml
        │      │  │      RoleMapper.xml
        │      │  │      RolePermissionMapper.xml
        │      │  │      UserMapper.xml
        │      │  │      UserRoleMapper.xml
        │      │  │
        │      │  └─generate    工具生成的方法
        │      └─secondary
        │          ├─custom
        │          │      TestMapper.xml
        │          │
        │          └─generate
        └─static
            └─files    静态文件目录（建议再shiro中配置白名单，避免多余验证）
                    ces.png
```
## 返回值格式
返回值格式 ：  
返回类型Res.java
```shell
{
    "msg":"",     附加消息,无附加消息加入空字符串
    "result":ture, 返执行结果，以判断执行成功或失败，失败将失败原因写至msg中
    "data":{}      需返回数据时将数据以此格式返回
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

#### 推荐使用以下方法 ,可统一参数传递与后台获取方式，有利于快速开发  

| http方法 | 资源操作 |  
| :------: | :------: |   
| POST | SELECT |  
| PUT | INSERT |  
| PATCH | UPDATE |  
| DELETE | DELETE |  