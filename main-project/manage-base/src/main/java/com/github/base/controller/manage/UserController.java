package com.github.base.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.service.interf.manage.SysUserService;
import com.github.base.vo.manage.SysUserVO;
import com.github.common.db.entity.primary.SysUser;
import com.github.common.tool.Res;
import com.github.common.tool.SimplePageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Api(tags = "基础管理-用户管理")
@ApiSort(1003)
@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'user'))")
public class UserController {

    private final SysUserService sysUserService;

    @Autowired
    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @ApiOperation(value = "增加用户", notes = "")
    @PutMapping()
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public Res<SysUserVO.InsertOneRes> insertOne(@RequestBody @Valid SysUserVO.InsertOneReq insertOneReq) {
        {
            QueryWrapper<SysUser> qw = new QueryWrapper<>();
            qw.eq(SysUser.USERNAME, insertOneReq.getUsername());
            boolean isDuplicate = sysUserService.count(qw) >0;
            if (isDuplicate) {
                return Res.failure("用户名已存在");
            }
        }
        Long id = sysUserService.insertOne(insertOneReq);
        return Res.res(id != null, new SysUserVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除用户（逻辑删除）", notes = "")
    @DeleteMapping
    public Res deleteOne(@RequestBody SysUserVO.DeleteOneReq deleteOneReq) {
        SysUser sysUser= sysUserService.getById(deleteOneReq.getId());
        if(sysUser.getIsBasic()){
            return Res.failure("删除失败，无法删除系统基础账号");
        }
        Boolean result = sysUserService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除用户（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody SysUserVO.DeleteOneReq deleteOneReq) {
        SysUser sysUser= sysUserService.getById(deleteOneReq.getId());
        if(sysUser.getIsBasic()){
            return Res.failure("删除失败，无法删除系统基础账号");
        }
        Boolean result = sysUserService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改用户", notes = "")
    @PatchMapping
    @Transactional(rollbackFor = Exception.class, value = "primaryTransactionManager")
    public Res updateOne(@RequestBody @Valid SysUserVO.UpdateOneReq updateOneReq) {
        {
            QueryWrapper<SysUser> qw = new QueryWrapper<>();
            qw.ne(SysUser.ID,updateOneReq.getId());
            qw.eq(SysUser.USERNAME, updateOneReq.getUsername());
            boolean isDuplicate = sysUserService.count(qw) > 0;
            if (isDuplicate) {
                return Res.failure("用户名已存在");
            }
        }
        Boolean result = sysUserService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "重置密码", notes = "")
    @PatchMapping("/password/default")
    public Res resetDefaultPassword(@RequestBody SysUserVO.ResetDefaultPasswordReq resetDefaultPasswordReq) {
        Boolean result = sysUserService.resetDefaultPassword(resetDefaultPasswordReq.getId());
        return Res.res(result, "重置" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找用户（单个） ", notes = "")
    @PostMapping
    public Res<SysUserVO.SelectOneRes> selectOne(@RequestBody SysUserVO.SelectOneReq findOneReq) {
        SysUserInTableDetailDTO sysUserInTableDetailDTO = sysUserService.selectOne(findOneReq.getId());
        return Res.res(sysUserInTableDetailDTO != null, new SysUserVO.SelectOneRes().setUser(sysUserInTableDetailDTO), sysUserInTableDetailDTO != null ? "" : "无此用户");
    }

    @ApiOperation(value = "查找用户（多个） ", notes = "")
    @PostMapping("/all")
    public Res<SysUserVO.SelectListRes> selectList(@RequestBody SysUserVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        SimplePageInfo<SysUserInTableDTO> sysUserInTableDTOSimpolePageInfo = sysUserService.selectList(selectListReq.getPageNum(), selectListReq.getPageSize(), selectListReq.getIsDelete(), selectListReq.getOrderBy());
        return Res.success(new SysUserVO.SelectListRes()
                .setUserList(sysUserInTableDTOSimpolePageInfo.getList())
                .setTotal(sysUserInTableDTOSimpolePageInfo.getTotal())
        );
    }
}

