package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.WellDataMaintian;
import server.security.JavaJWT;
import server.service.FunWellDataMaintianService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//单井数据维护
@RequestMapping("/wellDataMaintian")
@RestController("FunWellDataMaintianController")
public class WellDataMaintianController {
    @Autowired
    FunWellDataMaintianService funSingleWellService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        PageInfo pageInfo = funSingleWellService.selectWellPaged(pageNum, pageSize, areaIds);
        JSONObject jO = new JSONObject();
        jO.put("wellDataList", pageInfo.getList());
        jO.put("wellDataListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("wellData", funSingleWellService.selectWellOneById(id));
        }
        return Res.successData(jO);
    }

//    @PostMapping("/list")
//    public Res list() {
//        JSONObject jO = new JSONObject();
//        jO.put("wellList", funWellInfoService.selectWellForList());
//        return Res.successData(jO);
//    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        WellDataMaintian wellDataMaintian = bodyJO.getJSONObject("wellData").toJavaObject(WellDataMaintian.class);
        if (wellDataMaintian.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }

        if (funSingleWellService.isWellIdExist(wellDataMaintian.getWellId())) {
            return Res.failureMsg("该井号已存在");
        }
        wellDataMaintian.setStime(new Date());
        if (funSingleWellService.createWellData(wellDataMaintian)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }


    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        WellDataMaintian wellDataMaintian = bodyJO.getJSONObject("wellData").toJavaObject(WellDataMaintian.class);
        if (wellDataMaintian.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
//        if (funSingleWellService.isWellIdExistExceptSelf(wellDataMaintian.getId(), wellDataMaintian.getWellId())) {
//            return Res.failureMsg("该井号已存在在");
//        }
        if (funSingleWellService.updateWellData(wellDataMaintian)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @DeleteMapping()
    public Res delete(@RequestBody JSONObject bodyJO) {
        if (!bodyJO.containsKey("idList")) {
            return Res.failureMsg("删除失败，缺少[idList]");
        }
        List<Long> idList = bodyJO.getJSONArray("idList").toJavaList(Long.class);
        if (idList.size() == 0) {
            return Res.failureMsg("删除失败，未选中对象");
        }
        if (funSingleWellService.deleteWellByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
