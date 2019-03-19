package com.github.missthee.controller.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.missthee.test.staticproperty.test.TestModel;
import com.github.missthee.test.staticproperty.AStaticClass;
import com.github.missthee.tool.datastructure.TreeData;
import com.github.missthee.tool.excel.exports.bytemplate.ExcelExportByTemplate;
import com.github.missthee.tool.excel.imports.ExcelImport;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.github.missthee.config.security.jwt.JavaJWT;
import com.github.missthee.db.primary.model.basic.User;
import com.github.missthee.db.primary.model.compute.Compute;
import com.github.missthee.service.interf.basic.UserService;
import com.github.missthee.service.interf.compute.ComputeService;

import com.github.missthee.tool.FileRec;
import com.github.missthee.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import javax.naming.SizeLimitExceededException;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;


@ApiIgnore
@RestController
@RequestMapping("/test")
public class ExampleController {
    private final UserService userService;
    private final JavaJWT javaJWT;
    private final ComputeService computeService;
    private final DataSource primaryDataSource;

    @Autowired
    public ExampleController(UserService userService, JavaJWT javaJWT, ComputeService computeService, @Qualifier("primaryDataSource") DataSource dataSource) {

        this.userService = userService;
        this.javaJWT = javaJWT;
        this.computeService = computeService;
        this.primaryDataSource = dataSource;
    }

    @PostMapping("excel/output")
    public void excelTest(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException, ScriptException {
        //读取模板支持.xls（97-2003）或.xlsx（2007+）
        Workbook wb = ExcelExportByTemplate.readFile("exceltemplate/test.xls");
        //实体类
        TestModel testModel = new TestModel().setTest1("测试长文本长文本长文本长文本长文本长文本长文本长文本长文本");
        //参数：Workbook，工作簿编号（0开始），实体类，单元格自适应宽度
        ExcelExportByTemplate.simplePartialReplaceByPOJO(wb, 0, testModel, true);//使用${属性名}替换
        //流输出
        ExcelExportByTemplate.export(response, wb, "文件名");
    }

    @PostMapping("dbinfo")
    public String dbInfo() {
        return primaryDataSource.toString();
    }

    @PostMapping("getProperty")
    public Res getProperty() {
        Map<String, String> map = new HashMap<>();
        map.put("privateStaticString", AStaticClass.getPrivateStaticString());
        map.put("InnerStaticClass", AStaticClass.InnerStaticClass.getB());
        AStaticClass aStaticClass = new AStaticClass();
        aStaticClass.getPrivateString();
        aStaticClass.getPublicString();
        AStaticClass.InnerStaticClass innerStaticClass = new AStaticClass.InnerStaticClass();
        innerStaticClass.getA();
        return Res.success(map);
    }

    @PostMapping("setProperty")
    public Res setProperty() {
        AStaticClass.setPrivateStaticString(new Date().toString());
        AStaticClass.InnerStaticClass.setB(new Date().toString());
        return Res.success();
    }

    @PostMapping("error")
    public Res error() throws Exception {
        throw new Exception("A unknown exception");
    }

    @PostMapping("error1")
    public Res error1() throws Exception {
        Integer.parseInt("zz");
        return Res.success();
    }

    //获取当前用户相关信息。
    @PostMapping("infoByHeader")
    public Res<Map<String, Object>> getInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        String userIdByToken = javaJWT.getId(token);//通过token解析获得
        Object userIdBySubject = SecurityUtils.getSubject().getPrincipal();//通过shiro的subject获得
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("userIdByToken", userIdByToken);
            put("userIdBySubject", userIdBySubject);
        }};
        return Res.success(map);
    }

    //groupby测试(非标准扩展方法，不建议使用)。
    @PostMapping("groupBy")
    public Res<List<Compute>> getInfo() {
        List<Compute> computeList = computeService.selectGroupBy();
        return Res.success(computeList);
    }

    @PostMapping("addUser")
    public Res<JSONObject> addUser(@RequestBody JSONObject bJO) {
        String username = bJO.getString("username");
        String password = bJO.getString("password");
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        User user = new User();
        user.setUsername(username);
        String md5Password = new Md5Hash(password, salt, 3).toString();
        user.setPassword(md5Password);
        user.setNickname(username);
        user.setSalt(salt);
        int result = userService.insertOne(user);
        JSONObject jO = new JSONObject() {{
            put("result", result);
            put("user", user);
        }};
        return Res.res(result > 0, jO);
    }

    @PostMapping("alterUser")
    public Res<JSONObject> alterUser(@RequestBody JSONObject bJO) {
        Integer id = bJO.getInteger("id");
        int result = userService.alterOne(id);
        JSONObject jO = new JSONObject() {{
            put("result", result);
        }};
        return Res.res(result > 0, jO);
    }

    @RequestMapping("tree")
    public Res<JSONArray> getTree(@RequestParam("c") Boolean compareSelfId, @RequestParam("r") Integer rootId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TreeItem> li = new ArrayList<TreeItem>() {{
            add(new TreeItem(1, "name1", null));
            add(new TreeItem(2, "name2", 1));
            add(new TreeItem(3, "name3", 2));
            add(new TreeItem(4, "name4", 2));
            add(new TreeItem(5, "name5", null));
            add(new TreeItem(6, "name6", 2));
        }};
        JSONArray objects = TreeData.tree(li, rootId, compareSelfId == null ? false : compareSelfId, new HashMap<>());
        return Res.success(objects);
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    private class TreeItem {
        private Integer id;
        private String name;
        private Integer parentId;
    }

    //上传文件示例
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Res fileUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException, SizeLimitExceededException {
        String path = FileRec.fileUpload(file, "uploadTest");
        if (path != null) {
            return Res.success(path, "成功");
        } else {
            return Res.failure("失败");
        }
    }

    //上传文件示例
    @PostMapping(value = "/upload1")
    public Res fileUpload1(MultipartFile file, String customPath) throws FileNotFoundException, SizeLimitExceededException {
        String path = FileRec.fileUpload(file, customPath);
        if (path != null) {
            return Res.success(path, "成功");
        } else {
            return Res.failure("失败");
        }
    }

    //上传excel转为POJO
    @PostMapping(value = "/upload2")
    public Res fileUpload2(MultipartFile file) throws IOException, NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException {
        List<Object> objects = ExcelImport.excel2POJOList(file, User.class, new ArrayList<String>() {{
            add("nickname");
            add("username");
        }});
        return Res.success(objects);
    }
}
