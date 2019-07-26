package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.dto.manage.role.RoleInsertOneDTO;
import com.github.base.dto.manage.role.RoleUpdateOneDTO;
import com.github.base.dto.manage.role.RoleInTableDetailDTO;
import com.github.base.db.mapper.primary.manage.RoleMapper;
import com.github.base.db.mapper.primary.manage.RolePermissionMapper;
import com.github.common.db.entity.primary.Role;
import com.github.common.db.entity.primary.RolePermission;
import com.github.base.service.interf.manage.RoleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class RoleImp extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final MapperFacade mapperFacade;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Autowired
    public RoleImp(RoleMapper roleMapper, MapperFacade mapperFacade, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.mapperFacade = mapperFacade;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Long insertOne(RoleInsertOneDTO roleInsertOneDTO) {
        Role role = mapperFacade.map(roleInsertOneDTO, Role.class);
        roleMapper.insert(role);
        Long roleId = role.getId();
        if (roleId != null) {
            updateRolePermission(roleInsertOneDTO.getPermissionIdList(), role.getId());
        }
        return roleId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.ID, id);
        Boolean result = roleMapper.updateById(new Role().setId(id).setIsDelete(true)) > 0;
        if (result) {
            updateRolePermission(null, id);
        }
        return result;
    }

    @Override
    public Boolean updateOne(RoleUpdateOneDTO roleUpdateOneDTO) {
        //拷贝用户信息，生成Role对象
        Role role = mapperFacade.map(roleUpdateOneDTO, Role.class);
        //更新信息
        Boolean result = roleMapper.updateById(role) > 0;
        if (result) {
            updateRolePermission(roleUpdateOneDTO.getPermissionIdList(), role.getId());
        }
        return result;
    }

    @Override
    public RoleInTableDetailDTO selectOne(Long id) {
        Role role = roleMapper.selectById(id);
        RoleInTableDetailDTO roleInTableDetailDTO = mapperFacade.map(role, RoleInTableDetailDTO.class);
        List<Long> permissionIdList;
        {//查找权限id集(角色-权限关系表)
            permissionIdList = new ArrayList<>();
            if (id != null) {
                QueryWrapper<RolePermission> rolePermissionQW = new QueryWrapper<>();
                rolePermissionQW.eq(RolePermission.ROLE_ID, id);
                List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(rolePermissionQW);
                rolePermissionList.forEach(e -> permissionIdList.add(e.getPermissionId()));
            }
        }
        roleInTableDetailDTO.setPermissionIdList(permissionIdList);
        return roleInTableDetailDTO;
    }

    @Override
    public List<Role> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.IS_DELETE, isDelete);
        for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
            queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
        }
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isExist(String role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.ROLE, role);
        return roleMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean isExistExceptSelf(String role, Long id) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.ROLE, role)
                .ne(Role.ID, id);
        return roleMapper.selectList(queryWrapper).size() > 0;
    }

    private void updateRolePermission(List<Long> permissionIdList, Long roleId) {
        //清理关系
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RolePermission.ROLE_ID, roleId);
        rolePermissionMapper.delete(queryWrapper);
        //插入关系
        if (permissionIdList != null && permissionIdList.size() > 0) {
            for (Long permissionId : permissionIdList) {
                rolePermissionMapper.insert(new RolePermission().setRoleId(roleId).setPermissionId(permissionId));
            }
        }
    }
}
