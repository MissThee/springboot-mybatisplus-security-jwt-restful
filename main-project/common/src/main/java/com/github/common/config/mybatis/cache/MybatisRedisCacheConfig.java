package com.github.common.config.mybatis.cache;

import com.github.common.tool.ApplicationContextHolder;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//若使用devtool需做额外配置，否则反序列化会音classloader不同导致异常
public class MybatisRedisCacheConfig implements Cache {
    private final Integer EXPIRE_MINUTES = 6 * 60;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;
    private static RedisTemplate<Serializable, Serializable> redisTemplate;

    public MybatisRedisCacheConfig(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    private RedisTemplate<Serializable, Serializable> redisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<Serializable, Serializable>) ApplicationContextHolder.getBean("DBRedisTemplate");
        }
        return redisTemplate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        redisTemplate().boundHashOps(getId()).put(key, value);
        redisTemplate().expire(getId(), EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public Object getObject(Object key) {
        redisTemplate().expire(getId(), EXPIRE_MINUTES, TimeUnit.MINUTES);
        return redisTemplate().boundHashOps(getId()).get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return redisTemplate().boundHashOps(getId()).delete(key);
    }

    @Override
    public void clear() {
        redisTemplate().delete(getId());
    }

    @Override
    public int getSize() {
        Long size = redisTemplate().boundHashOps(getId()).size();
        return size == null ? 0 : size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }


}
