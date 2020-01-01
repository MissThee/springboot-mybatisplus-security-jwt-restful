package com.github.common.config.limiter.store;


public interface LimitInfoStore {
    //存储指定UserUniquelyId的访问时间。默认实现：存储为key,value形式：UserUniquelyId，System.currentTimeMillis()
    void setLimitInfo(String UserUniquelyId);
    //获取指定UserUniquelyId存储的访问时间。默认实现：返回存储的System.currentTimeMillis()，Long类型。
    Object getLimitInfo(String UserUniquelyId);
}
