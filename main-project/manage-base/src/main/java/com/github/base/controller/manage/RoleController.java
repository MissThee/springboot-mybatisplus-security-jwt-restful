package com.github.base.controller.manage;

import com.github.base.dto.manage.role.SysRoleInTableDetailDTO;
import com.github.base.service.interf.manage.RoleService;
import com.github.base.vo.manage.RoleVO;
import com.github.common.db.entity.primary.SysRole;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Api(tags = "基础管理-角色管理")
@ApiSort(1002)
@RestController
@RequestMapping("/role")
@PreAuthorize("isAuthenticated()")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "增加角色", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @ApiOperationSort(3)
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res<RoleVO.InsertOneRes> insertOne(@RequestBody RoleVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = roleService.isExist(insertOneReq.getRole());
        if (isDuplicate) {
            return Res.failure("角色值已存在");
        }
        Long id = roleService.insertOne(insertOneReq);
        return Res.res(id != null, new RoleVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除角色（逻辑删除）", notes = "")
    @ApiOperationSort(5)
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res deleteOne(@RequestBody RoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除角色（物理删除）[不用]", notes = "")
    @ApiOperationSort(6)
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res deleteOnePhysical(@RequestBody RoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = roleService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改角色", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @ApiOperationSort(4)
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res updateOne(@RequestBody RoleVO.UpdateOneReq updateOneReq) {
        Boolean isDuplicateExceptSelf = roleService.isExistExceptSelf(updateOneReq.getRole(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("角色值已存在");
        }
        Boolean result = roleService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找角色（单个）", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @ApiOperationSort(2)
    @PostMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res<RoleVO.SelectOneRes> selectOne(@RequestBody RoleVO.SelectOneReq findOneReq) {
        SysRoleInTableDetailDTO role = roleService.selectOne(findOneReq.getId());
        return Res.success(new RoleVO.SelectOneRes().setRole(role));
    }

    @ApiOperation(value = "角色列表", notes = "")
    @ApiOperationSort(1)
    @PostMapping("/all")
    public Res<RoleVO.SelectListRes> selectList(@RequestBody RoleVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysRole> roleList = roleService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        return Res.success(new RoleVO.SelectListRes().setRoleList(roleList));
    }
}

