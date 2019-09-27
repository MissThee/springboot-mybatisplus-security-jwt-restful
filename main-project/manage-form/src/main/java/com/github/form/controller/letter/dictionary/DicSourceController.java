package com.github.form.controller.letter.dictionary;

import com.github.common.db.entity.primary.DicSource;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonListDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.models.vo.letter.dictionary.*;
import com.github.form.service.interf.letter.dictionary.DicSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "数据字典-线索来源管理")
@ApiSort(1101)
@RestController
@RequestMapping("dic/source")
@PreAuthorize("isAuthenticated()")
public class DicSourceController {

    private final DicSourceService dicSourceService;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicSourceController(DicSourceService dicSourceService, MapperFacade mapperFacade) {
        this.dicSourceService = dicSourceService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加线索来源")
    @ApiOperationSort(2)
    @PutMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicSource'))")
    public Res<DicCreateOneResVO> insertOne(@RequestBody DicCreateOneReqVO dicCreateOneReqVO) {
        Integer id = dicSourceService.insertOne(mapperFacade.map(dicCreateOneReqVO, DicCommonInsertDTO.class));
        return Res.res(id != null, new DicCreateOneResVO().setId(id), "添加" + (id != null ? "成功" : "失败"));
    }

    @ApiOperation(value = "删除线索来源（逻辑删除）")
    @ApiOperationSort(4)
    @DeleteMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicSource'))")
    public Res deleteOne(@RequestBody DicDeleteOneReqVO dicDeleteOneReqVO) {
        Boolean result = dicSourceService.deleteOne(dicDeleteOneReqVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改线索来源")
    @ApiOperationSort(3)
    @PatchMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicSource'))")
    public Res updateOne(@RequestBody DicUpdateOneReqVO dicUpdateOneReqVO) {
        Boolean result = dicSourceService.updateOne(mapperFacade.map(dicUpdateOneReqVO, DicCommonUpdateDTO.class));
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "线索来源列表")
    @ApiOperationSort(1)
    @PostMapping("all")
    public Res<DicGetListResVO> selectList(@RequestBody DicGetListReqVO dicGetListReqVO) {
        List<DicSource> dicSourceList = dicSourceService.selectList(dicGetListReqVO.getIsDelete());
        List<DicCommonListDTO> dicCommonListDTOList = mapperFacade.mapAsList(dicSourceList, DicCommonListDTO.class);
        return Res.success(new DicGetListResVO().setDicCommonList(dicCommonListDTOList));
    }
}
