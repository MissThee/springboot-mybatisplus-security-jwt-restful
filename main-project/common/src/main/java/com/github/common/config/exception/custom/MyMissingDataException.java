package com.github.common.config.exception.custom;

public class MyMissingDataException extends Exception {
    //自定义错误。预期使用场景：数据库查询操作时，应有的数据不完整时，可手动抛出此错误。全局处理器处理为返回值500
    public MyMissingDataException(String message) {
        super(message);
    }
}
