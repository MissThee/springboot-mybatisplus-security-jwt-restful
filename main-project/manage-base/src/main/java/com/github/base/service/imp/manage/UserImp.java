package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.common.db.entity.primary.*;
import com.github.common.config.security.jwt.UserInfoForJWT;
import com.github.base.dto.manage.user.UserInTableDTO;
import com.github.base.dto.manage.user.UserInTableDetailDTO;
import com.github.base.dto.manage.user.UserInsertOneDTO;
import com.github.base.dto.manage.user.UserUpdateOneDTO;
import com.github.base.db.mapper.primary.manage.*;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.github.base.service.interf.manage.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Exception.class})
public class UserImp extends ServiceImpl<UserMapper, User> implements UserService, UserInfoForJWT {
    private final MapperFacade mapperFacade;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserUnitMapper userUnitMapper;
    private final RoleMapper roleMapper;
    private final UnitMapper unitMapper;

    @Autowired
    public UserImp(UserMapper userMapper, MapperFacade mapperFacade, UserRoleMapper userRoleMapper, UserUnitMapper userUnitMapper, RoleMapper roleMapper, UnitMapper unitMapper) {
        this.userMapper = userMapper;
        this.mapperFacade = mapperFacade;
        this.userRoleMapper = userRoleMapper;
        this.userUnitMapper = userUnitMapper;
        this.roleMapper = roleMapper;
        this.unitMapper = unitMapper;
    }

    @Override
    public Long insertOne(UserInsertOneDTO insertOneReq) {
        User user = mapperFacade.map(insertOneReq, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
        Long userId = user.getId();
        if (userId != null) {
            updateUserRole(user.getId(), insertOneReq.getRoleIdList().toArray(new Long[0]));
            updateUserUnit(user.getId(), insertOneReq.getUnitId());
        }
        return userId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        Boolean result = userMapper.updateById(new User().setId(id).setIsDelete(true)) > 0;
        if (result) {
            updateUserRole(id);
            updateUserUnit(id);
        }
        return result;
    }

    @Override
    public Boolean updateOne(UserUpdateOneDTO updateOneReq) {
        //拷贝用户信息，生成User对象
        User user = mapperFacade.map(updateOneReq, User.class);
        //更新信息
        Boolean result = userMapper.updateById(user) > 0;
        if (result) {
            updateUserRole(user.getId(), updateOneReq.getRoleIdList().toArray(new Long[0]));
            updateUserUnit(user.getId(), updateOneReq.getUnitId());
        }
        return result;
    }

    @Override
    public UserInTableDetailDTO selectOne(Long id) {
        //查询user
        User user = userMapper.selectById(id);
        //查询role id集合
        List<Long> roleIdList;
        {
            QueryWrapper<UserRole> userRoleQW = new QueryWrapper<>();
            userRoleQW.eq(UserRole.USER_ID, id);
            List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQW);
            roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        }
        //查询unit id
        Long unitId;
        {
            QueryWrapper<UserUnit> userUnitQW = new QueryWrapper<>();
            userUnitQW.eq(UserUnit.USER_ID, id);
            UserUnit userUnit = userUnitMapper.selectOne(userUnitQW);
            unitId = userUnit.getUnitId();
        }
        //整合进UserInTableDetailBo对象中
        UserInTableDetailDTO userInTableDetailBo = mapperFacade.map(user, UserInTableDetailDTO.class);
        userInTableDetailBo.setRoleIdList(roleIdList);
        userInTableDetailBo.setUnitId(unitId);
        return userInTableDetailBo;
    }

