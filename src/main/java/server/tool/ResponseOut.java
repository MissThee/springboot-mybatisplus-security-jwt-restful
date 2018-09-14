//package server.tool;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class ResponseOut {
//    //把返回值输出到客户端
//    private static void streamOut(HttpServletResponse response, JSONObject bodyJO) {
//        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        ServletOutputStream out = null;
//        try {
//            out = response.getOutputStream();
//            out.write(JSON.toJSONString(bodyJO).getBytes());
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void out(HttpServletResponse response, int code, String msg) {
//        HttpStatus httpStatus = HttpStatus.valueOf(code);
//        response.setStatus(httpStatus.value());
//        JSONObject jO = new JSONObject() {{
//            put("msg", httpStatus.getReasonPhrase() + ". " + msg + " 【需要将有效的token，加入http请求Header中的Authorization，进行请求，token由账号密码登录接口获取，于返回值的header中】");
//        }};
//        streamOut(response, jO);
//    }
//
//    public static void out401(HttpServletResponse response, String msg) {
//        out(response, 401, msg);
//    }
//
//    public static void out401(HttpServletResponse response) {
//        out(response, 401, "");
//    }
//}
