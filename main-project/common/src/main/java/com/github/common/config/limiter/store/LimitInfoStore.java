package com.github.common.config.limiter.store;


public interface LimitInfoStore {
    void setLimitInfo(String UserUniquelyId);

    Object getLimitInfo(String UserUniquelyId);
}
