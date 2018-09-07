package server.service;

import server.db.primary.model.sysoption.AuthRolePermission;

import java.util.List;

public interface FunAuthRolePermissionService {
    List<AuthRolePermission> selectPermissionByRoleId(Long roleId);

    boolean deleteRolePermissionByRoleId(Long roleId);

    boolean createRolePermission(AuthRolePermission authRolePermission);
}
