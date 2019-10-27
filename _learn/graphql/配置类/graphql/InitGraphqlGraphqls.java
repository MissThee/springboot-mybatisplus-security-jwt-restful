//package server.graphql;
//
//import graphql.GraphQL;
//import graphql.schema.*;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import server.db.primary.model.basic.User;
//import server.service.interf.basic.UserService;
//
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import static graphql.Scalars.GraphQLInt;
//import static graphql.Scalars.GraphQLString;
//import static graphql.schema.GraphQLArgument.newArgument;
//import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
//import static graphql.schema.GraphQLObjectType.newObject;
//import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
//
//@Component
//public class InitGraphqlGraphqls {
//
//    private final UserService userService;
//    private GraphQLObjectType userObjectType;
//
//    @Autowired
//    public InitGraphqlGraphqls(UserService userService) {
//        this.userService = userService;
//    }
//
//    private TypeDefinitionRegistry typeDefinitionRegistry() {
//        InputStream stream = getClass().getClassLoader().getResourceAsStream("schema.graphqls");
//        assert stream != null;
//        return new SchemaParser().parse(new InputStreamReader(stream));
//    }
//
//    private RuntimeWiring runtimeWiring() {
//        return newRuntimeWiring()
//                .type("Query", function -> {
//                            function.dataFetcher("user", fetcher -> {//{user(id:1){id,username,nickname}}
//                                // 获取查询参数
//                                int id = Integer.parseInt(fetcher.getArgument(User.ID));
//                                // 执行查询
//                                return userService.selectOneById(id);
//                            });
//                            function.dataFetcher("userList", fetcher -> {//{userList(customString:"test"){id,username,nickname}}
//                                // 获取查询参数
//                                String username = fetcher.getArgument("customString");
//                                // 执行查询
//                                return userService.selectByNickname(username);
//                            });
//                            return function;
//                        }
//                )
//                .type("Mutation", function -> {
//                            function.dataFetcher("user", fetcher -> {//mutation{user(nickname:"test"){id,username,nickname}}
//                                String nickname = fetcher.getArgument(User.NICKNAME);
//                                User user = new User().setNickname(nickname);
//                                userService.insertOne(user);
//                                return user;
//                            });
//                            return function;
//                        }
//                )
//                .build();
//    }
//
//    //构建图，引用匹配方式(使用graphql-spring-boot-starter时，默认使用此bean)
//    //graphql-spring-boot-starter注意：
//    // query可使用get或post
//    // mutation仅可使用post
//    @Bean
//    public GraphQLSchema userGraphQLSchema() {
//        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry(), runtimeWiring());
//    }
//
//    //构建graphQL，引用图。(使用graphql-java-spring-boot-starter-webmvc时，默认使用此bean)
//    @Bean
//    public GraphQL userGraphQLDefinition() {
//        return GraphQL.newGraphQL(userGraphQLSchema()).build();
//    }
//}
//
