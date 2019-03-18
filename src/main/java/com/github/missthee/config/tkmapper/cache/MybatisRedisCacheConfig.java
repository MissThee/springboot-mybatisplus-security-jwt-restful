package com.github.missthee.config.tkmapper.cache;

import com.github.missthee.tool.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//若使用devtool需做额外配置，否则反序列化会音classloader不同导致异常
@Slf4j
public class MybatisRedisCacheConfig implements Cache {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;

    public MybatisRedisCacheConfig(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getRedisTemplate().boundHashOps(getId()).put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return getRedisTemplate().boundHashOps(getId()).get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return getRedisTemplate().boundHashOps(getId()).delete(key);
    }

    @Override
    public void clear() {
        getRedisTemplate().delete(getId());
    }

    @Override
    public int getSize() {
        Long size = getRedisTemplate().boundHashOps(getId()).size();
        return size == null ? 0 : size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private RedisTemplate<Object, Object> getRedisTemplate() {
        return ApplicationContextHolder.getBean("DBRedisTemplate");
    }

}
