package server.tool;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Component
@NoArgsConstructor
public class Res<T> implements Serializable {
    private Res(Boolean result, T data, String msg) {
        this.result = result;
        data = data == null ? (T) new HashMap() : data;
        this.data = data;
        this.msg = StringUtils.isEmpty(msg) ? "" : msg;
    }

    public static <T> Res<T> res(Boolean result, T data, String msg) {
        return new Res<>(result, data, msg);
    }

    public static <T> Res<T> res(Boolean result, T data) {
        return new Res<>(result, data, "");
    }

    public static <T> Res<T> res(Boolean result, String msg) {
        return new Res<>(result, null, "");
    }

    public static <T> Res<T> res(Boolean result) {
        return new Res<>(result, null, "");
    }

    public static <T> Res<T> success(T data, String msg) {
        return new Res<>(true, data, msg);
    }

    public static <T> Res<T> success(T data) {
        return new Res<>(true, data, "");
    }

    public static <T> Res<T> success(String msg) {
        return new Res<>(true, null, msg);
    }

    public static <T> Res<T> success() {
        return new Res<>(true, null, "");
    }

    public static <T> Res<T> failure(T data, String msg) {
        return new Res<>(false, data, msg);
    }

    public static <T> Res<T> failure(T data) {
        return new Res<>(false, data, "");
    }

    public static <T> Res<T> failure(String msg) {
        return new Res<>(false, null, msg);
    }

    public static <T> Res<T> failure() {
        return new Res<>(false, null, "");
    }

    private Boolean result;
    private T data;
    private String msg;
}