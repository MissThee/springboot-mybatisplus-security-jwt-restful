package com.github.base.controller.manage;

import com.github.base.service.interf.manage.MyAccountService;
import com.github.base.vo.manage.MyAccountVO;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "基础管理-个人账号管理")
@ApiSort(1004)
@RestController
@RequestMapping("/myaccount")
@PreAuthorize("isAuthenticated()")//and (hasPermission(null,'[ADMIN]') or hasPermission(null,'[]'))
public class MyAccountController {
    private final MyAccountService myAccountService;
    private final JavaJWT javaJWT;

    @Autowired
    public MyAccountController(MyAccountService myAccountService, JavaJWT javaJWT) {
        this.myAccountService = myAccountService;
        this.javaJWT = javaJWT;
    }

    @ApiOperation(value = "修改个人密码", notes = "")
    @PatchMapping()
    public Res updateOne(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody MyAccountVO.UpdatePasswordReq updatePasswordReq) {
        String id = JavaJWT.getId(httpServletRequest);
        if (StringUtils.isEmpty(id)) {
            return Res.failure("无法获取当前用户信息，修改失败");
        }
        //检查输入的旧密码是否正确
        if (!myAccountService.comparePassword(Long.parseLong(id), updatePasswordReq.getOldPassword())) {
            return Res.failure("旧密码不正确");
        }
        //更新密码
        Boolean result = myAccountService.updatePassword(Long.parseLong(id), updatePasswordReq.getNewPassword());
        try {
            httpServletResponse.setHeader("Authorization", javaJWT.createToken(Long.parseLong(id), 7));  //添加token
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }
}
