//package server.graphql;
//
//import graphql.ExecutionResult;
//import graphql.GraphQL;
//import graphql.schema.GraphQLFieldDefinition;
//import graphql.schema.GraphQLObjectType;
//import graphql.schema.GraphQLSchema;
//import graphql.schema.StaticDataFetcher;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import static graphql.Scalars.GraphQLString;
//import static graphql.schema.GraphQLArgument.newArgument;
//import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
//import static graphql.schema.GraphQLObjectType.newObject;
//import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
//
//@RestController
//@RequestMapping("/myGraphql")
//public class GraphqlController {
//    private final GraphQL graphQL;
//
//    @Autowired
//    public GraphqlController(GraphQL graphQL) {
//        this.graphQL = graphQL;
//    }
//
//    @RequestMapping
//    public ResGraphql graphql1(@RequestParam(value = "query", required = false) String query) throws Exception {
//        if (StringUtils.isEmpty(query)) {
//            query = "{user(id:1) {id,username,nickname}}";
//        }
//        ExecutionResult executionResult = graphQL.execute(query);
//        //请求测试
//        //        /myGraphql/1?query=XXXXX
//        return new ResGraphql(executionResult);
//    }
//}
