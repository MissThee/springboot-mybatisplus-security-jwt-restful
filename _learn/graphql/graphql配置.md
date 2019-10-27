1. 项目配置文件
    ```properties
       #--------------------graphql----------------------------------------
       graphql.servlet.mapping=/graphql
       graphql.servlet.enabled=true
       graphql.servlet.corsEnabled=true
       # if you want to @ExceptionHandler annotation for custom GraphQLErrors
       graphql.servlet.exception-handlers-enabled=true
       #http://localhost:port/graphql 默认请求地址
       #--------------------graphiql----------------------------------------
       graphiql.mapping=/graphiql
       graphiql.endpoint.graphql=/graphql
       graphiql.endpoint.subscriptions=/subscriptions
       graphiql.static.basePath=/
       graphiql.enabled=true
       graphiql.pageTitle=GraphiQL Test
       graphiql.cdn.enabled=false
       graphiql.cdn.version=0.11.11
       graphiql.props.variables.editorTheme=solarized light
       #测试时Authorization的值
       #graphiql.headers.Authorization=[token value]
       #http://localhost:port/graphiql 默认请求地址
    ```
2. 配置类
    1. 需引入`graphql-java-spring-boot-starter-webmvc` 或 `graphql-spring-boot-starter`
    2. 两种方式创建graphql对象选择一种即可:
        + InitGraphqlGraphqls  配合resources/schema.graphqls配置文件
        + InitGraphqlJava      纯java

    3. GraphqlController 为自行实现接收参数方法。若使用此方法，可不使用graphql的starter相关依赖，其自带接口发布功能

