package com.github.base.service.imp.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.base.db.mapper.primary.manage.*;
import com.github.base.dto.login.AuthDTO;
import com.github.base.service.interf.login.AuthInfoService;
import com.github.common.config.security.SpecialPermission;
import com.github.common.config.security.springsecurity.filter.UserInfoForSecurity;
import com.github.common.db.entity.primary.*;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthInfoImp implements AuthInfoService, UserInfoForSecurity {
    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysPermissionMapper permissionMapper;
    private final MapperFacade mapperFacade;
    private final SysUnitMapper unitMapper;
    private final SysUserUnitMapper userUnitMapper;

    @Autowired
    public AuthInfoImp(SysUserMapper userMapper, SysUserRoleMapper userRoleMapper, SysRoleMapper roleMapper, SysRolePermissionMapper rolePermissionMapper, SysPermissionMapper permissionMapper, MapperFacade mapperFacade, SysUnitMapper unitMapper, SysUserUnitMapper userUnitMapper) {
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
    public AuthDTO selectUserByUsername(String username) {
        //查找用户
        SysUser user;
        {
            QueryWrapper<SysUser> userQW = new QueryWrapper<>();
            userQW.eq(SysUser.USERNAME, username)
                    .eq(SysUser.IS_DELETE, false);
            user = userMapper.selectOne(userQW);
        }
        return buildLoginDTO(user);
    }

    @Override
    public AuthDTO selectUserById(String id) {
        SysUser user;
        {
            QueryWrapper<SysUser> userQW = new QueryWrapper<>();
            userQW.eq(SysUser.ID, id)
                    .eq(SysUser.IS_DELETE, false);
            user = userMapper.selectOne(userQW);
        }
        if (user == null) {
            throw new BadCredentialsException("无此账号信息");
        }
        return buildLoginDTO(user);
    }

    private AuthDTO buildLoginDTO(SysUser user) {
        if (user == null) {
            return null;
        }
        //查找角色id集(用户-角色关系表)
        List<Long> roleIdList = new ArrayList<>();
        {
            QueryWrapper<SysUserRole> userRoleQW = new QueryWrapper<>();
            userRoleQW.eq(SysUserRole.USER_ID, user.getId());
            List<SysUserRole> userRoleList = userRoleMapper.selectList(userRoleQW);
            userRoleList.forEach(e -> roleIdList.add(e.getRoleId()));
        }
        //查找角色信息集(角色表)
        List<SysRole> roleList;
        List<String> roleValueList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            QueryWrapper<SysRole> roleQW = new QueryWrapper<>();
            roleQW.in(SysRole.ID, roleIdList)
                    .eq(SysRole.IS_ENABLE, true)
                    .eq(SysRole.IS_DELETE, false);
            roleList = roleMapper.selectList(roleQW);
            roleList.forEach(e -> {
                if (!StringUtils.isEmpty(e.getRole())) {
                    roleValueList.add(e.getRole());
                }
            });
        }
        //查找权限id集(角色-权限关系表)
        List<Long> permissionIdList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            QueryWrapper<SysRolePermission> rolePermissionQW = new QueryWrapper<>();
            rolePermissionQW.in(SysRolePermission.ROLE_ID, roleIdList);
            List<SysRolePermission> rolePermissionList = rolePermissionMapper.selectList(rolePermissionQW);
            rolePermissionList.forEach(e -> permissionIdList.add(e.getPermissionId()));
        }
        //查找权限信息集(权限表)
        List<SysPermission> permissionList;
        List<String> permissionValueList = new ArrayList<>();
        if (permissionIdList.size() > 0) {
            QueryWrapper<SysPermission> permissionQW = new QueryWrapper<>();
            permissionQW.in(SysPermission.ID, permissionIdList)
                    .eq(SysPermission.IS_ENABLE, true)
                    .eq(SysPermission.IS_DELETE, false);
            permissionList = permissionMapper.selectList(permissionQW);
            permissionList.forEach(e -> {
                if (!StringUtils.isEmpty(e.getPermission())) {
                    permissionValueList.add(e.getPermission());
                }
            });
        }
        //查询user_unit关系集合
        List<SysUserUnit> userUnitList;
        {
            QueryWrapper<SysUserUnit> userUnitQW = new QueryWrapper<>();
            userUnitQW.eq(SysUserUnit.USER_ID, user.getId());
            userUnitList = userUnitMapper.selectList(userUnitQW);
        }
        //查询unit集合
        SysUnit unit = null;
        {
            if (userUnitList.size() > 0) {
                QueryWrapper<SysUnit> unitQW = new QueryWrapper<>();
                unitQW.in(SysUnit.ID, userUnitList.stream().map(SysUserUnit::getUnitId).collect(Collectors.toList()))
                        .eq(SysUnit.IS_DELETE, false);
                unit = unitMapper.selectOne(unitQW);
            }
            if (unit == null) {
                unit = new SysUnit();
            }
        }

        //整合信息
        AuthDTO authDTO = mapperFacade.map(user, AuthDTO.class);
        authDTO.setRoleValueList(roleValueList);
        addSpecialPermission(permissionValueList, user);
        authDTO.setPermissionValueList(permissionValueList);
        authDTO.setUnitName(unit.getName());
        return authDTO;
    }

    @Override
    public UserDetails loadUserById(Object id) throws BadCredentialsException {
        AuthDTO authDTO;
        try {
            authDTO = selectUserById(id.toString());
        } catch (Exception e) {
            throw new BadCredentialsException("用户信息异常");
        }
        return transToUserDetails(authDTO);
    }

    private UserDetails transToUserDetails(AuthDTO authDTO) {
        if (authDTO == null) {
            throw new BadCredentialsException("User not found");
        }
        List<String> authList = new ArrayList<String>() {{
            addAll(authDTO.getRoleValueList().stream().map(e -> "ROLE_" + e).collect(Collectors.toSet()));
            addAll(authDTO.getPermissionValueList());
        }};
        addSpecialPermission(authList, authDTO);
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authList.stream().filter(e -> !StringUtils.isEmpty(e)).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(String.valueOf(authDTO.getId()), "", simpleGrantedAuthoritySet);//返回包括权限角色的User(此User为security提供的实体类)给security;
    }

    private void addSpecialPermission(Collection<String> authList, SysUser user) {
        if (user.getIsAdmin()) {
            authList.add(SpecialPermission.ADMIN);
        }
        if (user.getIsBasic()) {
            authList.add(SpecialPermission.ADMIN);
            authList.add(SpecialPermission.BASIC);
        }
    }
}
