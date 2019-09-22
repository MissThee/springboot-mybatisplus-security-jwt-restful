//package com.github.missthee.controller.example;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.github.missthee.config.security.jwt.JavaJWT;
//import com.github.missthee.db.po.model.basic.User;
//import com.github.missthee.db.po.model.compute.Compute;
//import com.github.missthee.service.interf.basic.UserService;
//import com.github.missthee.service.interf.compute.ComputeService;
//import staticproperty.AStaticClass;
//import staticproperty.test.TestModel;
//import com.github.missthee.tool.FileRec;
//import com.github.missthee.tool.Res;
//import com.github.missthee.tool.datastructure.TreeData;
//import com.github.missthee.tool.excel.exports.bytemplate.ExcelExportByTemplate;
//import com.github.missthee.tool.excel.imports.ExcelImport;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authz.annotation.*;
//import org.apache.shiro.crypto.SecureRandomNumberGenerator;
//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.naming.SizeLimitExceededException;
//import javax.script.ScriptException;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.*;
//
//
//@ApiIgnore
////权限访问测试
//@RestController
//@RequestMapping("/authShiro")
//@RequiresRoles({"role"})//相同注解同为@RequiresRoles或@RequiresPermissions等，方法上注解会覆盖类上注解。
//public class AuthShiroController {
//
//    @RequestMapping("/everyone")
//    public Res everyone() {
//        return Res.success("WebController：everyone");
//    }
//
//    @RequestMapping("/user")
//    @RequiresUser//因为订制rememberMe功能，作用同@RequiresAuthentication
//    public Res user() {
//        return Res.success("WebController：user");
//    }
//
//    @RequestMapping("/guest")
//    @RequiresGuest
//    public Res guest() {
//        return Res.success("WebController：guest");
//    }
//
//    @RequestMapping("/require_auth")
//    @RequiresAuthentication
//    public Res requireAuth() {
//        return Res.success("WebController：You are authenticated");
//    }
//
//    @RequestMapping("/require_role1")
//    @RequiresRoles({"role1"})
//    public Res requireRole1() {
//        return Res.success("WebController：You are visiting require_role12 [role1&role2]");
//    }
//
//    @RequestMapping("/require_role3")
//    @RequiresRoles("role3")
//    public Res requireRole3() {
//        return Res.success("WebController：You are visiting require_role [role3]");
//    }
//
//    @RequestMapping("/require_role_permission")
//    @RequiresPermissions({"admin:view"})
//    public Res requireRolePermission() {
//        return Res.success("WebController：You are visiting permission require admin:view");
//    }
//
//    @RequestMapping("/require_permission")
//    @RequiresPermissions({"view", "edit"})
//    public Res requirePermission() {
//        return Res.success("WebController：You are visiting permission require edit,view");
//    }
//
//}
