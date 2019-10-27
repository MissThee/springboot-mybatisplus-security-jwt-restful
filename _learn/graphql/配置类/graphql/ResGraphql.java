//package server.graphql;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
//import graphql.ExecutionResult;
//import graphql.GraphQLException;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//@Data
//@Component
//@NoArgsConstructor
//public class ResGraphql {
//    public ResGraphql(ExecutionResult executionResult, String msg) throws Exception {
//        if (executionResult.getErrors() != null && executionResult.getErrors().size() > 0) {
//            throw new GraphQLException(executionResult.getErrors().toString());
//        }
//        this.result = true;
//        this.data = executionResult.getData();
//        this.msg = StringUtils.isEmpty(msg) ? "" : msg;
//    }
//
//    public ResGraphql(ExecutionResult executionResult) throws Exception {
//        if (executionResult.getErrors() != null && executionResult.getErrors().size() > 0) {
//            throw new GraphQLException(executionResult.getErrors().toString());
//        }
//        this.result = true;
//        this.data = executionResult.getData();
//        this.msg = "";
//    }
//
//    private Boolean result;
//    private Object data;
//    private String msg;
//}
//
