package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.db.mapper.primary.manage.SysRoleMapper;
import com.github.base.db.mapper.primary.manage.SysRolePermissionMapper;
import com.github.base.dto.manage.role.SysRoleInTableDetailDTO;
import com.github.base.service.interf.manage.SysRoleService;
import com.github.base.vo.manage.SysRoleVO;
import com.github.common.db.entity.primary.SysRole;
import com.github.common.db.entity.primary.SysRolePermission;
import com.github.common.tool.SimplePageInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class SysRoleImp extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final MapperFacade mapperFacade;
    private final SysRoleMapper roleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    public SysRoleImp(SysRoleMapper roleMapper, MapperFacade mapperFacade, SysRolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.mapperFacade = mapperFacade;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Long insertOne(SysRoleVO.InsertOneReq insertOneReq) {
        SysRole role = mapperFacade.map(insertOneReq, SysRole.class);
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
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRole.ID, id);
        Boolean result = roleMapper.updateById(new SysRole().setId(id).setIsDelete(true)) > 0;
        if (result) {
            updateRolePermission(null, id);
        }
        return result;
    }

    @Override
    public Boolean updateOne(SysRoleVO.UpdateOneReq updateOneReq) {
        //拷贝用户信息，生成Role对象
        SysRole role = mapperFacade.map(updateOneReq, SysRole.class);
        //更新信息
        Boolean result = roleMapper.updateById(role) > 0;
        if (result) {
            updateRolePermission(updateOneReq.getPermissionIdList(), role.getId());
        }
        return result;
    }

    @Override
    public SysRoleInTableDetailDTO selectOne(Long id) {
        SysRole sysRole = roleMapper.selectById(id);
        SysRoleInTableDetailDTO sysRoleInTableDetailDTO = mapperFacade.map(sysRole, SysRoleInTableDetailDTO.class);

        List<Long> permissionIdList;
        {//查找权限id集(角色-权限关系表)
            permissionIdList = new ArrayList<>();
            if (id != null) {
                QueryWrapper<SysRolePermission> rolePermissionQW = new QueryWrapper<>();
                rolePermissionQW.eq(SysRolePermission.ROLE_ID, id);
                List<SysRolePermission> rolePermissionList = rolePermissionMapper.selectList(rolePermissionQW);
                rolePermissionList.forEach(e -> permissionIdList.add(e.getPermissionId()));
            }
        }
        sysRoleInTableDetailDTO.setPermissionIdList(permissionIdList);
        return sysRoleInTableDetailDTO;
    }

    @Override
    public SimplePageInfo<SysRole> selectList(Integer pageNum,Integer pageSize,Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        List<SysRole> sysRoleList;
        Long total;
        {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SysRole.IS_DELETE, isDelete);
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
            }
            if (pageNum != null && pageSize != null) {
                PageHelper.startPage(pageNum, pageSize);
            } else {
                PageHelper.startPage(1, 0, true, null, true);
            }
            PageInfo<SysRole> pageInfo = new PageInfo<>(roleMapper.selectList(queryWrapper));
            sysRoleList = pageInfo.getList();
            total = pageInfo.getTotal();
        }
        return new SimplePageInfo<>(sysRoleList,total);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isExist(String role) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRole.ROLE, role);
        return roleMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean isExistExceptSelf(String role, Long id) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRole.ROLE, role)
                .ne(SysRole.ID, id);
        return roleMapper.selectList(queryWrapper).size() > 0;
    }

    private void updateRolePermission(List<Long> permissionIdList, Long roleId) {
        //清理关系
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRolePermission.ROLE_ID, roleId);
        rolePermissionMapper.delete(queryWrapper);
        //插入关系
        if (permissionIdList != null && permissionIdList.size() > 0) {
            for (Long permissionId : permissionIdList) {
                rolePermissionMapper.insert(new SysRolePermission().setRoleId(roleId).setPermissionId(permissionId));
            }
        }
    }
}
