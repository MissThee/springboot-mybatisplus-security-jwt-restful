package com.github.base.service.imp.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.manage.*;
import com.github.common.config.security.SpecialPermission;
import com.github.common.config.security.security.filter.UserInfoForSecurity;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.github.common.db.mapper.primary.manage.*;
import com.github.base.dto.login.LoginDTO;
import com.github.base.service.interf.login.LoginService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginImp implements LoginService, UserInfoForSecurity {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final MapperFacade mapperFacade;
    private final UnitMapper unitMapper;
    private final UserUnitMapper userUnitMapper;

    @Autowired
    public LoginImp(UserMapper userMapper, UserRoleMapper userRoleMapper, RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper, PermissionMapper permissionMapper, MapperFacade mapperFacade, UnitMapper unitMapper, UserUnitMapper userUnitMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionMapper = permissionMapper;
        this.mapperFacade = mapperFacade;
        this.unitMapper = unitMapper;
        this.userUnitMapper = userUnitMapper;
    }

    @Override
    public LoginDTO selectUserByUsername(String username) {
        //查找用户
        User user;
        {
            QueryWrapper<User> userQW = new QueryWrapper<>();
            userQW.eq(User.USERNAME, username)
                    .eq(User.IS_DELETE, false);
            user = userMapper.selectOne(userQW);
        }
        return buildLoginDTO(user);
    }

    @Override
    public LoginDTO selectUserById(Integer id) {
        User user;
        {
            QueryWrapper<User> userQW = new QueryWrapper<>();
            userQW.eq(User.ID, id)
                    .eq(User.IS_DELETE, false);
            user = userMapper.selectOne(userQW);
        }
        if (user == null) {
            throw new BadCredentialsException("无此账号信息");
        }
        return buildLoginDTO(user);
    }

    private LoginDTO buildLoginDTO(User user) {
        //查找角色id集(用户-角色关系表)
        List<Long> roleIdList = new ArrayList<>();
        {
            QueryWrapper<UserRole> userRoleQW = new QueryWrapper<>();
            userRoleQW.eq(UserRole.USER_ID, user.getId());
            List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQW);
            userRoleList.forEach(e -> roleIdList.add(e.getRoleId()));
        }
        //查找角色信息集(角色表)
        List<Role> roleList;
        List<String> roleValueList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            QueryWrapper<Role> roleQW = new QueryWrapper<>();
            roleQW.in(Role.ID, roleIdList)
                    .eq(Role.IS_ENABLE, true)
                    .eq(Role.IS_DELETE, false);
            roleList = roleMapper.selectList(roleQW);
            roleList.forEach(e -> roleValueList.add(e.getRole()));
        }
        //查找权限id集(角色-权限关系表)
        List<Long> permissionIdList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            QueryWrapper<RolePermission> rolePermissionQW = new QueryWrapper<>();
            rolePermissionQW.in(RolePermission.ROLE_ID, roleIdList);
            List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(rolePermissionQW);
            rolePermissionList.forEach(e -> permissionIdList.add(e.getPermissionId()));
        }
        //查找权限信息集(权限表)
        List<Permission> permissionList;
        List<String> permissionValueList = new ArrayList<>();
        if (permissionIdList.size() > 0) {
            QueryWrapper<Permission> permissionQW = new QueryWrapper<>();
            permissionQW.in(Permission.ID, permissionIdList)
                    .eq(Permission.IS_ENABLE, true)
                    .eq(Permission.IS_DELETE, false);
            permissionList = permissionMapper.selectList(permissionQW);
            permissionList.forEach(e -> permissionValueList.add(e.getPermission()));
        }
        //查询user_unit关系集合
        List<UserUnit> userUnitList;
        {
            QueryWrapper<UserUnit> userUnitQW = new QueryWrapper<>();
            userUnitQW.eq(UserUnit.USER_ID, user.getId());
            userUnitList = userUnitMapper.selectList(userUnitQW);
        }
        //查询unit集合
        Unit unit = null;
        {
            if (userUnitList.size() > 0) {
                QueryWrapper<Unit> unitQW = new QueryWrapper<>();
                unitQW.in(Unit.ID, userUnitList.stream().map(UserUnit::getUnitId).collect(Collectors.toList()))
                        .eq(Unit.IS_DELETE, false);
                unit = unitMapper.selectOne(unitQW);
            }
            if (unit == null) {
                unit = new Unit();
            }
        }

        //整合信息
        LoginDTO loginDTO = mapperFacade.map(user, LoginDTO.class);
        loginDTO.setRoleValueList(roleValueList);
        addSpecialPermission(permissionValueList, user);
        loginDTO.setPermissionValueList(permissionValueList);
        loginDTO.setUnitName(unit.getName());
        return loginDTO;
    }

    @Override
    public UserDetails loadUserById(Object id) throws BadCredentialsException {
        LoginDTO loginDTO = selectUserById((Integer) id);
        return transToUserDetails(loginDTO);
    }

    private UserDetails transToUserDetails(LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new BadCredentialsException("User not found");
        }
        List<String> authList = new ArrayList<String>() {{
            addAll(loginDTO.getRoleValueList().stream().map(e -> "ROLE_" + e).collect(Collectors.toSet()));
            addAll(loginDTO.getPermissionValueList());
        }};
        addSpecialPermission(authList, loginDTO);
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(String.valueOf(loginDTO.getId()), "", simpleGrantedAuthoritySet);//返回包括权限角色的User(此User为security提供的实体类)给security;
    }

    private void addSpecialPermission(Collection<String> authList, User user) {
        if (user.getIsAdmin()) {
            authList.add(SpecialPermission.ADMIN);
        }
        if (user.getIsBasic()) {
            authList.add(SpecialPermission.ADMIN);
            authList.add(SpecialPermission.BASIC);
        }
    }
}
