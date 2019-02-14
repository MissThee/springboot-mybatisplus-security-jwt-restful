package server.service.imp.login;

import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.basic.*;
import server.db.primary.model.basic.*;
import server.db.primary.dto.login.LoginDTO;
import server.service.interf.login.LoginService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LoginImp implements LoginService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public LoginImp(UserMapper userMapper, UserRoleMapper userRoleMapper, RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper, PermissionMapper permissionMapper, MapperFacade mapperFacade) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionMapper = permissionMapper;
        this.mapperFacade = mapperFacade;

    }
    @Override
    public LoginDTO selectUserByUsername(String username) {
        //查找用户
        User user;
        {
            Example userExp = new Example(User.class);
            userExp.createCriteria()
                    .andEqualTo(User_.USERNAME, username);
            user = userMapper.selectOneByExample(userExp);
        }
        return getUserLoginInfo(user);
    }

    @Override
    public LoginDTO selectUserById(Integer id) {
        //查找用户
        User user;
        {
            user = userMapper.selectByPrimaryKey(id);
            if (user == null) {
                throw new UnauthenticatedException("无此账号信息");
            }
        }
        return getUserLoginInfo(user);
    }

    private LoginDTO getUserLoginInfo(User user) {
        //查找角色id集
        List<Integer> roleIdList = new ArrayList<>();
        {
            Example userRoleExp = new Example(UserRole.class);
            userRoleExp.createCriteria()
                    .andEqualTo(UserRole_.USER_ID, user.getId());
            List<UserRole> userRoleList = userRoleMapper.selectByExample(userRoleExp);
            userRoleList.forEach(e -> roleIdList.add(e.getRoleId()));
        }
        //查找角色信息集
        List<Role> roleList = new ArrayList<>();
        Set<String> roleValueList = new HashSet<>();
        if (roleIdList.size() > 0) {
            Example roleExp = new Example(Role.class);
            roleExp.createCriteria()
                    .andIn(Role_.ID, roleIdList);
//                    .andEqualTo(Role_.IS_ENABLE, true);
            roleList = roleMapper.selectByExample(roleExp);
            roleList.forEach(e -> roleValueList.add(e.getRole()));
        }
        //查找权限id集
        List<Integer> permissionIdList = new ArrayList<>();
        if (roleIdList.size() > 0)  {
            Example rolePermissionExp = new Example(RolePermission.class);
            rolePermissionExp.createCriteria()
                    .andIn(RolePermission_.ROLE_ID, roleIdList);
            List<RolePermission> rolePermissionList = rolePermissionMapper.selectByExample(rolePermissionExp);
            rolePermissionList.forEach(e -> permissionIdList.add(e.getPermissionId()));
        }
        //查找权限信息集
        List<Permission> permissionList = new ArrayList<>();
        Set<String> permissionValueList = new HashSet<>();
        if (permissionIdList.size() > 0)     {
            Example permissionExp = new Example(Permission.class);
            permissionExp.createCriteria()
                    .andIn(Permission_.ID, permissionIdList);
//                    .andEqualTo(Permission_.IS_ENABLE, true) ;
            permissionList = permissionMapper.selectByExample(permissionExp);
            permissionList.forEach(e -> permissionValueList.add(e.getPermission()));
        }

        LoginDTO loginDTO = mapperFacade.map(user, LoginDTO.class);
        loginDTO.setRoleValueList(roleValueList);
        loginDTO.setPermissionValueList(permissionValueList);
        return loginDTO;
    }
}
