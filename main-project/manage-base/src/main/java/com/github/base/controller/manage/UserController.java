package com.github.base.controller.manage;

import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.service.interf.manage.UserService;
import com.github.base.vo.manage.UserVO;
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

@Api(tags = "基础管理-用户管理")
@ApiSort(1003)
@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'user'))")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "增加用户", notes = "")
    @ApiOperationSort(3)
    @PutMapping()
    public Res<UserVO.InsertOneRes> insertOne(@RequestBody UserVO.InsertOneReq insertOneReq) {
        Boolean isDuplicate = userService.isExist(insertOneReq.getUsername());
        if (isDuplicate) {
            return Res.failure("用户名已存在");
        }
        Long id = userService.insertOne(insertOneReq);
        return Res.res(id != null, new UserVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除用户（逻辑删除）", notes = "")
    @ApiOperationSort(5)
    @DeleteMapping()
    public Res deleteOne(@RequestBody UserVO.DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除用户（物理删除）[不用]", notes = "")
    @ApiOperationSort(6)
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody UserVO.DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改用户", notes = "")
    @ApiOperationSort(4)
    @PatchMapping()
    public Res updateOne(@RequestBody UserVO.UpdateOneReq updateOneReq) {
        Boolean result = userService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "重置密码", notes = "")
    @ApiOperationSort(7)
    @PatchMapping("/password/default")
    public Res resetDefaultPassword(@RequestBody UserVO.ResetDefaultPasswordReq resetDefaultPasswordReq) {
        Boolean result = userService.resetDefaultPassword(resetDefaultPasswordReq.getId());
        return Res.res(result, "重置" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找用户（单个） ", notes = "")
    @ApiOperationSort(2)
    @PostMapping()
    public Res<UserVO.SelectOneRes> selectOne(@RequestBody UserVO.SelectOneReq findOneReq) {
        SysUserInTableDetailDTO userInTableDetailBo = userService.selectOne(findOneReq.getId());
        return Res.res(userInTableDetailBo != null, new UserVO.SelectOneRes().setUser(userInTableDetailBo), userInTableDetailBo != null ? "" : "无此用户");
    }

    @ApiOperation(value = "查找用户（多个） ", notes = "")
    @ApiOperationSort(1)
    @PostMapping("/all")
    public Res<UserVO.SelectListRes> selectList(@RequestBody UserVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysUserInTableDTO> userInTableDTOList = userService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        return Res.success(new UserVO.SelectListRes().setUserList(userInTableDTOList));
    }
}

