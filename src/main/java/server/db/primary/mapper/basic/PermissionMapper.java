package server.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.basic.Permission;
@Component
@CacheNamespace
public interface PermissionMapper extends CommonMapper<Permission> {

}