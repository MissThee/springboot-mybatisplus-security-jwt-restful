package com.github.missthee.controller.manage;

import com.github.missthee.db.dto.manage.rolecontroller.*;
import com.github.missthee.db.po.primary.manage.Role;
import com.github.missthee.service.interf.manage.RoleService;
import com.github.missthee.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Api(tags = "基础管理-角色管理")
@RestController
@RequestMapping("/role")
//@PreAuthorize("isAuthenticated()")
public class RoleController {

    private final RoleService roleService;
    private final MapperFacade mapperFacade;

    @Autowired
    public RoleController(RoleService roleService, MapperFacade mapperFacade) {
        this.roleService = roleService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加角色", notes = "")
    @PutMapping()
    public Res<InsertOneRes> insertOne(@RequestBody InsertOneReq insertOneReq) {
        Boolean isDuplicate = roleService.isDuplicate(insertOneReq.getRole());
        if (isDuplicate) {
            return Res.failure("角色值已存在");
        }
        Long id = roleService.insertOne(insertOneReq);
        return Res.res(id == null, new InsertOneRes(id));
    }

    @ApiOperation(value = "删除角色（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除角色（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改角色", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody UpdateOneReq updateOneReq) {
        Boolean isDuplicateExceptSelf = roleService.isDuplicateExceptSelf(updateOneReq.getRole(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("角色值已存在");
        }
        Boolean result = roleService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找角色（单个）", notes = "")
    @PostMapping()
    public Res<SelectOneRes> selectOne(@RequestBody SelectOneReq findOneReq) {
        Role role = roleService.selectOne(findOneReq.getId());
        return Res.success(new SelectOneRes(role));
    }

    @ApiOperation(value = "查找角色（多个）", notes = "")
    @PostMapping("/all")
    public Res<SelectListRes> selectList(@RequestBody SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<Role> roleList = roleService.selectList(selectListReq);
        return Res.success(new SelectListRes(roleList));
    }
}

