# 简介
> 基于RBAC管理系统，基础功能demo，集成了前端页面  
> `后端`springboot提供restful接口  
> `前端`vue、elementui、axios构建交互页面  
> `身份验证/访问权限`使用jwt与spring-security结合实现（旧版为使用shiro但shiro的注解不够灵活，改用security）   
>
(原`springboot-mybatis-shiro-jwt-restful`改为分支`shiro-demo`)
## 项目结构总览
```shell
root
├─dev-project    //[无用]另一个独立运行的项目，与本项目无关，但没删除，里边有一些工具的学习使用方法记录文档等
├─main-project    //项目主目录
│  ├─common    //通用模块：包含各种配置、工具类、其他模块共用的Model类等
│  ├─manage-base    //系统管理：主要为用户、角色、权限、单位 管理
│  ├─manage-flow    //springboot整合flowable：包含flowable流程运作相关的主要几大引擎的调用测试方法。默认启用（引入了flowable就会启用，暂时不会禁用的办法），项目启动时会在数据库中自动新建独立的table，若不使用可直接删除此模块，并将flowable移除即可
│  ├─manage-form    //表单测试接口：简单的表单填写
│  ├─service-start    //项目启动类，前端页面资源
│  └─webrtc    //springboot整合socketio，默认不启用。若启用，需在此子项中的
└─mybatis-plus-generator    //mybatis-plus官方提供的代码生成工具。自行做了修改，引用了本项目中的数据源配置，可在生成时自己选择部分配置

```
## 基础结构

