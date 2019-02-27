package com.github.missthee.socketio.model;

import lombok.Data;

@Data
public class AckModel {
    boolean result;
    String msg;

    private AckModel(boolean result, String msg) {
        this.msg = msg;
        this.result = result;
    }

    public static AckModel success() {
        return new AckModel(true, "");
    }

    public static AckModel success(String msg) {
        return new AckModel(true, msg);
    }

    public static AckModel failure() {
        return new AckModel(false, "");
    }

    public static AckModel failure(String msg) {
        return new AckModel(false, msg);
    }
}
