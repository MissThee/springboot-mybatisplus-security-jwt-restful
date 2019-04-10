package com.github.missthee.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.missthee.db.dto.manage.rolecontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.rolecontroller.SelectListReq;
import com.github.missthee.db.dto.manage.rolecontroller.UpdateOneReq;
import com.github.missthee.db.mapper.primary.manage.RoleMapper;
import com.github.missthee.db.mapper.primary.manage.RolePermissionMapper;
import com.github.missthee.db.entity.primary.manage.Role;
import com.github.missthee.db.entity.primary.manage.RolePermission;
import com.github.missthee.service.interf.manage.RoleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long insertOne(InsertOneReq insertOneReq) {
        Role role = mapperFacade.map(insertOneReq, Role.class);
        roleMapper.insert(role);
        Long roleId = role.getId();
        if (roleId != null) {
            updateRolePermission(insertOneReq.getPermissionIdList(), role.getId());
        }
        return roleId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.ID, id)
                .eq(Role.IS_DELETE, id);
        Boolean result = roleMapper.delete(queryWrapper) > 0;
        if (result) {
            updateRolePermission(null, id);
        }
        return result;
    }

    @Override
    public Boolean updateOne(UpdateOneReq updateOneReq) {
        //拷贝用户信息，生成Role对象
        Role role = mapperFacade.map(updateOneReq, Role.class);
        //更新信息
        Boolean result = roleMapper.updateById(role) > 0;
        if (result) {
            updateRolePermission(updateOneReq.getPermissionIdList(), role.getId());
        }
        return result;
    }

    @Override
    public Role selectOne(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public List<Role> selectList(SelectListReq selectListReq) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.IS_DELETE, selectListReq.getIsDelete());
        for (Map.Entry<String, Boolean> entry : selectListReq.getOrderBy().entrySet()) {
            queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
        }
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isDuplicate(String role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Role.ROLE, role);
        return roleMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean isDuplicateExceptSelf(String role, Long id) {
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
