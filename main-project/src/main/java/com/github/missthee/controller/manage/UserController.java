package com.github.missthee.controller.manage;

import com.github.missthee.db.dto.manage.usercontroller.*;
import com.github.missthee.db.entity.primary.manage.User;
import com.github.missthee.service.interf.manage.UserService;
import com.github.missthee.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Api(tags = "基础管理-用户管理")
@RestController
@RequestMapping("/user")
//@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;
    private final MapperFacade mapperFacade;

    @Autowired
    public UserController(UserService userService, MapperFacade mapperFacade) {
        this.userService = userService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加用户", notes = "")
    @PutMapping()
    public Res<InsertOneRes> insertOne(@RequestBody InsertOneReq insertOneReq) {
        Boolean isDuplicate = userService.isDuplicate(insertOneReq.getUsername());
        if (isDuplicate) {
            return Res.failure("用户名已存在");
        }
        Long id = userService.insertOne(insertOneReq);
        return Res.res(id == null, new InsertOneRes(id));
    }

    @ApiOperation(value = "删除用户（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除用户（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改用户", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody UpdateOneReq updateOneReq) {
        Boolean result = userService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找用户（单个） ", notes = "")
    @PostMapping()
    public Res<SelectOneRes> selectOne(@RequestBody SelectOneReq findOneReq) {
        User user = userService.selectOne(findOneReq.getId());
        return Res.success(new SelectOneRes(user));
    }

    @ApiOperation(value = "查找用户（多个） ", notes = "")
    @PostMapping("/all")
    public Res<SelectListRes> selectList(@RequestBody SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<User> userList = userService.selectList(selectListReq);
        return Res.success(new SelectListRes(userList));
    }
}

