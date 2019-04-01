package com.github.missthee.controller.manage;

import com.alibaba.fastjson.JSONArray;
import com.github.missthee.db.dto.manage.permissioncontroller.*;
import com.github.missthee.db.po.primary.manage.Permission;
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
    public Res<InsertOneRes> insertOne(@RequestBody InsertOneReq insertOneReq) {
        Boolean isDuplicate = permissionService.isDuplicate(insertOneReq.getPermission());
        if (isDuplicate) {
            return Res.failure("权限值已存在");
        }
        Long id = permissionService.insertOne(insertOneReq);
        return Res.res(id == null, new InsertOneRes(id));
    }

    @ApiOperation(value = "删除权限（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除权限（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = permissionService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改权限", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody UpdateOneReq updateOneReq) {
        Boolean isDuplicateExceptSelf = permissionService.isDuplicateExceptSelf(updateOneReq.getPermission(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("角色值已存在");
        }
        Boolean result = permissionService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找权限（单个）", notes = "")
    @PostMapping()
    public Res<SelectOneRes> selectOne(@RequestBody SelectOneReq findOneReq) {
        Permission permission = permissionService.selectOne(findOneReq.getId());
        return Res.success(new SelectOneRes(permission));
    }

    @ApiOperation(value = "查找权限（树状）", notes = "")
    @PostMapping("/tree")
    public Res<SelectTreeRes> selectList(@RequestBody SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<Permission> permissionList = permissionService.selectList(selectListReq);
        List<Object> tree = TreeData.tree(permissionList, selectListReq.getRootId(), false, new HashMap<String, String>() {{
            put("type", "type");
            put("name", "name");
            put("value", "value");
            put("isEnable", "isEnable");
            put("isDelete", "isDelete");
        }});
        return Res.success(new SelectTreeRes(tree));
    }
}

