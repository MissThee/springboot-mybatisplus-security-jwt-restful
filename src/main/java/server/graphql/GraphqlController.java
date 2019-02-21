package server.graphql;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.tool.ResGraphql;

import java.io.Serializable;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@RestController
@RequestMapping("/g")
public class GraphqlController {
    private final GraphQL graphQL;

    @Autowired
    public GraphqlController(GraphQL graphQL) {
        this.graphQL = graphQL;
    }

    @RequestMapping("1")
    public ResGraphql graphql1(@RequestParam(value = "query", required = false) String query) throws Exception {
        if (StringUtils.isEmpty(query)) {
            query = "{user(id:1) {id,username,nickname}}";
        }
        ExecutionResult executionResult = graphQL.execute(query);
        return new ResGraphql(executionResult);
    }

    @RequestMapping("2")
    public ResGraphql graphql2(@RequestParam(value = "query", required = false) String query) throws Exception {
        String schemaStr = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaStr);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        //请求测试
        //        /g/2?query={hello}
        return new ResGraphql(GraphQL.newGraphQL(schema).build().execute(query));
    }

    @RequestMapping("3")
    public Object graphql3(@RequestParam(value = "query", required = false) String query) {
        GraphQLObjectType objectType = newObject()//创建对象
                .name("UserTest")
                .field(newFieldDefinition().type(GraphQLString).name("string1"))
                .field(newFieldDefinition().type(GraphQLString).name("string2").staticValue("GraphQLObjectType中设置的静态值会覆盖原值"))
                .build();

        GraphQLFieldDefinition fieldDefinition = newFieldDefinition()//创建匹配方式
                .name("userTestDef")//不能重复创建名称相同的GraphQLFieldDefinition
                .argument(newArgument().name("param1").type(GraphQLString).build()) //请求时必须匹配两个参数的名称和类型
                .argument(newArgument().name("param2").type(GraphQLString).build())
                .type(objectType)
                .dataFetcher(environment -> {
                    String param1 = environment.getArgument("param1");
                    String param2 = environment.getArgument("param2");
                    new UserTest();

                    if (!StringUtils.isEmpty(param1 + param2)) {
                        return new UserTest().setString1(param1 + param2);
                    } else {
                        return new UserTest();
                    }
                })
                .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()//创建图
                .query(newObject()
                        .name("GraphQuery")
                        .field(fieldDefinition)
                        .build())
                .build();
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        //请求测试
        //        /g/3?query={userTestDef(param1:"124",param2:"123") {string1,string2}}
        return graphQL.execute(query);
    }

    @Data
    @Accessors(chain = true)
    public class UserTest implements Serializable {
        public String string1 = "对象值初始值";
        public String string2 = "456";
    }
}
