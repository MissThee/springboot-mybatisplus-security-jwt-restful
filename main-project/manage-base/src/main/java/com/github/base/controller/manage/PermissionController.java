package com.github.base.controller.manage;

import com.github.base.service.interf.manage.PermissionService;
import com.github.base.vo.manage.PermissionVO;
import com.github.common.db.entity.primary.SysPermission;
import com.github.common.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@Api(tags = "基础管理-权限管理")
@ApiSort(1001)
@RestController
@RequestMapping("/permission")
@PreAuthorize("isAuthenticated()")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @ApiOperation(value = "增加权限", notes = "")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res<PermissionVO.InsertOneRes> insertOne(@RequestBody PermissionVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = permissionService.isExist(insertOneReq.getPermission());
        if (isDuplicate) {
            return Res.failure("权限值已存在");
        }
        Long id = permissionService.insertOne(insertOneReq);
        PermissionVO.InsertOneRes insertOneRes = new PermissionVO.InsertOneRes().setId(id);
        return Res.res(id != null, insertOneRes, "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除权限（逻辑删除）", notes = "")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res deleteOne(@RequestBody PermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除权限（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res deleteOnePhysical(@RequestBody PermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改权限", notes = "")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'permission'))")
    public Res updateOne(@RequestBody PermissionVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, NoSuchFieldException, InvalidAttributeValueException, IllegalAccessException, InvocationTargetException {
        Boolean isDuplicateExceptSelf = permissionService.isExistExceptSelf(updateOneReq.getPermission(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("权限值已存在");
        }
        List<SysPermission> permissionList = permissionService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(permissionList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本权限，放置到本权限的子节点之下");
        }
        Boolean result = permissionService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找权限（单个）", notes = "")
    @PostMapping()
    public Res<PermissionVO.SelectOneRes> selectOne(@RequestBody PermissionVO.SelectOneReq findOneReq) {
        SysPermission permission = permissionService.selectOne(findOneReq.getId());
        PermissionVO.SelectOneRes selectOneRes = new PermissionVO.SelectOneRes().setPermission(permission);
        return Res.success(selectOneRes);
    }

    @ApiOperation(value = "权限列表（树状）", notes = "")
    @PostMapping("/tree")
    public Res<PermissionVO.SelectTreeRes> selectList(@RequestBody PermissionVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysPermission> permissionList = permissionService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        List<Object> tree = TreeData.tree(permissionList, selectListReq.getRootId(), false, new HashMap<String, String>() {{
            put("type", "type");
            put("name", "name");
            put("permission", "permission");
            put("isEnable", "isEnable");
            put("isDelete", "isDelete");
            put("indexNum", "indexNum");
            put("parentId", "parentId");
        }});
        PermissionVO.SelectTreeRes selectTreeRes = new PermissionVO.SelectTreeRes().setPermissionTree(tree);
        return Res.success(selectTreeRes);
    }
}

