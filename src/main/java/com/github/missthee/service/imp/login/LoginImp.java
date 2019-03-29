package com.github.missthee.service.imp.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.missthee.config.security.security.filter.UserInfoForSecurity;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.github.missthee.db.mapper.primary.manage.*;
import com.github.missthee.db.po.primary.manage.*;
import com.github.missthee.db.bo.login.LoginBO;
import com.github.missthee.service.interf.login.LoginService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginImp implements LoginService, UserInfoForSecurity {
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
    public LoginBO selectUserByUsername(String username) {
        //查找用户
        User user;
        {
            QueryWrapper<User> userQW = new QueryWrapper<>();
            userQW.eq(User.USERNAME, username);
            user = userMapper.selectOne(userQW);
        }
        return buildLoginDTO(user);
    }

    @Override
    public LoginBO selectUserById(Integer id) {
        //查找用户
        User user;
        {
            user = userMapper.selectById(id);
            if (user == null) {
                throw new BadCredentialsException("无此账号信息");
            }
        }
        return buildLoginDTO(user);
    }

    private LoginBO buildLoginDTO(User user) {
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
        Set<String> roleValueList = new HashSet<>();
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
        Set<String> permissionValueList = new HashSet<>();
        if (permissionIdList.size() > 0) {
            QueryWrapper<Permission> permissionQW = new QueryWrapper<>();
            permissionQW.in(Permission.ID, permissionIdList)
                    .eq(Permission.IS_ENABLE, true)
                    .eq(Permission.IS_DELETE, false);
            permissionList = permissionMapper.selectList(permissionQW);
            permissionList.forEach(e -> permissionValueList.add(e.getPermission()));
        }
        LoginBO loginDTO = mapperFacade.map(user, LoginBO.class);
        loginDTO.setRoleValueList(roleValueList);
        loginDTO.setPermissionValueList(permissionValueList);
        return loginDTO;
    }

    @Override
    public UserDetails loadUserById(Object id) throws BadCredentialsException {
        LoginBO loginDTO = selectUserById((Integer) id);
        return transToUserDetails(loginDTO);
    }

    private UserDetails transToUserDetails(LoginBO loginDTO) {
        if (loginDTO == null) {
            throw new BadCredentialsException("User not found");
        }
        List<String> authList = new ArrayList<String>() {{
            addAll(loginDTO.getRoleValueList().stream().map(e -> "ROLE_" + e).collect(Collectors.toSet()));
            addAll(loginDTO.getPermissionValueList());
        }};
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(String.valueOf(loginDTO.getId()), "", simpleGrantedAuthoritySet);//返回包括权限角色的User(此User为security提供的实体类)给security;
    }
}
