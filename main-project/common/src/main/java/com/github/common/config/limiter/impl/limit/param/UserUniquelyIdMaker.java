package com.github.common.config.limiter.impl.limit.param;
//限流逻辑使用这个接口返回的字符串为key，进行限流。
// 即此字符串内容相同时，频繁访问会被限制。
// 默认实现为DefaultUserUniquelyIdMaker，即以[用户token]+[用户ip]+[请求的uri]+[请求类型]区分本次请求是否应被限流
public interface UserUniquelyIdMaker {
    String getUserUniquelyId();
}
