package com.github.missthee.controller.dcinfo;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.ApplicationContextHolder;
import com.github.missthee.tool.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/db")
public class DBInfoController {
    @PostMapping("all")
    public Res getProperty123() {
        List<String> beanNameList = new ArrayList<String>() {{
            add("primaryDataSource");
            add("secondaryDataSource");
            add("activitiDataSource");
        }};
        for (String beanName : beanNameList) {
        }

        Map<String, DataSource> beansOfTypeMap = ApplicationContextHolder.getBeansOfType(DataSource.class);
        Map<String, String> collectMap = beansOfTypeMap.keySet().stream().collect(Collectors.toMap(e -> e, e -> String.valueOf(beansOfTypeMap.get(e)), (k1, k2) -> k1));
        return Res.success(collectMap);
    }
}
