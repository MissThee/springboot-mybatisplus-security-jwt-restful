package com.github.form.controller.letter.dictionary;

import com.github.common.db.entity.primary.DicAreaInvolved;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonListDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.models.vo.letter.dictionary.*;
import com.github.form.service.interf.letter.dictionary.DicAreaInvolvedService;
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
@Api(tags = "数据字典-涉及领域管理")
@ApiSort(1105)
@RestController
@RequestMapping("dic/areainvolved")
@PreAuthorize("isAuthenticated()")
public class DicAreaInvolvedController {

    private final DicAreaInvolvedService dicAreaInvolvedService;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicAreaInvolvedController(DicAreaInvolvedService dicAreaInvolvedService, MapperFacade mapperFacade) {
        this.dicAreaInvolvedService = dicAreaInvolvedService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加涉及领域")
    @ApiOperationSort(2)
    @PutMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicAreaInvolved'))")
    public Res<DicCreateOneResVO> insertOne(@RequestBody DicCreateOneReqVO dicCreateOneReqVO) {
        Integer id = dicAreaInvolvedService.insertOne(mapperFacade.map(dicCreateOneReqVO, DicCommonInsertDTO.class));
        return Res.res(id != null, new DicCreateOneResVO().setId(id), "添加" + (id != null ?  "成功": "失败" ));
    }

    @ApiOperation(value = "删除涉及领域（逻辑删除）")
    @ApiOperationSort(4)
    @DeleteMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicAreaInvolved'))")
    public Res deleteOne(@RequestBody DicDeleteOneReqVO dicDeleteOneReqVO) {
        Boolean result = dicAreaInvolvedService.deleteOne(dicDeleteOneReqVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改涉及领域")
    @ApiOperationSort(3)
    @PatchMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicAreaInvolved'))")
    public Res updateOne(@RequestBody DicUpdateOneReqVO dicUpdateOneReqVO) {
        Boolean result = dicAreaInvolvedService.updateOne(mapperFacade.map(dicUpdateOneReqVO, DicCommonUpdateDTO.class));
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "涉及领域列表")
    @ApiOperationSort(1)
    @PostMapping("all")
    public Res<DicGetListResVO> selectList(@RequestBody DicGetListReqVO dicGetListReqVO) {
        List<DicAreaInvolved> dicAreaInvolvedList = dicAreaInvolvedService.selectList(dicGetListReqVO.getIsDelete());
        List<DicCommonListDTO> dicCommonListDTOList = mapperFacade.mapAsList(dicAreaInvolvedList, DicCommonListDTO.class);
        return Res.success(new DicGetListResVO().setDicCommonList(dicCommonListDTOList));
    }
}
