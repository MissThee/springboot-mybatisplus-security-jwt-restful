package com.github.missthee.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.missthee.db.dto.manage.permissioncontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.permissioncontroller.SelectTreeReq;
import com.github.missthee.db.dto.manage.permissioncontroller.UpdateOneReq;
import com.github.missthee.db.mapper.primary.manage.PermissionMapper;
import com.github.missthee.db.po.primary.manage.Permission;
import com.github.missthee.service.interf.manage.PermissionService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PermissionImp extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final MapperFacade mapperFacade;
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionImp(PermissionMapper permissionMapper, MapperFacade mapperFacade) {
        this.permissionMapper = permissionMapper;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public Long insertOne(InsertOneReq insertOneReq) {
        Permission permission = mapperFacade.map(insertOneReq, Permission.class);
        permissionMapper.insert(permission);
        Long permissionId = permission.getId();
        return permissionId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Permission.ID, id)
                .eq(Permission.IS_DELETE, id);
        Boolean result = permissionMapper.delete(queryWrapper) > 0;
        return result;
    }

    @Override
    public Boolean updateOne(UpdateOneReq updateOneReq) {
        //拷贝用户信息，生成Permission对象
        Permission permission = mapperFacade.map(updateOneReq, Permission.class);
        //更新信息
        Boolean result = permissionMapper.updateById(permission) > 0;
        return result;
    }

    @Override
    public Permission selectOne(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public List<Permission> selectList(SelectTreeReq selectListReq) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (selectListReq.getIsDelete()) {//相较于user和role，permission不能仅显示已删除节点，因为仅已删除的节点不能构建完整的树结构
            queryWrapper.eq(Permission.IS_DELETE, selectListReq.getIsDelete());
        }
        for (Map.Entry<String, Boolean> entry : selectListReq.getOrderBy().entrySet()) {
            queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
        }
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return permissionMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isDuplicate(String permission) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Permission.PERMISSION, permission);
        return permissionMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Boolean isDuplicateExceptSelf(String permission, Long id) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Permission.PERMISSION, permission)
                .ne(Permission.ID, id);
        return permissionMapper.selectList(queryWrapper).size() > 0;
    }


}
