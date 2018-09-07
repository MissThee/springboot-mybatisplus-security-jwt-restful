package server.tool;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Getter
@Setter
//public class Res {
//    Res(Boolean result, Object data, String msg) {
//        this.result = result;
//        this.data = data == null ? new Object() : data;
//        this.msg = StringUtils.isEmpty(msg) ? "" : msg;
//    }
//
//    public static Res success(Object data, String msg) {
//        return new Res(true, data, msg);
//    }
//
//    public static Res successData(Object data) {
//        return new Res(true, data, "");
//    }
//
//    public static Res successMsg(String msg) {
//        return new Res(true, new HashMap(), msg);
//    }
//
//    public static Res failure(Object data, String msg) {
//        return new Res(false, data, msg);
//    }
//
//    public static Res failureData(Object data) {
//        return new Res(false, data, "");
//    }
//
//    public static Res failureMsg(String msg) {
//        return new Res(false, new HashMap(), msg);
//    }
//
//    private Boolean result;
//    private Object data;
//    private String msg;
//}
@Component
@NoArgsConstructor
public class Res<T> {

    private Res(Boolean result, Object data, String msg) {
        this.result = result;
        if (data == null) {
            data = new JSONObject();
        }
        this.data = data;
        this.msg = StringUtils.isEmpty(msg) ? "" : msg;
    }

    public static <T> Res<T> res(Boolean result, T data, String msg) {
        return new Res<>(result, data, msg);
    }

    public static <T> Res<T> success(T data, String msg) {
        return new Res<>(true, data, msg);
    }

    public static <T> Res<T> successData(T data) {
        return new Res<>(true, data, "");
    }

    public static <T> Res<T> successMsg(String msg) {
        return new Res<>(true, null, msg);
    }

    public static <T> Res<T> failure(T data, String msg) {
        return new Res<>(false, data, msg);
    }

    public static <T> Res<T> failureData(T data) {
        return new Res<>(false, data, "");
    }

    public static <T> Res<T> failureMsg(String msg) {
        return new Res<>(false, null, msg);
    }

    private Boolean result;
    private Object data;
    private String msg;
}