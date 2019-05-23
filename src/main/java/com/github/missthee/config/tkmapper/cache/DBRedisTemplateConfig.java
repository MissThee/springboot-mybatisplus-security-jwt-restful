package com.github.missthee.config.tkmapper.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//配置mybatis使用的redisTemplate，主要是修改序列化与反序列化方式
@Configuration
public class DBRedisTemplateConfig {
    @Bean()
    RedisTemplate<Object, Object> DBRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
            serializer.setObjectMapper(mapper);
            redisTemplate.setKeySerializer(serializer);
        }
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
            serializer.setObjectMapper(mapper);
            redisTemplate.setValueSerializer(serializer);
        }
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}