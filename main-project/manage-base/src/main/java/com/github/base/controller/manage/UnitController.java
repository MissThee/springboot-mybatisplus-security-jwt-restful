package com.github.base.controller.manage;

import com.github.base.service.interf.manage.UnitService;
import com.github.base.vo.manage.UnitVO;
import com.github.common.db.entity.primary.SysUnit;
import com.github.common.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@Api(tags = "基础管理-组织结构管理")
@ApiSort(1000)
@RestController
@RequestMapping("/unit")
@PreAuthorize("isAuthenticated()")
public class UnitController {

    private final UnitService unitService;
    private final MapperFacade mapperFacade;
    @Autowired
    public UnitController(UnitService unitService, MapperFacade mapperFacade) {
        this.unitService = unitService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加组织结构", notes = "")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res<UnitVO.InsertOneRes> insertOne(@RequestBody UnitVO.InsertOneReq insertOneReq) {
        Long id = unitService.insertOne(insertOneReq);
        return Res.res(id != null, new UnitVO.InsertOneRes().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除组织结构（逻辑删除）", notes = "")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res deleteOne(@RequestBody UnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = unitService.deleteOne(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除组织结构（物理删除）[不用]", notes = "")
    @DeleteMapping("/physical")
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res deleteOnePhysical(@RequestBody UnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = unitService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改组织结构", notes = "")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res updateOne(@RequestBody UnitVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<SysUnit> unitList = unitService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(unitList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本组织结构，放置到本组织结构的子节点之下");
        }
        Boolean result = unitService.updateOne(updateOneReq);
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "查找组织结构（单个）", notes = "")
    @PostMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'unit'))")
    public Res<UnitVO.SelectOneRes> selectOne(@RequestBody UnitVO.SelectOneReq findOneReq) {
        SysUnit unit = unitService.selectOne(findOneReq.getId());
        return Res.success(new UnitVO.SelectOneRes().setUnit(unit));
    }

    @ApiOperation(value = "组织结构列表（树状）", notes = "")
    @PostMapping("/tree")
    public Res<UnitVO.SelectTreeRes> selectList(@RequestBody UnitVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<SysUnit> unitList = unitService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        List<Object> tree = TreeData.tree(unitList, selectListReq.getRootId(), false, new HashMap<String, String>() {{
            put("type", "type");
            put("name", "name");
            put("parentId", "parentId");
            put("indexNum", "indexNum");
        }});
        return Res.success(new UnitVO.SelectTreeRes().setUnitTree(tree));
    }
}