    @Override
    public List<UserInTableDTO> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        List<User> userList;
        {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(User.IS_DELETE, isDelete);
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
            }
            userList = userMapper.selectList(queryWrapper);
        }
        List<Long> userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
        //查询涉及到的user_role关系集合
        List<UserRole> userRoleList = new ArrayList<>();
        if (userIdList.size() > 0) {
            QueryWrapper<UserRole> userRoleQW = new QueryWrapper<>();
            userRoleQW.in(UserRole.USER_ID, userIdList);
            userRoleList = userRoleMapper.selectList(userRoleQW);
        }
        //查询涉及到的role集合
        Map<Long, Role> roleMap = new HashMap<>();
        if (userRoleList.size() > 0) {
            QueryWrapper<Role> roleQW = new QueryWrapper<>();
            roleQW.in(Role.ID, userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()))
                    .eq(Role.IS_DELETE, false);
            List<Role> roleList = roleMapper.selectList(roleQW);
            roleMap = roleList.stream().collect(Collectors.toMap(Role::getId, e -> e));
        }
        //查询user_unit关系集合
        List<UserUnit> userUnitList = new ArrayList<>();
        if (userIdList.size() > 0) {
            QueryWrapper<UserUnit> userUnitQW = new QueryWrapper<>();
            userUnitQW.in(UserUnit.USER_ID, userIdList);
            userUnitList = userUnitMapper.selectList(userUnitQW);
        }
        //查询unit集合
        Map<Long, Unit> unitMap = new HashMap<>();
        if (userUnitList.size() > 0) {
            QueryWrapper<Unit> unitQW = new QueryWrapper<>();
            unitQW.in(Unit.ID, userUnitList.stream().map(UserUnit::getUnitId).collect(Collectors.toList()))
                    .eq(Unit.IS_DELETE, false);
            List<Unit> unitList = unitMapper.selectList(unitQW);
            unitMap = unitList.stream().collect(Collectors.toMap(Unit::getId, e -> e));
        }
        //整合user，role，unit相关信息
        List<UserInTableDTO> userInTableBoList = mapperFacade.mapAsList(userList, UserInTableDTO.class);
        for (UserInTableDTO item : userInTableBoList) {
            List<UserInTableDTO.RoleInfo> roleInfoList = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                if (item.getId().equals(userRole.getUserId())) {
                    Long roleId = userRole.getRoleId();
                    Role role = roleMap.getOrDefault(roleId, new Role());
                    UserInTableDTO.RoleInfo roleInfo = mapperFacade.map(role, UserInTableDTO.RoleInfo.class);
                    roleInfoList.add(roleInfo);
                }
            }
            item.setRoleList(roleInfoList);
            for (UserUnit userUnit : userUnitList) {
                if (item.getId().equals(userUnit.getUserId())) {
                    Long unitId = userUnit.getUnitId();
                    Unit unit = unitMap.getOrDefault(unitId, new Unit());
                    item.setUnitName(unit.getName());
                }
            }
        }
        return userInTableBoList;
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isExist(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.USERNAME, username);
        return userMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean resetDefaultPassword(Long id) {
        User user = new User();
        user.setId(id);
        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public String getSecret(Object userId) {
        if (userId == null || "".equals(userId.toString())) {
            throw new BadCredentialsException("empty userId, when get secret");
        }
        User user = userMapper.selectById(Long.valueOf(userId.toString()));
        if (user == null) {
            throw new BadCredentialsException("can't find user, when get secret");
        }
        return user.getPassword();

    }

    private void updateUserRole(Long userId, Long... roleIdList) {
        //清理关系
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserRole.USER_ID, userId);
        userRoleMapper.delete(queryWrapper);
        //插入关系
        if (roleIdList != null && roleIdList.length > 0) {
            for (Long roleId : roleIdList) {
                userRoleMapper.insert(new UserRole().setUserId(userId).setRoleId(roleId));
            }
        }
    }

    private void updateUserUnit(Long userId, Long... unitIdList) {
        //清理关系
        QueryWrapper<UserUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserUnit.USER_ID, userId);
        userUnitMapper.delete(queryWrapper);
        //插入关系
        if (unitIdList != null && unitIdList.length > 0) {
            for (Long unitId : unitIdList) {
                userUnitMapper.insert(new UserUnit().setUserId(userId).setUnitId(unitId));
            }
        }
    }
}
