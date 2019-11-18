package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.db.mapper.primary.manage.*;
import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.dto.manage.user.SysUserInsertOneDTO;
import com.github.base.dto.manage.user.SysUserUpdateOneDTO;
import com.github.base.service.interf.manage.SysUserService;
import com.github.common.config.security.jwt.UserInfoForJWT;
import com.github.common.db.entity.primary.*;
import com.github.common.tool.SimplePageInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysUserImp extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserInfoForJWT {
    private final MapperFacade mapperFacade;
    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserUnitMapper userUnitMapper;
    private final SysRoleMapper roleMapper;
    private final SysUnitMapper unitMapper;

    @Autowired
    public SysUserImp(SysUserMapper userMapper, MapperFacade mapperFacade, SysUserRoleMapper userRoleMapper, SysUserUnitMapper userUnitMapper, SysRoleMapper roleMapper, SysUnitMapper unitMapper) {
        this.userMapper = userMapper;
        this.mapperFacade = mapperFacade;
        this.userRoleMapper = userRoleMapper;
        this.userUnitMapper = userUnitMapper;
        this.roleMapper = roleMapper;
        this.unitMapper = unitMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public Long insertOne(SysUserInsertOneDTO userInsertOneDTO) {
        SysUser user = mapperFacade.map(userInsertOneDTO, SysUser.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
        Long userId = user.getId();
        if (userId != null) {
            updateUserRole(user.getId(), userInsertOneDTO.getRoleIdList().toArray(new Long[0]));
            updateUserUnit(user.getId(), userInsertOneDTO.getUnitId());
        }
        return userId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        Boolean result = userMapper.updateById(new SysUser().setId(id).setIsDelete(true)) > 0;
        if (result) {
            updateUserRole(id);
            updateUserUnit(id);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public Boolean updateOne(SysUserUpdateOneDTO updateOneReq) {
        //拷贝用户信息，生成User对象
        SysUser user = mapperFacade.map(updateOneReq, SysUser.class);
        //更新信息
        Boolean result = userMapper.updateById(user) > 0;
        if (result) {
            updateUserRole(user.getId(), updateOneReq.getRoleIdList().toArray(new Long[0]));
            updateUserUnit(user.getId(), updateOneReq.getUnitId());
        }
        return result;
    }

    @Override
    public SysUserInTableDetailDTO selectOne(Long id) {
        //查询user
        SysUser user = userMapper.selectById(id);
        SysUserInTableDetailDTO userInTableDetailBo = null;
        if (user != null) {
            //查询role id集合
            List<Long> roleIdList;
            {
                QueryWrapper<SysUserRole> userRoleQW = new QueryWrapper<>();
                userRoleQW.eq(SysUserRole.USER_ID, id);
                List<SysUserRole> userRoleList = userRoleMapper.selectList(userRoleQW);
                roleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            }
            //查询unit id
            Long unitId = null;
            {
                QueryWrapper<SysUserUnit> userUnitQW = new QueryWrapper<>();
                userUnitQW.eq(SysUserUnit.USER_ID, id);
                SysUserUnit userUnit = userUnitMapper.selectOne(userUnitQW);
                if (userUnit != null) {
                    unitId = userUnit.getUnitId();
                }
            }

            //整合进UserInTableDetailBo对象中
            userInTableDetailBo = mapperFacade.map(user, SysUserInTableDetailDTO.class);
            userInTableDetailBo.setRoleIdList(roleIdList);
            userInTableDetailBo.setUnitId(unitId);
        }
        return userInTableDetailBo;
    }

    @Override
    public SimplePageInfo<SysUserInTableDTO> selectList(Integer pageNum, Integer pageSize, Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        List<SysUser> userList;
        Long total;
        {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SysUser.IS_DELETE, isDelete);
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
            }
            if (pageNum != null && pageSize != null) {
                PageHelper.startPage(pageNum, pageSize);
            } else {
                PageHelper.startPage(1, 0, true, null, true);
            }
            PageInfo<SysUser> pageInfo = new PageInfo<>(userMapper.selectList(queryWrapper));
            userList = pageInfo.getList();
            total = pageInfo.getTotal();
        }
        List<Long> userIdList = userList.stream().map(SysUser::getId).collect(Collectors.toList());
        //查询涉及到的user_role关系集合
        List<SysUserRole> userRoleList = new ArrayList<>();
        if (userIdList.size() > 0) {
            QueryWrapper<SysUserRole> userRoleQW = new QueryWrapper<>();
            userRoleQW.in(SysUserRole.USER_ID, userIdList);
            userRoleList = userRoleMapper.selectList(userRoleQW);
        }
        //查询涉及到的role集合
        Map<Long, SysRole> roleMap = new HashMap<>();
        if (userRoleList.size() > 0) {
            QueryWrapper<SysRole> roleQW = new QueryWrapper<>();
            roleQW.in(SysRole.ID, userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()))
                    .eq(SysRole.IS_DELETE, false);
            List<SysRole> roleList = roleMapper.selectList(roleQW);
            roleMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, e -> e));
        }
        //查询user_unit关系集合
        List<SysUserUnit> userUnitList = new ArrayList<>();
        if (userIdList.size() > 0) {
            QueryWrapper<SysUserUnit> userUnitQW = new QueryWrapper<>();
            userUnitQW.in(SysUserUnit.USER_ID, userIdList);
            userUnitList = userUnitMapper.selectList(userUnitQW);
        }
        //查询unit集合
        Map<Long, SysUnit> unitMap = new HashMap<>();
        if (userUnitList.size() > 0) {
            QueryWrapper<SysUnit> unitQW = new QueryWrapper<>();
            unitQW.in(SysUnit.ID, userUnitList.stream().map(SysUserUnit::getUnitId).collect(Collectors.toList()))
                    .eq(SysUnit.IS_DELETE, false);
            List<SysUnit> unitList = unitMapper.selectList(unitQW);
            unitMap = unitList.stream().collect(Collectors.toMap(SysUnit::getId, e -> e));
        }
        //整合user，role，unit相关信息
        List<SysUserInTableDTO> userInTableBoList = mapperFacade.mapAsList(userList, SysUserInTableDTO.class);
        for (SysUserInTableDTO item : userInTableBoList) {
            List<SysUserInTableDTO.RoleInfo> roleInfoList = new ArrayList<>();
            for (SysUserRole userRole : userRoleList) {
                if (item.getId().equals(userRole.getUserId())) {
                    Long roleId = userRole.getRoleId();
                    if (roleMap.containsKey(roleId)) {
                        SysRole role = roleMap.get(roleId);
                        SysUserInTableDTO.RoleInfo roleInfo = mapperFacade.map(role, SysUserInTableDTO.RoleInfo.class);
                        roleInfoList.add(roleInfo);
                    }
                }
            }
            item.setRoleList(roleInfoList);
            for (SysUserUnit userUnit : userUnitList) {
                if (item.getId().equals(userUnit.getUserId())) {
                    Long unitId = userUnit.getUnitId();
                    SysUnit unit = unitMap.getOrDefault(unitId, new SysUnit());
                    item.setUnitName(unit.getName());
                }
            }
        }
        return new SimplePageInfo<>(userInTableBoList, total);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isExist(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.USERNAME, username);
        return userMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean resetDefaultPassword(Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public String getSecret(Object userId) {
        if (userId == null || "".equals(userId.toString())) {
            throw new BadCredentialsException("empty userId, when get secret");
        }
        SysUser user = userMapper.selectById(Long.valueOf(userId.toString()));
        if (user == null) {
            throw new BadCredentialsException("can't find user, when get secret");
        }
        return user.getPassword();

    }

    private void updateUserRole(Long userId, Long... roleIdList) {
        //清理关系
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUserRole.USER_ID, userId);
        userRoleMapper.delete(queryWrapper);
        //插入关系
        if (roleIdList != null && roleIdList.length > 0) {
            for (Long roleId : roleIdList) {
                userRoleMapper.insert(new SysUserRole().setUserId(userId).setRoleId(roleId));
            }
        }
    }

    private void updateUserUnit(Long userId, Long... unitIdList) {
        //清理关系
        QueryWrapper<SysUserUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUserUnit.USER_ID, userId);
        userUnitMapper.delete(queryWrapper);
        //插入关系
        if (unitIdList != null && unitIdList.length > 0) {
            for (Long unitId : unitIdList) {
                userUnitMapper.insert(new SysUserUnit().setUserId(userId).setUnitId(unitId));
            }
        }
    }
}
