package com.github.form.controller.letter.dictionary;


import com.github.common.db.entity.primary.DicResultType;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonListDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.models.vo.letter.dictionary.*;
import com.github.form.service.interf.letter.dictionary.DicResultTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 信访信访室处理结果 前端控制器
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-28
 */
@Api(tags = "数据字典-处理类型")
@RestController
@RequestMapping("dic/resulttype")
@PreAuthorize("isAuthenticated() ")
public class DicResultTypeController {
    private final DicResultTypeService dicResultTypeService;
    private final MapperFacade mapperFacade;

    public DicResultTypeController(DicResultTypeService dicResultTypeService, MapperFacade mapperFacade) {
        this.dicResultTypeService = dicResultTypeService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加处理类型")
    @PutMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicResultType'))")
    public Res<DicCreateOneResVO> insertOne(@RequestBody DicCreateOneReqVO dicCreateOneReqVO) {
        Integer id = dicResultTypeService.insertOne(mapperFacade.map(dicCreateOneReqVO, DicCommonInsertDTO.class));
        return Res.res(id != null, new DicCreateOneResVO().setId(id), "添加" + (id != null ?  "成功": "失败" ));
    }

    @ApiOperation(value = "删除处理类型（逻辑删除）")
    @DeleteMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicResultType'))")
    public Res deleteOne(@RequestBody DicDeleteOneReqVO dicDeleteOneReqVO) {
        Boolean result = dicResultTypeService.deleteOne(dicDeleteOneReqVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改处理类型")
    @PatchMapping()
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicResultType'))")
    public Res updateOne(@RequestBody DicUpdateOneReqVO dicUpdateOneReqVO) {
        Boolean result = dicResultTypeService.updateOne(mapperFacade.map(dicUpdateOneReqVO, DicCommonUpdateDTO.class));
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "处理类型列表")
    @PostMapping("all")
    public Res<DicGetListResVO> selectList(@RequestBody DicGetListReqVO dicGetListReqVO) {
        List<DicResultType> dicResultTypes = dicResultTypeService.selectList(dicGetListReqVO.getIsDelete());
        List<DicCommonListDTO> dicCommonListDTOList = mapperFacade.mapAsList(dicResultTypes, DicCommonListDTO.class);
        return Res.success(new DicGetListResVO().setDicCommonList(dicCommonListDTOList));
    }

}
