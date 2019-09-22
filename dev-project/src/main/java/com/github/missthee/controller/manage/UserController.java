package com.github.missthee.controller.manage;

import com.github.missthee.vo.manage.UserControllerVO;
import com.github.missthee.db.entity.primary.manage.User;
import com.github.missthee.service.interf.manage.UserService;
import com.github.missthee.tool.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/user")
//@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping()
    public Res<UserControllerVO.InsertOneRes> insertOne(@RequestBody UserControllerVO.InsertOneReq insertOneReq) {
        Long id = userService.insertOne(insertOneReq);
        return Res.res(id == null, new UserControllerVO.InsertOneRes(id));
    }

    @DeleteMapping()
    public Res deleteOne(@RequestBody UserControllerVO.DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody UserControllerVO.DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @PatchMapping()
    public Res updateOne(@RequestBody UserControllerVO.UpdateOneReq updateOneReq) {
        Boolean result = userService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @PostMapping()
    public Res<UserControllerVO.SelectOneRes> selectOne(@RequestBody UserControllerVO.SelectOneReq findOneReq) {
        User user = userService.selectOne(findOneReq.getId());
        return Res.success(new UserControllerVO.SelectOneRes(user));
    }

    @PostMapping("/all")
    public Res<UserControllerVO.SelectListRes> selectList(@RequestBody UserControllerVO.SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<User> userList = userService.selectList(selectListReq);
        return Res.success(new UserControllerVO.SelectListRes(userList));
    }
}

