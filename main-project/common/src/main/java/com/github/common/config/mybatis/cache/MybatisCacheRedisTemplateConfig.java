package com.github.common.config.mybatis.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Configuration
public class MybatisCacheRedisTemplateConfig {

    @Bean(name = "MybatisCacheRedisConnectionFactory")
    @ConfigurationProperties(prefix = "spring.redis.mybatis-cache")
    public RedisConnectionFactory primaryDataSourceHikari() {
        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<Serializable, Serializable> MybatisCacheRedisTemplate(@Qualifier("MybatisCacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置自定义key序列化方法
        Jackson2JsonRedisSerializer<Serializable> serializer = new Jackson2JsonRedisSerializer<>(Serializable.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //PropertyAccessor.ALL指定要序列化的访问器getXxx(),setXxx(),构造函数等。all是所有
        //JsonAutoDetect.Visibility.ANY指定哪些访问修饰符的对象要序列化。any是所有
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //ObjectMapper.DefaultTyping.NON_FINAL指定序列化输入的类型，类必须是非final修饰的，final修饰的类会抛出出异常
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    //mybatis二级缓存：
    //mybatis二级缓存使用hash结构存储：
    //限定类名：{
    //   myBatis的CacheKey对象1：数据对象1
    //   myBatis的CacheKey对象2：数据对象2
    //}
    //其中：
    //1. 限定类名为直接的字符串，可使用new StringRedisSerializer()直接转义
    //2. myBatis的CacheKey对象1，使用自定义的jackson序列化。
    //3. 数据对象，使用自定义的jackson序列化。
    //为什么使用自定义：
    // 以上2、3中使用自定义的jackson序列化方法不是必须的，redisTemplate默认使用JdkSerializationRedisSerializer序列化，也没有问题。
    // 使用自定义序列化可使redis中存储的内容可读性高，数据会序列化成标准的json格式；默认的序列化则是非可读文本内容"\x00\xAC..."
}