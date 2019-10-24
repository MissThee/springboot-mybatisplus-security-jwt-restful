package com.github.missthee.config.limiter.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultLimitInfoStore implements LimitInfoStore {

    private static final Map<String, Long> USER_LIMIT_INFO = new ConcurrentHashMap<>();

    public void setLimitInfo(String UserUniquelyId) {
        USER_LIMIT_INFO.put(UserUniquelyId, System.currentTimeMillis());
    }

    public Long getLimitInfo(String UserUniquelyId) {
        return USER_LIMIT_INFO.getOrDefault(UserUniquelyId, 0L);
    }
}
