package server.service;

import server.db.primary.model.sysoption.AuthRole;

import java.util.List;

public interface FunAuthRoleService {
    List<AuthRole> selectRole();

    AuthRole selectRoleById(Long id);

    List<AuthRole> selectRoleForList();

    boolean isRoleExist(String roleNo);

    boolean isNameExist(String roleName);

    boolean createRole(AuthRole authRole);

    boolean isRoleExistExceptSelf(Long id, String roleNo);

    boolean isNameExistExceptSelf(Long id, String roleName);

    boolean updateRole(AuthRole authRole);

    boolean deleteRoleByIdList(List<Long> idList);
}
