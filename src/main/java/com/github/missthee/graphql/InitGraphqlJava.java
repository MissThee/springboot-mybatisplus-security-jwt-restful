//package server.graphql;
//
//import graphql.GraphQL;
//import graphql.schema.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import server.db.primary.model.basic.User;
//import server.service.interf.basic.UserService;
//
//import static graphql.Scalars.*;
//import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
//import static graphql.schema.GraphQLObjectType.newObject;
//import static graphql.schema.GraphQLArgument.newArgument;
//
//@Component
//public class InitGraphqlJava {
//
//    private final UserService userService;
//    private GraphQLObjectType userObjectType;
//
//    @Autowired
//    public InitGraphqlJava(UserService userService) {
//        userObjectType();
//        this.userService = userService;
//    }
//
//    //根据User实体类构建GraphQL实体类
//    private void userObjectType() {
//        this.userObjectType =newObject()
//                .name("User")//不能重复创建名称相同的GraphQLObjectType
//                .field(newFieldDefinition().name("id").type(GraphQLInt).build())
//                .field(newFieldDefinition().name("nickname").type(GraphQLString).build())
//                .field(newFieldDefinition().name("username").type(GraphQLString).build())
//                .field(newFieldDefinition().name("password").type(GraphQLString).build())
//                .field(newFieldDefinition().name("salt").type(GraphQLString).staticValue("设置此值覆盖原值").build())
//                .build();
//    }
//
//    //查询单个用户信息
//    //构建匹配方式，引用GraphQL实体类。匹配{user(id:1) {id,username,nickname}}
//    private GraphQLFieldDefinition userFieldDefinition() {
//        return newFieldDefinition()
//                .name("user")                                                       //方法名。不能重复创建名称相同的GraphQLFieldDefinition
//                .argument(newArgument().name(User.ID).type(GraphQLID).build())     //方法所需参数。查询时会匹配参数名及其类型，两者均需匹配；参数有多个也需全部匹配
//                .type(userObjectType)                                               //查询结果对应的类型。返回类型为实体类、集合类均需与dataFetcher的返回值统一，否则查不到返回结果，报错
//                .dataFetcher(fetcher -> {
//                    // 获取查询参数
//                    int id = Integer.parseInt(fetcher.getArgument(User.ID));
//                    // 执行查询
//                    return userService.selectOneById(id);
//                })
//                .build();
//    }
//
//    //查询多个用户信息
//    //构建匹配方式，引用GraphQL实体类。匹配{userList(customString:"test") {id,username,nickname}}
//    private GraphQLFieldDefinition userListFieldDefinition() {
//        return newFieldDefinition()
//                .name("userList")
//                .argument(newArgument().name("customString").type(GraphQLString).build())
//                .type(new GraphQLList(userObjectType))
//                .dataFetcher(fetcher -> {
//                    // 获取查询参数
//                    String username = fetcher.getArgument("customString");
//                    // 执行查询
//                    return userService.selectByNickname(username);
//                })
//                .build();
//    }
//
//    //增加用户
//    //构建匹配方式，引用GraphQL实体类。匹配{userList(customString:"test") {id,username,nickname}}
//    private GraphQLFieldDefinition userAddFieldDefinition() {
//        return newFieldDefinition()
//                .name("user")
//                .argument(newArgument().name("nickname").type(GraphQLString).build())
//                .type(userObjectType)
//                .dataFetcher(fetcher -> {
//                    String nickname = fetcher.getArgument(User.NICKNAME);
//                    User user = new User().setNickname(nickname);
//                    userService.insertOne(user);
//                    return user;
//                })
//                .build();
//    }
//
//    //构建图，引用匹配方式(使用graphql-spring-boot-starter时，默认使用此bean)
//    //graphql-spring-boot-starter注意：
//    // query可使用get或post
//    // mutation仅可使用post
//    @Bean
//    public GraphQLSchema userGraphQLSchema() {
//        return GraphQLSchema.newSchema()
//                .query(newObject()
//                        .name("Query")
//                        .field(userListFieldDefinition())
//                        .field(userFieldDefinition())
//                        .build())
//                .mutation(newObject()
//                        .name("Mutation")
//                        .field(userAddFieldDefinition())
//                        .build())
//                .build();
//    }
//
//    //构建graphQL，引用图。(使用graphql-java-spring-boot-starter-webmvc时，默认使用此bean)
//    @Bean
//    public GraphQL userGraphQLDefinition() {
//        return GraphQL.newGraphQL(userGraphQLSchema()).build();
//    }
//}
