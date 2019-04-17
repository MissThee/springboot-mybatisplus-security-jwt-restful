package com.github.base.controller.manage;

import com.github.base.vo.manage.RoleVO;
import com.github.common.db.entity.primary.manage.Role;
import com.github.common.service.interf.manage.RoleService;
import com.github.common.tool.Res;
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
    public Res<RoleVO.InsertOneRes> insertOne(@RequestBody RoleVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = roleService.isExist(insertOneReq.getRole());
        if (isDuplicate) {
            return Res.failure("角色值已存在");
        }
        Long id = roleService.insertOne(insertOneReq);
        return Res.res(id == null, new RoleVO.InsertOneRes().setId(id));
    }

    @ApiOperation(value = "删除角色（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody RoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除角色（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody RoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改角色", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody RoleVO.UpdateOneReq updateOneReq) {
        Boolean isDuplicateExceptSelf = roleService.isExistExceptSelf(updateOneReq.getRole(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("角色值已存在");
        }
        Boolean result = roleService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找角色（单个）", notes = "")
    @PostMapping()
    public Res<RoleVO.SelectOneRes> selectOne(@RequestBody RoleVO.SelectOneReq findOneReq) {
        Role role = roleService.selectOne(findOneReq.getId());
        return Res.success(new RoleVO.SelectOneRes().setRole(role));
    }

    @ApiOperation(value = "查找角色（多个）", notes = "")
    @PostMapping("/all")
    public Res<RoleVO.SelectListRes> selectList(@RequestBody RoleVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<Role> roleList = roleService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        return Res.success(new RoleVO.SelectListRes().setRoleList(roleList));
    }
}

