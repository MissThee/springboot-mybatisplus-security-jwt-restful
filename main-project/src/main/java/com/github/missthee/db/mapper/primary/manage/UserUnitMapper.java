package com.github.missthee.db.mapper.primary.manage;

import com.github.missthee.config.mybatis.cache.MybatisRedisCacheConfig;
import com.github.missthee.db.entity.primary.manage.UserUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-04-16
 */
@Component
@CacheNamespace(implementation = MybatisRedisCacheConfig.class)
public interface UserUnitMapper extends BaseMapper<UserUnit> {

}
