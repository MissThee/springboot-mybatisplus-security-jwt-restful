package com.github.base.db.mapper.primary.manage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.config.mybatis.cache.RedisCache;
import com.github.common.db.entity.primary.SysUnit;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-04-15
 */
@Component
@CacheNamespace(implementation = RedisCache.class)
public interface SysUnitMapper extends BaseMapper<SysUnit> {

}
