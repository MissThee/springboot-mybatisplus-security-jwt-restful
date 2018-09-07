package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.WellPipeInfo_Group;
import server.service.FunWellPipeService;
import server.tool.Res;

import java.util.List;

//管线走向
@RequestMapping("/wellPipe")
@RestController("FunPipeController")
public class WellPipeController {
    @Autowired
    FunWellPipeService funWellPipeService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO ) {
        JSONObject jO = new JSONObject();
//        List<WellPipeInfo> pipeInfoList=funWellPipeService.selectPipe()
//        jO.put("pipeLIst", funWellPipeService.selectPipe());
//        JSONArray jA=new JSONArray();
//        for(WellPipeInfo wellPipeInfo:pipeInfoList){
//
//        }

        List<WellPipeInfo_Group> pipeInfoList=funWellPipeService.selectPipeGroup();
        jO.put("pipeLIst", pipeInfoList);
        return Res.successData(jO);
    }
}
