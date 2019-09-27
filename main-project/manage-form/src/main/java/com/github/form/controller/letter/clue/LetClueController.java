package com.github.form.controller.letter.clue;


import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.db.entity.primary.LetClue;
import com.github.common.tool.Res;
import com.github.common.tool.SimplePageInfo;
import com.github.form.common.EnumLetClueResultType;
import com.github.form.controller.StuffController;
import com.github.form.models.dto.letter.clue.LetClueListResDTO;
import com.github.form.models.dto.letter.clue.LetClueResDTO;
import com.github.form.models.dto.letter.clue.LetClueCreateDTO;
import com.github.form.models.dto.letter.clue.LetClueUpdateDTO;
import com.github.form.service.imp.letter.clue.LetClueImp;
import com.github.form.service.interf.letter.clue.LetClueService;
import com.github.form.service.interf.letter.review.ReviewClueService;
import com.github.form.models.vo.letter.clue.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 信访表单 前端控制器
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Api(tags = "信访表单")
@RestController
@RequestMapping("letClue")
@PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'letClue') or hasPermission(null,'letClueType'))")
public class LetClueController {
    private final ReviewClueService reviewClueService;
    private final LetClueService letClueService;
    private final MapperFacade mapperFacade;

    public LetClueController(LetClueImp letClueService, ReviewClueService reviewClueService, MapperFacade mapperFacade) {
        this.letClueService = letClueService;
        this.reviewClueService = reviewClueService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "查询单个by id")
    @PostMapping
    public Res letClue(@RequestBody LetClueGetByIdVO letClueGetByIdVO) throws UnsupportedEncodingException {
        LetClueResDTO letClueResDTO = letClueService.selectOne(letClueGetByIdVO.getId());
        if (letClueResDTO == null) {
            return Res.failure("查询的内容不存在，id编号:" + letClueGetByIdVO.getId());
        } else {
            return Res.success(letClueResDTO);
        }
    }

    @ApiOperation(value = "查询列表")
    @PostMapping("list")
    public Res letClueList(@RequestBody @Validated LetClueGetListVO letClueGetListVO) {
        SimplePageInfo<LetClueListResDTO> letClueListResDTOPageInfo = letClueService.selectSimplePage(letClueGetListVO.getPageIndex(), letClueGetListVO.getPageSize(), letClueGetListVO.getResultTypeId(), letClueGetListVO.getStartReceptionTime(), letClueGetListVO.getEndReceptionTime(), letClueGetListVO.getContent(), letClueGetListVO.getDefendantName());
        return Res.success(letClueListResDTOPageInfo);
    }

    @ApiOperation(value = "添加信访表单")
    @PutMapping
    public Res createLetClue(HttpServletRequest httpServletRequest, @RequestBody @Validated LetClueCreateVO letClueCreateVO) throws Exception {
        LetClueCreateDTO letClueCreateDTO = mapperFacade.map(letClueCreateVO, LetClueCreateDTO.class);
        String userId = JavaJWT.getId(httpServletRequest);
        if (userId != null) {
            letClueCreateDTO.setCreatorId(Long.parseLong(userId));
        }
        letClueCreateDTO.setResultTypeId(EnumLetClueResultType.Default.getValue());
         letClueService.createOne(letClueCreateDTO);
        return Res.success("添加成功");
    }

    @ApiOperation(value = "修改信访表单")
    @PatchMapping
    public Res updateLetClue(@RequestBody LetClueUpdateVO letClueUpdateVO) throws Exception {
        LetClueUpdateDTO letClueUpdateDTO = mapperFacade.map(letClueUpdateVO, LetClueUpdateDTO.class);
        LetClue letClue = letClueService.getById(letClueUpdateVO.getId());
        if(letClue.getResultTypeId() == null || EnumLetClueResultType.getEnumByValue(letClue.getResultTypeId()).getIsEditableInLetClue()){
            letClueUpdateDTO.setResultTypeId(null);
            letClueService.updateLetClue(letClueUpdateDTO);
            return Res.success("修改成功");
        }else{
            return Res.failure("已经分发处理，不可修改");
        }
    }

    @ApiOperation(value = "删除信访表单")
    @DeleteMapping
    public Res deleteLetClue(@RequestBody @Validated LetClueDeleteVO letClueDeleteVO) {
        Boolean result = letClueService.deleteOne(letClueDeleteVO.getId());
        return Res.res(result, "删除" + (result ? "成功" : "失败"));
    }

    @ApiOperation(value = "信访表单分类(处理信访表单)")
    @PatchMapping("resultType")
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res classify(HttpServletRequest httpServletRequest, @RequestBody LetClueUpdateResultTypeVO letClueUpdateResultTypeVO) throws Exception {
        boolean result = true;
        if (EnumLetClueResultType.AnGuan.getValue().equals(letClueUpdateResultTypeVO.getResultTypeId())) {//固定值，类型为AnGuan时创建呈批笺
            result = reviewClueService.createOne(JavaJWT.getId(httpServletRequest), letClueUpdateResultTypeVO.getId());
        }
        if (result) {
            result = letClueService.updateLetClueResultType(letClueUpdateResultTypeVO.getId(), letClueUpdateResultTypeVO.getResultTypeId());
        }
        return Res.res(result, "修改" + (result ? "成功" : "失败"));
    }

    //上传文件
    @PostMapping(value = "/file")
    public Res fileUpload1(MultipartFile file, String customPath) throws IOException, SizeLimitExceededException {
        return new StuffController().fileUpload(file, customPath);
    }

}
