package com.github.base.controller.manage;

import com.github.base.service.interf.manage.SysPermissionService;
import com.github.base.vo.manage.SysPermissionVO;
import com.github.common.db.entity.primary.SysPermission;
import com.github.common.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "基础管理-权限管理")
@ApiSort(1001)
@RestController
@RequestMapping("/permission")
@PreAuthorize("isAuthenticated()")
public class PermissionController {

    private final SysPermissionService sysPermissionService;

    @Autowired
    public PermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    @ApiOperation(value = "增加权限", notes = "")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res<SysPermissionVO.InsertOneRes> insertOne(@RequestBody @Valid SysPermissionVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = sysPermissionService.isExist(insertOneReq.getPermission());
        if (isDuplicate) {
            return Res.failure("权限值已存在");
        }
        Long id = sysPermissionService.insertOne(insertOneReq);
        SysPermissionVO.InsertOneRes insertOneRes = new SysPermissionVO.InsertOneRes().setId(id);
        return Res.res(id != null, insertOneRes, "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除权限（逻辑删除）", notes = "")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res deleteOne(@RequestBody SysPermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysPermissionService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除权限（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res deleteOnePhysical(@RequestBody SysPermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysPermissionService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改权限", notes = "")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res updateOne(@RequestBody @Valid SysPermissionVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, NoSuchFieldException, InvalidAttributeValueException, IllegalAccessException, InvocationTargetException {
        Boolean isDuplicateExceptSelf = sysPermissionService.isExistExceptSelf(updateOneReq.getPermission(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("权限值已存在");
        }
        List<SysPermission> permissionList = sysPermissionService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(permissionList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本权限，放置到本权限的子节点之下");
        }
        Boolean result = sysPermissionService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找权限（单个）", notes = "")
    @PostMapping()
    public Res<SysPermissionVO.SelectOneRes> selectOne(@RequestBody SysPermissionVO.SelectOneReq findOneReq) {
        SysPermission permission = sysPermissionService.selectOne(findOneReq.getId());
        SysPermissionVO.SelectOneRes selectOneRes = new SysPermissionVO.SelectOneRes().setPermission(permission);
        return Res.success(selectOneRes);
    }

    @ApiOperation(value = "权限列表（树状）", notes = "")
    @PostMapping("/tree")
    public Res<SysPermissionVO.SelectTreeRes> selectList(@RequestBody SysPermissionVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysPermission> permissionList = sysPermissionService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        Map<String, String> map = new HashMap<>();
        map.put("type", "type");
        map.put("name", "name");
        map.put("permission", "permission");
        map.put("isEnable", "isEnable");
        map.put("isDelete", "isDelete");
        map.put("indexNum", "indexNum");
        map.put("parentId", "parentId");
        List<Object> tree = TreeData.tree(permissionList, selectListReq.getRootId(), false, map);
        SysPermissionVO.SelectTreeRes selectTreeRes = new SysPermissionVO.SelectTreeRes().setPermissionTree(tree);
        return Res.success(selectTreeRes);
    }
}

