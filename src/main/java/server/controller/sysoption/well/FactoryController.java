package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.FactoryInfo;
import server.service.FunAreaInfoService;
import server.service.FunFactoryInfoService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//厂信息
@RequestMapping("/factory")
@RestController("FunFactoryController")
public class FactoryController {

    @Autowired
    FunFactoryInfoService funFactoryInfoService;

    @PostMapping("/list")
    public Res list() {
        JSONObject jO = new JSONObject();
        jO.put("factoryList", funFactoryInfoService.selectFactoryForList("a11CodeFather"));
        return Res.successData(jO);
    }
}
