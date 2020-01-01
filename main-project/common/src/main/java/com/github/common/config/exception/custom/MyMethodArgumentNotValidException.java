package com.github.common.config.exception.custom;

public class MyMethodArgumentNotValidException extends Exception {
    //自定义错误。预期使用场景：接收的参数不满足要求时，可手动抛出此错误。全局处理器处理为返回值400
    public MyMethodArgumentNotValidException(String message) {
        super(message);
    }
}
