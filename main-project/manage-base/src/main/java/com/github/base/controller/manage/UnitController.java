package com.github.base.controller.manage;

import com.github.base.service.interf.manage.SysUnitService;
import com.github.base.vo.manage.SysUnitVO;
import com.github.common.db.entity.primary.SysUnit;
import com.github.common.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "基础管理-组织结构管理")
@ApiSort(1000)
@RestController
@RequestMapping("/unit")
@PreAuthorize("isAuthenticated()")
public class UnitController {

    private final SysUnitService sysUnitService;
    private final MapperFacade mapperFacade;

    @Autowired
    public UnitController(SysUnitService sysUnitService, MapperFacade mapperFacade) {
        this.sysUnitService = sysUnitService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加组织结构", notes = "")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res<SysUnitVO.InsertOneRes> insertOne(@RequestBody @Valid SysUnitVO.InsertOneReq insertOneReq) {
        Long id = sysUnitService.insertOne(insertOneReq);
        return Res.res(id != null, new SysUnitVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除组织结构（逻辑删除）", notes = "")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res deleteOne(@RequestBody SysUnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysUnitService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除组织结构（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res deleteOnePhysical(@RequestBody SysUnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = sysUnitService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改组织结构", notes = "")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res updateOne(@RequestBody @Valid SysUnitVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<SysUnit> unitList = sysUnitService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(unitList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本组织结构，放置到本组织结构的子节点之下");
        }
        Boolean result = sysUnitService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找组织结构（单个）", notes = "")
    @PostMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res<SysUnitVO.SelectOneRes> selectOne(@RequestBody SysUnitVO.SelectOneReq findOneReq) {
        SysUnit unit = sysUnitService.selectOne(findOneReq.getId());
        return Res.success(new SysUnitVO.SelectOneRes().setUnit(unit));
    }

    @ApiOperation(value = "组织结构列表（树状）", notes = "")
    @PostMapping("/tree")
    public Res<SysUnitVO.SelectTreeRes> selectTree(@RequestBody SysUnitVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysUnit> unitList = sysUnitService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "type");
        map.put("name", "name");
        map.put("parentId", "parentId");
        map.put("indexNum", "indexNum");
        List<Object> tree = TreeData.tree(unitList, selectListReq.getRootId(), false, map);
        return Res.success(new SysUnitVO.SelectTreeRes().setUnitTree(tree));
    }

    @ApiOperation(value = "组织结构列表", notes = "")
    @PostMapping("/list")
    public Res<SysUnitVO.SelectListRes> selectList(@RequestBody SysUnitVO.SelectListReq selectListReq) {
        List<SysUnit> unitList = sysUnitService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        return Res.success(new SysUnitVO.SelectListRes().setUnitList(unitList));
    }
}
