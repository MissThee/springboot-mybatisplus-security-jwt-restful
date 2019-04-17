package com.github.base.controller.manage;

import com.github.base.vo.manage.UnitVO;
import com.github.common.db.entity.primary.manage.Unit;
import com.github.common.service.interf.manage.UnitService;
import com.github.common.tool.Res;
import com.github.missthee.tool.datastructure.TreeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@Api(tags = "基础管理-组织结构管理")
@RestController
@RequestMapping("/unit")
//@PreAuthorize("isAuthenticated()")
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
    public Res<UnitVO.InsertOneRes> insertOne(@RequestBody UnitVO.InsertOneReq insertOneReq) {
        Long id = unitService.insertOne(insertOneReq);
        return Res.res(id == null, new UnitVO.InsertOneRes().setId(id));
    }

    @ApiOperation(value = "删除组织结构（逻辑删除）", notes = "")
    @DeleteMapping()
    public Res deleteOne(@RequestBody UnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = unitService.deleteOne(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "删除组织结构（物理删除）", notes = "")
    @DeleteMapping("/physical")
    public Res deleteOnePhysical(@RequestBody UnitVO.DeleteOneReq deleteOneReq) {
        Boolean result = unitService.deleteOnePhysical(deleteOneReq.getId());
        return Res.res(result);
    }

    @ApiOperation(value = "修改组织结构", notes = "")
    @PatchMapping()
    public Res updateOne(@RequestBody UnitVO.UpdateOneReq updateOneReq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Unit> unitList = unitService.selectList(true, null);
        List<Object> childIdList = TreeData.getChildIdList(unitList, updateOneReq.getId());
        if (childIdList.contains(updateOneReq.getParentId())) {
            return Res.failure("不能将本组织结构，放置到本组织结构的子节点之下");
        }
        Boolean result = unitService.updateOne(updateOneReq);
        return Res.res(result);
    }

    @ApiOperation(value = "查找组织结构（单个）", notes = "")
    @PostMapping()
    public Res<UnitVO.SelectOneRes> selectOne(@RequestBody UnitVO.SelectOneReq findOneReq) {
        Unit unit = unitService.selectOne(findOneReq.getId());
        return Res.success(new UnitVO.SelectOneRes().setUnit(unit));
    }

    @ApiOperation(value = "查找组织结构（树状）", notes = "")
    @PostMapping("/tree")
    public Res<UnitVO.SelectTreeRes> selectList(@RequestBody UnitVO.SelectTreeReq selectListReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        List<Unit> unitList = unitService.selectList(selectListReq.getIsDelete(), selectListReq.getOrderBy());
        List<Object> tree = TreeData.tree(unitList, selectListReq.getRootId(), false, new HashMap<String, String>() {{
            put("type", "type");
            put("name", "name");
        }});
        return Res.success(new UnitVO.SelectTreeRes().setUnitTree(tree));
    }
}
