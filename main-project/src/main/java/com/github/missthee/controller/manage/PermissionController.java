package com.github.missthee.controller.manage;

import com.github.missthee.db.vo.manage.PermissionVO;
import com.github.missthee.db.entity.primary.manage.Permission;
import com.github.missthee.service.interf.manage.PermissionService;
import com.github.missthee.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@Api(tags = "基础管理-权限管理")
@RestController
@RequestMapping("/permission")
//@PreAuthorize("isAuthenticated()")
public class PermissionController {

    private final PermissionService permissionService;
    private final MapperFacade mapperFacade;

    @Autowired
    public PermissionController(PermissionService permissionService, MapperFacade mapperFacade) {
        this.permissionService = permissionService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加权限", notes = "")
    @PutMapping()
    public Res<PermissionVO.InsertOneRes> insertOne(@RequestBody PermissionVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = permissionService.isDuplicate(insertOneReq.getPermission());
        if (isDuplicate) {
            return Res.failure("权限值已存在");
        }
        Long id = permissionService.insertOne(insertOneReq);
        PermissionVO.InsertOneRes insertOneRes = new PermissionVO.InsertOneRes().setId(id);
        return Res.res(id == null, insertOneRes);
    }

    @ApiOperation(value = "删除权限（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody PermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除权限（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody PermissionVO.DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改权限", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody PermissionVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, NoSuchFieldException, InvalidAttributeValueException, IllegalAccessException, InvocationTargetException {
        Boolean isDuplicateExceptSelf = permissionService.isDuplicateExceptSelf(updateOneReq.getPermission(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("权限值已存在");
        }
        List<Permission> permissionList = permissionService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(permissionList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本权限，放置到本权限的子节点之下");
        }
        Boolean result = permissionService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找权限（单个）", notes = "")
    @PostMapping()
    public Res<PermissionVO.SelectOneRes> selectOne(@RequestBody PermissionVO.SelectOneReq findOneReq) {
        Permission permission = permissionService.selectOne(findOneReq.getId());
        PermissionVO.SelectOneRes selectOneRes = new PermissionVO.SelectOneRes().setPermission(permission);
        return Res.success(selectOneRes);
    }

    @ApiOperation(value = "查找权限（树状）", notes = "")
    @PostMapping("/tree")
    public Res<PermissionVO.SelectTreeRes> selectList(@RequestBody PermissionVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<Permission> permissionList = permissionService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        List<Object> tree = TreeData.tree(permissionList, selectListReq.getRootId(), false, new HashMap<String, String>() {{
            put("type", "type");
            put("name", "name");
            put("permission", "permission");
            put("isEnable", "isEnable");
            put("isDelete", "isDelete");
        }});
        PermissionVO.SelectTreeRes selectTreeRes = new PermissionVO.SelectTreeRes().setPermissionTree(tree);
        return Res.success(selectTreeRes);
    }
}

