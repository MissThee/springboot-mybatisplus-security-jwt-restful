package com.github.missthee.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import com.github.missthee.config.tkmapper.cache.MybatisRedisCacheConfig;
import com.github.missthee.config.tkmapper.common.CommonMapper;
import com.github.missthee.db.primary.model.basic.UserRole;
@Component
@CacheNamespace(implementation= MybatisRedisCacheConfig.class)
public interface UserRoleMapper extends CommonMapper<UserRole> {

}