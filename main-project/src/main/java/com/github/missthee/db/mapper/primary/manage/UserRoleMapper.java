package com.github.missthee.db.mapper.primary.manage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import com.github.missthee.config.mybatis.cache.MybatisRedisCacheConfig;
import com.github.missthee.db.entity.primary.manage.UserRole;

@Component
@CacheNamespace(implementation = MybatisRedisCacheConfig.class)
public interface UserRoleMapper extends BaseMapper<UserRole> {

}