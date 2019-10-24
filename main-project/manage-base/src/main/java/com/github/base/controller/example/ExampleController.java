package com.github.base.controller.example;

import com.github.base.service.interf.transaction.OperateAllService;
import com.github.common.config.limiter.annotation.RLimit;
import com.github.missthee.tool.datastructure.TreeData;
import com.github.missthee.tool.excel.exports.bytemplate.ExcelExportByTemplate;
import com.github.missthee.tool.excel.imports.ExcelImport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.db.entity.primary.SysUser;

import com.github.common.tool.FileRec;
import com.github.common.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import javax.naming.SizeLimitExceededException;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@ApiIgnore
@RestController
@RequestMapping("/test")
public class ExampleController {
    private final OperateAllService operateAllService;

    public ExampleController(OperateAllService operateAllService) {
        this.operateAllService = operateAllService;
    }

    @PostMapping("excel/output")
    public void excelTest(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException, ScriptException {
        //读取模板支持.xls（97-2003）或.xlsx（2007+）
        Workbook wb = ExcelExportByTemplate.readExcel("exceltemplate/test.xls");
        //实体类
        TestModel testModel = new TestModel().setTest1("测试长文本长文本长文本长文本长文本长文本长文本长文本长文本");
        //参数：Workbook，工作簿编号（0开始），实体类，单元格自适应宽度
        ExcelExportByTemplate.simplePartialReplaceByPOJO(wb, 0, testModel, true);//使用${属性名}替换
        //流输出
        ExcelExportByTemplate.exportToResponse(response, wb, "文件名");
    }

    @RLimit(minTime = 10000)
    @RequestMapping("limit")
    public Res limit() {
        return Res.success("limit test");
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
    public Res<Map<String, Object>> getInfo(HttpServletRequest httpServletRequest) {
        String userIdByToken = JavaJWT.getId(httpServletRequest);//通过token解析获得
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();//通过Security的SecurityContextHolder获得
//        Object principal = SecurityUtils.getSubject().getPrincipal();//通过shiro的subject获得
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("userIdByToken", userIdByToken);
//            put("userIdBySubject", userIdBySubject);
        }};
        return Res.success(map);
    }

    @GetMapping("tree")
    public Res<Object> getTree(@RequestParam("c") Boolean compareSelfId, @RequestParam("r") Integer rootId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TreeItem> li = new ArrayList<TreeItem>() {{
            add(new TreeItem(1, "name1", null));
            add(new TreeItem(2, "name2", 1));
            add(new TreeItem(3, "name3", 2));
            add(new TreeItem(4, "name4", 2));
            add(new TreeItem(5, "name5", null));
            add(new TreeItem(6, "name6", 2));
        }};
        List<Object> objects = TreeData.tree(li, rootId, compareSelfId == null ? false : compareSelfId, new HashMap<>());
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
    public Res fileUpload(@RequestParam("file") MultipartFile file) throws IOException, SizeLimitExceededException {
        String path = FileRec.fileUpload(file, "uploadTest");
        if (path != null) {
            return Res.success(path, "成功");
        } else {
            return Res.failure("失败");
        }
    }

    //上传文件示例
    @PostMapping(value = "/upload1")
    public Res fileUpload1(MultipartFile file, String customPath) throws IOException, SizeLimitExceededException {
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
        List<Object> objects = ExcelImport.excel2POJOList(file.getInputStream(), SysUser.class, new ArrayList<String>() {{
            add("nickname");
            add("username");
        }});
        return Res.success(objects);
    }

    @Data
    @Accessors(chain = true)
    private static class TestModel {
        private String test1;
        private Date test2;
        private String test3 = "测试文字3";
        private String test4 = "123";
        private String test5 = null;
    }

    //多数据库事务测试（测试本方法需改用jta，否则找不到事务管理器）
    @GetMapping("/tran/{isOK}")
    @Transactional(rollbackFor = Exception.class)
    public Res tran(@PathVariable("isOK") String isOK) throws Exception {
        operateAllService.insertAll(isOK.equals("1"));
        return Res.success();
    }
}
