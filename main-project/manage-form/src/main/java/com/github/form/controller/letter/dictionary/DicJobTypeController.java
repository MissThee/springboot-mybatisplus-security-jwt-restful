package com.github.form.controller.letter.dictionary;

import com.github.common.db.entity.primary.DicJobType;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonListDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.models.vo.letter.dictionary.*;
import com.github.form.service.interf.letter.dictionary.DicJobTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@ApiIgnore
@Api(tags = "数据字典-干部类型管理")
@ApiSort(1102)
@RestController
@RequestMapping("dic/jobtype")
@PreAuthorize("isAuthenticated()")
public class DicJobTypeController {

    private final DicJobTypeService dicJobTypeService;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicJobTypeController(DicJobTypeService dicJobTypeService, MapperFacade mapperFacade) {
        this.dicJobTypeService = dicJobTypeService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加干部类型")
    @PutMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobType'))")
    public Res<DicCreateOneResVO> insertOne(@RequestBody DicCreateOneReqVO dicCreateOneReqVO) {
        Integer id = dicJobTypeService.insertOne(mapperFacade.map(dicCreateOneReqVO, DicCommonInsertDTO.class));
        return Res.res(id == null, new DicCreateOneResVO().setId(id), "添加" + (id == null ? "失败" : "成功"));
    }

    @ApiOperation(value = "删除干部类型（逻辑删除）")
    @DeleteMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobType'))")
    public Res deleteOne(@RequestBody DicDeleteOneReqVO dicDeleteOneReqVO) {
        Boolean result = dicJobTypeService.deleteOne(dicDeleteOneReqVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改干部类型")
    @PatchMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobType'))")
    public Res updateOne(@RequestBody DicUpdateOneReqVO dicUpdateOneReqVO) {
        Boolean result = dicJobTypeService.updateOne(mapperFacade.map(dicUpdateOneReqVO, DicCommonUpdateDTO.class));
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "干部类型列表")
    @PostMapping("all")
    public Res<DicGetListResVO> selectList(@RequestBody DicGetListReqVO dicGetListReqVO) {
        List<DicJobType> dicJobTypeList = dicJobTypeService.selectList(dicGetListReqVO.getIsDelete());
        List<DicCommonListDTO> dicCommonListDTOList = mapperFacade.mapAsList(dicJobTypeList, DicCommonListDTO.class);
        return Res.success(new DicGetListResVO().setDicCommonList(dicCommonListDTOList));
    }
}
