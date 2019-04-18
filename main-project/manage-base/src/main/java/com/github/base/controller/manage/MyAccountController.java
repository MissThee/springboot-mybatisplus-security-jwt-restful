package com.github.base.controller.manage;

import com.github.base.dto.manage.myaccount.MyAccountComparePasswordDTO;
import com.github.base.dto.manage.myaccount.MyAccountUpdatePasswordDTO;
import com.github.base.service.interf.manage.MyAccountService;
import com.github.base.service.interf.manage.UserService;
import com.github.base.vo.manage.MyAccountVO;
import com.github.base.vo.manage.UserVO;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "基础管理-个人账号管理")
@RestController
@RequestMapping("/myaccount")
//@PreAuthorize("isAuthenticated()")
public class MyAccountController {
    private final MyAccountService myAccountService;
    private final MapperFacade mapperFacade;

    @Autowired
    public MyAccountController(MyAccountService myAccountService, MapperFacade mapperFacade) {
        this.myAccountService = myAccountService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "修改用户", notes = "")
    @PatchMapping()
    public Res updateOne(HttpServletRequest httpServletRequest, @RequestBody MyAccountVO.UpdatePasswordReq updatePasswordReq) {
        String id = JavaJWT.getId(httpServletRequest);
        if (StringUtils.isEmpty(id)) {
            return Res.failure("无法获取当前用户信息，修改失败");
        }
        //检查输入的旧密码是否正确
        {
            MyAccountComparePasswordDTO myAccountComparePasswordDTO = mapperFacade.map(updatePasswordReq, MyAccountComparePasswordDTO.class);
            myAccountComparePasswordDTO.setId(Long.parseLong(id));
            if (!myAccountService.comparePassword(myAccountComparePasswordDTO)) {
                return Res.failure("旧密码不正确");
            }
        }
        //更新密码
        {
            MyAccountUpdatePasswordDTO myAccountUpdatePasswordDTO = mapperFacade.map(updatePasswordReq, MyAccountUpdatePasswordDTO.class);
            myAccountUpdatePasswordDTO.setId(Long.parseLong(id));
            Boolean result = myAccountService.updatePassword(myAccountUpdatePasswordDTO);
            return Res.res(result);
        }
    }
}
