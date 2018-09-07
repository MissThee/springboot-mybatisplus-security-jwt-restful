package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.WellStateMan;
import server.security.JavaJWT;
import server.service.FunCLoginService;
import server.service.FunWellInfoService;
import server.service.FunWellStateManService;
import server.tool.Res;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

//油井信息管理
@RequestMapping("/wellStateMan")
@RestController("FunWellStateManController")
public class WellStateManController {

    @Autowired
    FunWellStateManService funWellStateManService;
    @Autowired
    FunWellInfoService funWellInfoService;
    @Autowired
    FunCLoginService funCLoginService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        String searchWellName = bodyJO.getString("searchWellName");
        Long searchAreaId = bodyJO.getLong("searchAreaId");
        Long searchStateTypeId = bodyJO.getLong("searchStateTypeId");
        Long searchMark = bodyJO.getLong("searchMark");
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        PageInfo pageInfo = funWellStateManService.selectWellStateManPaged(pageNum, pageSize, areaIds, searchWellName, searchAreaId, searchStateTypeId, searchMark);
        JSONObject jO = new JSONObject();
        jO.put("wellStateManList", pageInfo.getList());
        jO.put("wellStateManListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Long id = bodyJO.getLong("id");
        Boolean needWellList = false;
        if (bodyJO.containsKey("needWellList")) {
            needWellList = bodyJO.getBoolean("needWellList");
        }
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("wellStateMan", funWellStateManService.selectWellStateManOneById(id));
        }
        if (needWellList) {
            jO.put("wellNoConfigList", funWellInfoService.selectWellForListNotInWellStateMan(areaIds));
        }
        return Res.successData(jO);
    }


    @PutMapping()
    public Res create(HttpServletRequest httpServletRequest, @RequestHeader("Authorization") String token, @RequestBody JSONObject bodyJO) {
        WellStateMan wellStateMan = bodyJO.getJSONObject("wellStateMan").toJavaObject(WellStateMan.class);

        String userIp = httpServletRequest.getRemoteAddr();
        String userLoginName = funCLoginService.selectUserById(JavaJWT.getId(token)).getCLoginname();
        wellStateMan.setOpIpaddr(userIp);
        wellStateMan.setOpLoginname(userLoginName);
        if (wellStateMan.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        if (funWellStateManService.isWellStateManWellExist(wellStateMan.getWellId())) {
            return Res.failureMsg("该井配置已存在");
        }
        wellStateMan.setStime(new Date());
        Map<String, Object> map = funWellStateManService.createWellStateMan(wellStateMan);
        if (Boolean.getBoolean(map.get("result").toString())) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加" + map.get("msg").toString());
        }
    }


    @PatchMapping()
    public Res update(HttpServletRequest httpServletRequest, @RequestHeader("Authorization") String token, @RequestBody JSONObject bodyJO) {
        WellStateMan wellStateMan = bodyJO.getJSONObject("wellStateMan").toJavaObject(WellStateMan.class);
        if(wellStateMan.getMark()==0) {
            String userIp = httpServletRequest.getRemoteAddr();
            String userLoginName = funCLoginService.selectUserById(JavaJWT.getId(token)).getCLoginname();
            wellStateMan.setOpIpaddr0(userIp);
            wellStateMan.setOpLoginname0(userLoginName);
            wellStateMan.setEtime(new Date());
        }
        if (wellStateMan.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        wellStateMan.setWellId(null);
        wellStateMan.setWellName(null);
        wellStateMan.setAreaId(null);
        wellStateMan.setAreaName(null);
        wellStateMan.setStationId(null);
        wellStateMan.setStationName(null);
        if (funWellStateManService.updateWellStateMan(wellStateMan)) {
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
        if (funWellStateManService.deleteWellStateManByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
