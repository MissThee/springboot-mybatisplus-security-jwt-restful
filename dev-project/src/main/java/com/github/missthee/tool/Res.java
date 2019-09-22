package com.github.missthee.tool;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

@Data
@Component
@NoArgsConstructor
public class Res<T> implements Serializable {
    private Res(Boolean result, T data, String msg) {
        this.result = result;
        try {
            data = data == null ? (T) new HashMap() : data;
        } catch (Exception ignored) {
        }
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
        return new Res<>(result, null, msg);
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

    public static void out(HttpServletResponse httpServletResponse, InputStream inputStream) throws IOException {
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        int len;
        byte[] buff = new byte[100];
        while ((len = inputStream.read(buff)) > 0) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        outputStream.close();
    }
    private Boolean result;
    private T data;
    private String msg;
}