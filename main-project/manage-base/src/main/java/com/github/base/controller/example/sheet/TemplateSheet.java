package com.github.base.controller.example.sheet;

import com.github.missthee.tool.excel.exports.bytemplate.ExcelExportByTemplate;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping("/test")
@Slf4j
public class TemplateSheet {

    @PostMapping("excel/output")
    public void excelTest(HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException, ScriptException {
        //读取模板支持.xls（97-2003）或.xlsx（2007+）
        Workbook wb = ExcelExportByTemplate.readExcel("exceltemplate/test.xls");
        //实体类
        TestModel testModel = new TestModel().setTest1("测试长文本长文本长文本长文本长文本长文本长文本长文本长文本");
        //参数：Workbook，工作簿编号（0开始），实体类，单元格自适应宽度
        ExcelExportByTemplate.simplePartialReplaceByPOJO(wb, 0, testModel, true);//使用${属性名}替换
        //参数：Workbook，工作簿编号（0开始），实体类集合，需要插入的列集合，实体类类型，单元格自适应宽度
        List<DataModel> dataModelList = new ArrayList<>();
        dataModelList.add(new DataModel());
        dataModelList.add(new DataModel().setFengLi("11111111111111111111111111"));
        dataModelList.add(new DataModel().setFengLi("2"));
        dataModelList.add(new DataModel().setFengLi("3"));
        dataModelList.add(new DataModel().setFengLi("4"));
        dataModelList.add(new DataModel().setFengLi("5"));
        List<String> propertyList = new ArrayList<>();
        propertyList.add("tianQi");//要插入DataModel的属性名，依次添加
        propertyList.add("wenDu");
        propertyList.add("fengLi");
        ExcelExportByTemplate.simpleReplaceByPOJOList(wb, 0, dataModelList, propertyList, DataModel.class, true);//使用$[实体类名]替换
        //流输出
        ExcelExportByTemplate.exportToResponse(response, wb, "文件名");
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

    @Data
    @Accessors(chain = true)
    public static class DataModel {
        private String tianQi = "天气良好";
        private String wenDu = "温度适中";
        private String fengLi = "1级风";
    }
}
