package com.github.form.controller.letter.dictionary;

import com.github.common.db.entity.primary.DicJobRank;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonListDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.models.vo.letter.dictionary.*;
import com.github.form.service.interf.letter.dictionary.DicJobRankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "数据字典-职级管理")
@ApiSort(1103)
@RestController
@RequestMapping("dic/jobrank")
@PreAuthorize("isAuthenticated() ")
public class DicJobRankController {

    private final DicJobRankService dicJobRankService;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicJobRankController(DicJobRankService dicJobRankService, MapperFacade mapperFacade) {
        this.dicJobRankService = dicJobRankService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "增加职级")
    @ApiOperationSort(2)
    @PutMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobRank'))")
    public Res<DicCreateOneResVO> insertOne(@RequestBody DicCreateOneReqVO dicCreateOneReqVO) {
        Integer id = dicJobRankService.insertOne(mapperFacade.map(dicCreateOneReqVO, DicCommonInsertDTO.class));
        return Res.res(id != null, new DicCreateOneResVO().setId(id), "添加" + (id != null ?  "成功": "失败" ));
    }

    @ApiOperation(value = "删除职级（逻辑删除）")
    @ApiOperationSort(4)
    @DeleteMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobRank'))")
    public Res deleteOne(@RequestBody DicDeleteOneReqVO dicDeleteOneReqVO) {
        Boolean result = dicJobRankService.deleteOne(dicDeleteOneReqVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "修改职级")
    @ApiOperationSort(3)
    @PatchMapping
    @PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'dicJobRank'))")
    public Res updateOne(@RequestBody DicUpdateOneReqVO dicUpdateOneReqVO) {
        Boolean result = dicJobRankService.updateOne(mapperFacade.map(dicUpdateOneReqVO, DicCommonUpdateDTO.class));
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "职级列表")
    @ApiOperationSort(1)
    @PostMapping("all")
    public Res<DicGetListResVO> selectList(@RequestBody DicGetListReqVO dicGetListReqVO) {
        List<DicJobRank> dicJobRankList = dicJobRankService.selectList(dicGetListReqVO.getIsDelete());
        List<DicCommonListDTO> dicCommonListDTOList = mapperFacade.mapAsList(dicJobRankList, DicCommonListDTO.class);
        return Res.success(new DicGetListResVO().setDicCommonList(dicCommonListDTOList));
    }
}
