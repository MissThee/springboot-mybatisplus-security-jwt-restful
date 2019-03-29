package com.github.missthee.controller.manage;

import com.github.missthee.db.dto.manage.usercontroller.*;
import com.github.missthee.db.po.primary.manage.User;
import com.github.missthee.service.interf.manage.UserService;
import com.github.missthee.tool.Res;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sun.invoke.empty.Empty;

import javax.management.InvalidAttributeValueException;
import javax.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Res<InsertOneRes> insertOne(@RequestBody InsertOneReq insertOneReq) {
        Long id = userService.insertOne(insertOneReq);
        return Res.res(id == null, new InsertOneRes(id));
    }

    @DeleteMapping()
    public Res deleteOne(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody DeleteOneReq deleteOneReq) {
        Boolean result = userService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @PatchMapping()
    public Res updateOne(@RequestBody UpdateOneReq updateOneReq) {
        Boolean result = userService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @PostMapping()
    public Res<SelectOneRes> selectOne(@RequestBody SelectOneReq findOneReq) {
        User user = userService.selectOne(findOneReq.getId());
        return Res.success(new SelectOneRes(user));
    }

    @PostMapping("/all")
    public Res<SelectListRes> selectList(@RequestBody SelectListReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<User> userList = userService.selectList(selectListReq);
        return Res.success(new SelectListRes(userList));
    }
}

