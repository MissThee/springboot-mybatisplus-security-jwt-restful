package com.github.base.controller.manage;

import com.github.base.dto.manage.role.SysRoleInTableDetailDTO;
import com.github.base.service.interf.manage.SysRoleService;
import com.github.base.vo.manage.SysRoleVO;
import com.github.common.db.entity.primary.SysRole;
import com.github.common.tool.Res;
import com.github.common.tool.SimplePageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Api(tags = "基础管理-角色管理")
@ApiSort(1002)
@RestController
@RequestMapping("/role")
@PreAuthorize("isAuthenticated()")
public class RoleController {

    private final SysRoleService sysRoleService;

    @Autowired
    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @ApiOperation(value = "增加角色", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res<SysRoleVO.InsertOneRes> insertOne(@RequestBody @Valid SysRoleVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = sysRoleService.isExist(insertOneReq.getRole());
        if (isDuplicate) {
            return Res.failure("角色值已存在");
        }
        Long id = sysRoleService.insertOne(insertOneReq);
        return Res.res(id != null, new SysRoleVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除角色（逻辑删除）", notes = "")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res deleteOne(@RequestBody SysRoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysRoleService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除角色（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res deleteOnePhysical(@RequestBody SysRoleVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysRoleService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改角色", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res updateOne(@RequestBody @Valid SysRoleVO.UpdateOneReq updateOneReq) {
        Boolean isDuplicateExceptSelf = sysRoleService.isExistExceptSelf(updateOneReq.getRole(), updateOneReq.getId());
        if (isDuplicateExceptSelf) {
            return Res.failure("角色值已存在");
        }
        Boolean result = sysRoleService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找角色（单个）", notes = "其中权限列表使用【基础管理-权限管理】 → 【权限列表（树状）】")
    @PostMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'role'))")
    public Res<SysRoleVO.SelectOneRes> selectOne(@RequestBody  SysRoleVO.SelectOneReq findOneReq) {
        SysRoleInTableDetailDTO role = sysRoleService.selectOne(findOneReq.getId());
        return Res.success(new SysRoleVO.SelectOneRes().setRole(role));
    }

    @ApiOperation(value = "角色列表", notes = "")
    @PostMapping("/all")
    public Res<SysRoleVO.SelectListRes> selectList(@RequestBody SysRoleVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        SimplePageInfo<SysRole> roleSimplePageInfo = sysRoleService.selectList(
                selectListReq.getPageNum(),
                selectListReq.getPageSize(),
                selectListReq.getIsDelete(),
                selectListReq.getOrderBy());
        return Res.success(new SysRoleVO.SelectListRes()
                .setRoleList(roleSimplePageInfo.getList())
                .setTotal(roleSimplePageInfo.getTotal())
        );
    }
}