```shell
root
├─dev-project    //[无用]因与本项目无关，就不写注解了
├─log_application    //logback日志输出目录，仅在profile为dev时日志会输出到此目录
│      log_all.log    //以下所有日志的集合
│      log_debug_mybatis1.log    //第一数据源的sql日志
│      log_debug_mybatis2.log    //第一数据源的sql日志
│      log_debug_request.log    //controller请求日志
│      log_error.log    //异常日志
│
├─log_tomcat    //springboot内置的tomcat的日志
├─main-project
│  │  pom.xml
│  │
│  ├─common
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─com
│  │          │      └─github
│  │          │          └─common
│  │          │              ├─config    //配置类目录
│  │          │              │  ├─cors    //跨域配置
│  │          │              │  │      CorsConfig.java    //跨域，默认配置允许任意host请求。此项目因页面与后台使用的是同一服务，不配置也可
│  │          │              │  │
│  │          │              │  ├─db    //数据库配置
│  │          │              │  │      CustomGlobalConfig.java    //myabtis-plus自定义通用方法注入配置
│  │          │              │  │      PrimaryDBConfig.java    //第一数据源配置（hikari）
│  │          │              │  │      //PrimaryDBConfigAtomikos.java    //第一数据源配置（jta。未使用。用jta做分布式事务时使用此配置）
│  │          │              │  │      SecondaryDBConfig.java    //第二数据源配置（hikari）
│  │          │              │  │      //SecondaryDBConfigAtomikos.java    //第二数据源配置（jta。未使用。用jta做分布式事务时使用此配置）
│  │          │              │  │      //TransactionConfig.java    //数据库事务配置（未使用。用jta做分布式事务时使用此配置）
│  │          │              │  │
│  │          │              │  ├─exception    //异常统一处理配置
│  │          │              │  │  ├─custom    //自定义异常
│  │          │              │  │  │      MyMethodArgumentNotValidException.java    //参数值不符合要求的范围或内容异常，统一处理返回400
│  │          │              │  │  │      MyMissingDataException.java    //参数值缺失异常，统一处理返回400
│  │          │              │  │  │
│  │          │              │  │  ├─handler    //异常捕获统一处理类
│  │          │              │  │  │      ControllerExceptionHandler.java    //controller异常捕获处理
│  │          │              │  │  │      MyErrorController.java    //其他异常处理。如：访问的url不存在返回的404结果可在此处处理
│  │          │              │  │  │      UnknownExceptionHandler.java    //ControllerExceptionHandler中未捕获的其他所有异常。与ControllerExceptionHandler类似，但比ControllerExceptionHandler后执行，由@Order注解配置
│  │          │              │  │  │
│  │          │              │  │  └─model
│  │          │              │  │          ExceptionResultModel.java    //异常处理
│  │          │              │  │
│  │          │              │  ├─json    //json序列化配置
│  │          │              │  │      LocalDateTimeSerializerConfig.java    //LocalDateTime格式化
│  │          │              │  │      //SpringFastJsonConfig.java    //fastjson序列化自定义配置（未使用，直接用默认配置）
│  │          │              │  │
│  │          │              │  ├─log    //日志输出配置
│  │          │              │  │  ├─aspect    //切面方法
│  │          │              │  │  │      ControllerLogger.java    //controller切面，输出日志：请求内容、返回值
│  │          │              │  │  │
│  │          │              │  │  ├─builder    //日志构建辅助类
│  │          │              │  │  │      GetterAndSetter.java    //反射工具
│  │          │              │  │  │      LogBuilder.java    //日志内容格式化工具
│  │          │              │  │  │
│  │          │              │  │  └─format    //logback配置相关，自定义输出格式、颜色
│  │          │              │  │          EasyHighlightingCompositeConverter.java
│  │          │              │  │          EasyPatternLayout.java
│  │          │              │  │          ProcessIdClassicConverter.java
│  │          │              │  │
│  │          │              │  ├─mybatis    //mybatis配置
│  │          │              │  │  └─cache    //缓存配置（默认未启用二级缓存，此配置实际无效。若启用，需安装redis、在application-common.properties中配置连接、在mybatis.cfg.xml中修改二级缓存配置为true）
│  │          │              │  │          DBRedisTemplateConfig.java    //redisTemplate配置
│  │          │              │  │          MybatisRedisCacheConfig.java    //mybatis二级缓存使用redis实现配置
│  │          │              │  │
│  │          │              │  ├─orika    //bean拷贝工具配置
│  │          │              │  │      OrikaConfig.java
│  │          │              │  │
│  │          │              │  ├─security    //安全框架配置
│  │          │              │  │  │  SpecialPermission.java    //自定义特殊权限值，用来标记基础账户、管理员账户（仅为两个字符串，定义为java对象易于寻找引用位置）
│  │          │              │  │  │
│  │          │              │  │  ├─jwt    //jwt配置
│  │          │              │  │  │      JavaJWT.java    //jwt的java实现：生成token，验证token，从token中获取指定信息等
│  │          │              │  │  │      UserInfoForJWT.java    //jwt生成token时，获取用户密码的接口（本项目生成token时需使用用户的密码）
│  │          │              │  │  │
│  │          │              │  │  └─springsecurity    //spring-security配置
│  │          │              │  │      │  SecurityConfig.java    //security配置：停用csrf，停用session(前后分离不用session记录用户登录信息)
│  │          │              │  │      │
│  │          │              │  │      ├─annotation    //权限注解配置
│  │          │              │  │      │      CustomPermissionEvaluator.java    //简单的实现@PreAuthorize注解表达式的hasPermission关键字
│  │          │              │  │      │
│  │          │              │  │      ├─exception    //身份验证异常统一处理
│  │          │              │  │      │      SecurityExceptionHandler.java    //权限不足(返回403)与未登录(返回401)异常处理。与ControllerExceptionHandler类似，为spring的异常拦截器。
│  │          │              │  │      │
│  │          │              │  │      └─filter
│  │          │              │  │              MyJWTVerificationFilter.java    //security与jwt结合验证身份。主要是用本次访问的token获取用户信息，生成一个security身份对象，供后续security验证
│  │          │              │  │              UserInfoForSecurity.java    //生成security身份对象时，获取UserDetails的接口。
│  │          │              │  │
│  │          │              │  └─swagger    //swagger2配置
│  │          │              │          SwaggerConfig.java    //swagger2配置，默认启用。访问http://[host]:[port]/swagger-ui.html
│  │          │              │
│  │          │              ├─db
│  │          │              │  ├─entity    //实体类（本目录为该项目中生成的实体类，因与数据库表完全一一对应，统一放置于此，其他子项目引用common项目来使用这些实体类）
│  │          │              │  │  ├─primary
│  │          │              │  │  │      DicAreaInvolved.java
│  │          │              │  │  │      DicIllegalBehavior.java
│  │          │              │  │  │      DicJobRank.java
│  │          │              │  │  │      DicJobType.java
│  │          │              │  │  │      DicResultType.java
│  │          │              │  │  │      DicSource.java
│  │          │              │  │  │      LetClue.java
│  │          │              │  │  │      LetClueAreaInvolved.java
│  │          │              │  │  │      LetClueDefendant.java
│  │          │              │  │  │      LetClueIllegalBehavior.java
│  │          │              │  │  │      LetClueSource.java
│  │          │              │  │  │      LetDefendant.java
│  │          │              │  │  │      LetDefendantJobType.java
│  │          │              │  │  │      Stuff.java
│  │          │              │  │  │      SysPermission.java
│  │          │              │  │  │      SysRole.java
│  │          │              │  │  │      SysRolePermission.java
│  │          │              │  │  │      SysSignature.java
│  │          │              │  │  │      SysUnit.java
│  │          │              │  │  │      SysUser.java
│  │          │              │  │  │      SysUserRole.java
│  │          │              │  │  │      SysUserUnit.java
│  │          │              │  │  │      TableInDb1.java
│  │          │              │  │  │
│  │          │              │  │  └─secondary
│  │          │              │  │          TableInDb2.java
│  │          │              │  │
│  │          │              │  └─mapper    //mapper
│  │          │              │      └─common    //mybatis-plus自定义方法
│  │          │              │              BatchInsertByList.java
│  │          │              │              CustomerSqlInjector.java
│  │          │              │              CustomSqlMethod.java
│  │          │              │
│  │          │              └─tool    //通用工具类
│  │          │                      ApplicationContextHolder.java
│  │          │                      FileRec.java
│  │          │                      MapAndBean.java
│  │          │                      Res.java
│  │          │                      SimplePageInfo.java
│  │          │                      UnderLineAndHump.java
│  │          │
│  │          └─resources    //配置文件
│  │              │  application-common.properties
│  │              │  logback-spring.xml    //日志输出配置
│  │              │
│  │              └─mybatis    //mybatis配置文件，两个数据源均使用此配置，若独立配置需自行修改
│  │                      mybatis.cfg.xml
│  │
│  ├─manage-base    //系统管理模块（用户相关信息的增删改查）
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─com
│  │          │      └─github
│  │          │          └─base
│  │          │              ├─controller    //接口
│  │          │              │  ├─example    //测试代码的一些接口
│  │          │              │  │      AuthSecurityController.java
│  │          │              │  │      ExampleController.java
│  │          │              │  │
│  │          │              │  ├─login    //登录
│  │          │              │  │      LoginController.java
│  │          │              │  │
│  │          │              │  └─manage    //管理
│  │          │              │          MyAccountController.java
│  │          │              │          PermissionController.java
│  │          │              │          RoleController.java
│  │          │              │          UnitController.java
│  │          │              │          UserController.java
│  │          │              │
│  │          │              ├─db
│  │          │              │  └─mapper
│  │          │              │      ├─primary
│  │          │              │      │  ├─manage
│  │          │              │      │  │      SysPermissionMapper.java
│  │          │              │      │  │      SysRoleMapper.java
│  │          │              │      │  │      SysRolePermissionMapper.java
│  │          │              │      │  │      SysSignatureMapper.java
│  │          │              │      │  │      SysUnitMapper.java
│  │          │              │      │  │      SysUserMapper.java
│  │          │              │      │  │      SysUserRoleMapper.java
│  │          │              │      │  │      SysUserUnitMapper.java
│  │          │              │      │  │
│  │          │              │      │  └─transaction
│  │          │              │      │          TableInDb1Mapper.java
│  │          │              │      │
│  │          │              │      └─secondary
│  │          │              │          └─transaction
│  │          │              │                  TableInDb2Mapper.java
│  │          │              │
│  │          │              ├─dto    //项目中使用这些实体类，在controller、service、mapper之间交换传输数据
│  │          │              │  ├─login
│  │          │              │  │      AuthDTO.java
│  │          │              │  │
│  │          │              │  └─manage
│  │          │              │      ├─myaccount
│  │          │              │      │      MyAccountComparePasswordDTO.java
│  │          │              │      │      MyAccountUpdatePasswordDTO.java
│  │          │              │      │
│  │          │              │      ├─permission
│  │          │              │      │      SysPermissionInsertOneDTO.java
│  │          │              │      │      SysPermissionUpdateOneDTO.java
│  │          │              │      │
│  │          │              │      ├─role
│  │          │              │      │      SysRoleInsertOneDTO.java
│  │          │              │      │      SysRoleInTableDetailDTO.java
│  │          │              │      │      SysRoleUpdateOneDTO.java
│  │          │              │      │
│  │          │              │      ├─unit
│  │          │              │      │      SysUnitInsertOneDTO.java
│  │          │              │      │      SysUnitUpdateOneDTO.java
│  │          │              │      │
│  │          │              │      └─user
│  │          │              │              SysUserInsertOneDTO.java
│  │          │              │              SysUserInTableDetailDTO.java
│  │          │              │              SysUserInTableDTO.java
│  │          │              │              SysUserUpdateOneDTO.java
│  │          │              │
│  │          │              ├─service
│  │          │              │  ├─imp
│  │          │              │  │  ├─login
│  │          │              │  │  │      AuthInfoImp.java
│  │          │              │  │  │
│  │          │              │  │  ├─manage
│  │          │              │  │  │      MyAccountImp.java
│  │          │              │  │  │      PermissionImp.java
│  │          │              │  │  │      RoleImp.java
│  │          │              │  │  │      SignatureImp.java
│  │          │              │  │  │      UnitImp.java
│  │          │              │  │  │      UserImp.java
│  │          │              │  │  │
│  │          │              │  │  └─transaction
│  │          │              │  │          OperateAllImp.java
│  │          │              │  │
│  │          │              │  └─interf
│  │          │              │      ├─login
│  │          │              │      │      AuthInfoService.java
│  │          │              │      │
│  │          │              │      ├─manage
│  │          │              │      │      MyAccountService.java
│  │          │              │      │      PermissionService.java
│  │          │              │      │      RoleService.java
│  │          │              │      │      SignatureService.java
│  │          │              │      │      UnitService.java
│  │          │              │      │      UserService.java
│  │          │              │      │
│  │          │              │      └─transaction
│  │          │              │              OperateAllService.java
│  │          │              │
│  │          │              └─vo    //项目中使用这些实体类，在controller、前端间传输数据
│  │          │                  ├─login
│  │          │                  │      LoginVO.java
│  │          │                  │
│  │          │                  └─manage
│  │          │                          MyAccountVO.java
│  │          │                          PermissionVO.java
│  │          │                          RoleVO.java
│  │          │                          UnitVO.java
│  │          │                          UserVO.java
│  │          │
│  │          └─resources
│  │              └─exceltemplate
│  │                      test.xls
│  │
│  ├─manage-flow    //工作流测试模块
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─com
│  │          │      └─github
│  │          │          └─flow
│  │          │              ├─config
│  │          │              │  ├─exception
│  │          │              │  │      FlowableExceptionHandler.java
│  │          │              │  │
│  │          │              │  └─flowable
│  │          │              │          ActDBConfig.java
│  │          │              │          MyProcessEngineAutoConfiguration.java
│  │          │              │
│  │          │              ├─controller
│  │          │              │      HistoryController.java
│  │          │              │      ImgController.java
│  │          │              │      ManaController.java
│  │          │              │      UseWithFormController.java
│  │          │              │
│  │          │              ├─dto
│  │          │              │      FlowLinePositionDTO.java
│  │          │              │      FlowNodePositionDTO.java
│  │          │              │      FormDataDTO.java
│  │          │              │      HistoricProcessInstanceDTO.java
│  │          │              │      HistoricTaskInstanceDTO.java
│  │          │              │      IdentityLinkDTO.java
│  │          │              │      ProcessDefinitionDTO.java
│  │          │              │      ProcessInstanceDTO.java
│  │          │              │      TaskDTO.java
│  │          │              │
│  │          │              ├─listener
│  │          │              │      TaskListenerImp.java
│  │          │              │
│  │          │              └─vo
│  │          │                      HistoryVO.java
│  │          │                      ImgVO.java
│  │          │                      ManaVO.java
│  │          │                      UseWithFormVO.java
│  │          │
│  │          └─resources
│  │                  application-flow.properties
│  │
│  ├─manage-form    //表单填写模块
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─com
│  │          │      └─github
│  │          │          └─form
│  │          │              ├─common
│  │          │              │      Constants.java
│  │          │              │      EnumCaseManageState.java
│  │          │              │      EnumLetClueResultType.java
│  │          │              │      EnumStuffRelationType.java
│  │          │              │
│  │          │              ├─controller
│  │          │              │  │  StuffController.java
│  │          │              │  │
│  │          │              │  └─letter
│  │          │              │      ├─clue
│  │          │              │      │      LetClueController.java
│  │          │              │      │
│  │          │              │      └─dictionary
│  │          │              │              DicAreaInvolvedController.java
│  │          │              │              DicIllegalBehaviorController.java
│  │          │              │              DicJobRankController.java
│  │          │              │              DicJobTypeController.java
│  │          │              │              DicResultTypeController.java
│  │          │              │              DicSourceController.java
│  │          │              │
│  │          │              ├─db
│  │          │              │  └─mapper
│  │          │              │      └─primary
│  │          │              │          └─letter
│  │          │              │              │  StuffMapper.java
│  │          │              │              │
│  │          │              │              ├─clue
│  │          │              │              │      LetClueAreaInvolvedMapper.java
│  │          │              │              │      LetClueDefendantMapper.java
│  │          │              │              │      LetClueIllegalBehaviorMapper.java
│  │          │              │              │      LetClueMapper.java
│  │          │              │              │      LetClueSourceMapper.java
│  │          │              │              │      LetDefendantJobTypeMapper.java
│  │          │              │              │      LetDefendantMapper.java
│  │          │              │              │
│  │          │              │              └─dictionary
│  │          │              │                      DicAreaInvolvedMapper.java
│  │          │              │                      DicIllegalBehaviorMapper.java
│  │          │              │                      DicJobRankMapper.java
│  │          │              │                      DicJobTypeMapper.java
│  │          │              │                      DicResultTypeMapper.java
│  │          │              │                      DicSourceMapper.java
│  │          │              │
│  │          │              ├─models
│  │          │              │  ├─dto
│  │          │              │  │  │  LetFileResDTO.java
│  │          │              │  │  │
│  │          │              │  │  └─letter
│  │          │              │  │      ├─clue
│  │          │              │  │      │      LetClueCreateDTO.java
│  │          │              │  │      │      LetClueListResDTO.java
│  │          │              │  │      │      LetClueResDTO.java
│  │          │              │  │      │      LetClueUpdateDTO.java
│  │          │              │  │      │      LetDefendantResDTO.java
│  │          │              │  │      │
│  │          │              │  │      └─dictionary
│  │          │              │  │              DicCommonInsertDTO.java
│  │          │              │  │              DicCommonListDTO.java
│  │          │              │  │              DicCommonUpdateDTO.java
│  │          │              │  │
│  │          │              │  └─vo
│  │          │              │      └─letter
│  │          │              │          ├─clue
│  │          │              │          │      LetClueCreateVO.java
│  │          │              │          │      LetClueDeleteVO.java
│  │          │              │          │      LetClueGetByIdVO.java
│  │          │              │          │      LetClueGetListVO.java
│  │          │              │          │      LetClueUpdateResultTypeVO.java
│  │          │              │          │      LetClueUpdateVO.java
│  │          │              │          │
│  │          │              │          └─dictionary
│  │          │              │                  DicCreateOneReqVO.java
│  │          │              │                  DicCreateOneResVO.java
│  │          │              │                  DicDeleteOneReqVO.java
│  │          │              │                  DicGetListReqVO.java
│  │          │              │                  DicGetListResVO.java
│  │          │              │                  DicUpdateOneReqVO.java
│  │          │              │
│  │          │              ├─service
│  │          │              │  ├─imp
│  │          │              │  │  └─letter
│  │          │              │  │      ├─clue
│  │          │              │  │      │      LetClueAreaInvolvedImp.java
│  │          │              │  │      │      LetClueDefendantImp.java
│  │          │              │  │      │      LetClueIllegalBehaviorImp.java
│  │          │              │  │      │      LetClueImp.java
│  │          │              │  │      │      LetClueSourceImp.java
│  │          │              │  │      │      LetDefendantImp.java
│  │          │              │  │      │      LetDefendantJobTypeImp.java
│  │          │              │  │      │
│  │          │              │  │      └─dictionary
│  │          │              │  │              DicAreaInvolvedImp.java
│  │          │              │  │              DicIllegalBehaviorImp.java
│  │          │              │  │              DicJobRankImp.java
│  │          │              │  │              DicJobTypeImp.java
│  │          │              │  │              DicResultTypeImp.java
│  │          │              │  │              DicSourceImp.java
│  │          │              │  │
│  │          │              │  └─interf
│  │          │              │      └─letter
│  │          │              │          ├─clue
│  │          │              │          │      LetClueAreaInvolvedService.java
│  │          │              │          │      LetClueDefendantService.java
│  │          │              │          │      LetClueIllegalBehaviorService.java
│  │          │              │          │      LetClueService.java
│  │          │              │          │      LetClueSourceService.java
│  │          │              │          │      LetDefendantJobTypeService.java
│  │          │              │          │      LetDefendantService.java
│  │          │              │          │
│  │          │              │          └─dictionary
│  │          │              │                  DicAreaInvolvedService.java
│  │          │              │                  DicIllegalBehaviorService.java
│  │          │              │                  DicJobRankService.java
│  │          │              │                  DicJobTypeService.java
│  │          │              │                  DicResultTypeService.java
│  │          │              │                  DicSourceService.java
│  │          │              │
│  │          │              └─utils
│  │          │                      LetClueIdBuilder.java
│  │          │                      StringUtil.java
│  │          │
│  │          └─resources
│  │              │  application-form.properties
│  │              │
│  │              └─mybatis
│  │                  └─mapper
│  │                      └─primary
│  │                          └─generate
│  │                              └─letter
│  │                                  └─clue
│  │                                          LetClueMapper.xml
│  │
│  ├─service-start    //服务启动模块
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─com
│  │          │      └─github
│  │          │              AutoRunWhenStart.java
│  │          │              WebApplication.java    //启动类，由此启动服务
│  │          │
│  │          └─resources
│  │              │  application-localtest.properties
│  │              │  application.properties
│  │              │
│  │              └─static
│  │                  ├─files    //静态资源文件
│  │                  │      ces
│  │                  │      ces.png
│  │                  │      ces.z
│  │                  │      mybatis_test_db.sql    //第一源初始化文件
│  │                  │      mybatis_test_db_2.sql    //第二源初始化文件
│  │                  │
│  │                  └─webpage    //页面文件
│  │
│  └─webrtc    //socketio模块
│      └─src
│          └─main
│              ├─java
│              │  └─com
│              │      └─github
│              │          └─socketio
│              │              │  MessageEventHandler.java
│              │              │  MyCommandLineRunner.java
│              │              │  SocketIOServerConfig.java
│              │              │
│              │              └─model
│              │                      MessageModel.java
│              │
│              └─resources
│                      application-socketio.properties
│
└─mybatis-plus-generator    //代码生成工具，运行后可选择一些生成配置
    └─src
        └─main
            └─java
                └─generator
                        MybatisPlusGen.java

```

## 使用
主要管理功能测试：
1. 克隆本项目到本地
2. 使用`\main-project\service-start\src\main\resources\static`目录中`mybatis_test_db.sql`与`mybatis_test_db_2.sql`文件可导入项目所需基础表到mysql数据库；
3. 运行`\main-project\service-start\src\main\java\com\github\WebApplication.java`（若需修改端口号，修改`application-localtest.properties`即可，或自行寻找配置字段修改，均为springboot配置方法，各个子项目配置文件由`service-start`启动模块中`application.properties`的`spring.profiles.include`包含）
4. 浏览器访问`http:\\localhost:8098\webpage\index.html`，账号admin，密码123

测试socket.io：
1. socketio尚未在以上页面中进行使用，仅有独立的交互测试demo。
2. 测试socketio需先配置`webrtc`模块中`application-socketio.properties`，设置`socket.io.enable=true`
3. 直接使用浏览器打开`\main-project\webrtc\src\main\resources\static\socketio-test.html`，依次点击`登录`，`连接`即可测试实时聊天demo

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
`400`：请求的参数错误  
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
