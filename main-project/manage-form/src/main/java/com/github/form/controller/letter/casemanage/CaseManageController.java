package com.github.form.controller.letter.casemanage;

import com.github.base.service.interf.manage.UnitService;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.db.entity.primary.CaseManage;
import com.github.common.db.entity.primary.LetClue;
import com.github.common.db.entity.primary.SysUnit;
import com.github.common.tool.Res;
import com.github.common.tool.SimplePageInfo;
import com.github.form.common.EnumCaseManageState;
import com.github.form.common.EnumLetClueResultType;
import com.github.form.models.dto.letter.casemanage.CaseManageDeptDTO;
import com.github.form.models.dto.letter.casemanage.CaseManageListResDTO;
import com.github.form.models.dto.letter.casemanage.CaseManageResDTO;
import com.github.form.models.dto.letter.clue.LetClueCreateDTO;
import com.github.form.models.dto.letter.clue.LetClueResDTO;
import com.github.form.models.dto.letter.clue.LetClueUpdateDTO;
import com.github.form.models.vo.letter.casemanage.CaseManageGetByIdVO;
import com.github.form.models.vo.letter.casemanage.CaseManageGetListVO;
import com.github.form.models.vo.letter.casemanage.CaseManageRefuseVO;
import com.github.form.models.vo.letter.casemanage.CaseManageUpdateVO;
import com.github.form.models.vo.letter.clue.LetClueCreateVO;
import com.github.form.models.vo.letter.clue.LetClueGetByIdVO;
import com.github.form.models.vo.letter.clue.LetClueUpdateVO;
import com.github.form.service.interf.casemanage.CaseManageService;
import com.github.form.service.interf.letter.clue.LetClueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "信访表单")
@RestController
@RequestMapping("caseManage")
@PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'caseManage'))")
public class CaseManageController {
    private final MapperFacade mapperFacade;
    private final CaseManageService caseManageService;
    private final UnitService unitService;
    private final LetClueService letClueService;

    public CaseManageController(CaseManageService caseManageService, MapperFacade mapperFacade, UnitService unitService, LetClueService letClueService) {
        this.caseManageService = caseManageService;
        this.mapperFacade = mapperFacade;
        this.unitService = unitService;
        this.letClueService = letClueService;
    }

    @ApiOperation(value = "查询列表")
    @PostMapping("list")
    public Res<SimplePageInfo<CaseManageListResDTO>> caseManageList(@RequestBody @Validated CaseManageGetListVO caseManageGetListVO) {
        SimplePageInfo<CaseManageListResDTO> caseManageListResDTOPageInfo = caseManageService.selectSimplePage(caseManageGetListVO.getPageIndex(), caseManageGetListVO.getPageSize(), caseManageGetListVO.getLetClueId(), caseManageGetListVO.getDefendantName(), caseManageGetListVO.getContent(), caseManageGetListVO.getStartReceptionTime(), caseManageGetListVO.getEndReceptionTime(), caseManageGetListVO.getLeftDayNum());
        return Res.success(caseManageListResDTOPageInfo);
    }

    @ApiOperation(value = "查询单个by id")
    @PostMapping()
    public Res<CaseManageResDTO> getCaseManageById(@RequestBody @Validated CaseManageGetByIdVO caseManageGetByIdVO) {
        CaseManage caseManage = caseManageService.getById(caseManageGetByIdVO.getId());
        CaseManageResDTO caseManageResDTO = mapperFacade.map(caseManage, CaseManageResDTO.class);
        return Res.success(caseManageResDTO);
    }

    @ApiOperation(value = "提交办理")
    @PatchMapping()
    public Res updateCaseManage(@RequestBody @Validated CaseManageUpdateVO caseManageUpdateVO) {
        CaseManage caseManage = caseManageService.getById(caseManageUpdateVO.getId());
        if (!EnumCaseManageState.Default.getValue().equals(caseManage.getStateId())) {
            return Res.failure("操作失败，此信息已处理，不可重复处理");
        }
        CaseManage caseManageTmp = mapperFacade.map(caseManageUpdateVO, CaseManage.class);
        caseManageTmp.setStateId(EnumCaseManageState.YiBanLi.getValue());
        caseManageTmp.setOperationDate(LocalDateTime.now());
        boolean result = caseManageService.updateById(caseManageTmp);
        return Res.res(result);
    }

    @ApiOperation(value = "提交驳回")
    @PatchMapping("refuse")
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res refuseCaseManage(@RequestBody @Validated CaseManageRefuseVO caseManageRefuseVO) throws Exception {
        CaseManage caseManage = caseManageService.getById(caseManageRefuseVO.getId());
        if (!EnumCaseManageState.Default.getValue().equals(caseManage.getStateId())) {
            return Res.failure("操作失败，此信息已处理，不可重复处理");
        }
        CaseManage caseManageTmp = mapperFacade.map(caseManageRefuseVO, CaseManage.class);
        caseManageTmp.setStateId(EnumCaseManageState.YiBoHui.getValue());
        boolean result1 = caseManageService.updateById(caseManageTmp);
        if (result1) {
            Boolean result2 = letClueService.updateLetClueResultType(caseManage.getLetClueId(), EnumLetClueResultType.BoHui.getValue());
            if (result2) {
                return Res.success();
            }
        }
        throw new Exception("操作失败，驳回处理错误");
    }

    @ApiOperation(value = "查询案管室下拉列表(编辑单个时使用)")
    @PostMapping("deptList")
    public Res<List<CaseManageDeptDTO>> caseManageDeptList() {
        List<SysUnit> unitList = unitService.getListByType("案管室");
        List<CaseManageDeptDTO> caseManageDeptDTOList = mapperFacade.mapAsList(unitList, CaseManageDeptDTO.class);
        return Res.success(caseManageDeptDTOList);
    }

    @ApiOperation(value = "线索-新增案管线索")
    @PutMapping("letCLue")
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res createLetCLueInCaseManage(HttpServletRequest httpServletRequest, @RequestBody @Validated LetClueCreateVO letClueCreateVO) throws Exception {
        LetClueCreateDTO letClueCreateDTO = mapperFacade.map(letClueCreateVO, LetClueCreateDTO.class);
        letClueCreateDTO.setResultTypeId(EnumLetClueResultType.AnGuan.getValue());
        String userId = JavaJWT.getId(httpServletRequest);
        if (userId != null) {
            letClueCreateDTO.setCreatorId(Long.parseLong(userId));
        }
        String letCLueId = letClueService.createOne(letClueCreateDTO);
        boolean caseManageResult = caseManageService.save(new CaseManage().setLetClueId(letCLueId));
        if (caseManageResult) {
            return Res.success();
        }
        throw new Exception("添加失败，保存出现错误");
    }

    @ApiOperation(value = "线索-修改案管线索")
    @PatchMapping("letClue")
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res updateLetClueInCaseManage(@RequestBody LetClueUpdateVO letClueUpdateVO) throws Exception {
        LetClueUpdateDTO letClueUpdateDTO = mapperFacade.map(letClueUpdateVO, LetClueUpdateDTO.class);
        LetClue letClue = letClueService.getById(letClueUpdateVO.getId());
        if (letClue.getResultTypeId() == null || EnumLetClueResultType.getEnumByValue(letClue.getResultTypeId()).getIsEditableInAnGuan()) {
            letClueService.updateLetClue(letClueUpdateDTO);
            return Res.success("修改成功");
        }else{
            return Res.failure("此线索未分发到案管，不可修改");
        }
    }

    @ApiOperation(value = "线索-查询单个by id")
    @PostMapping("letClue")
    public Res selectLetClue(@RequestBody LetClueGetByIdVO letClueGetByIdVO) throws UnsupportedEncodingException {
        LetClueResDTO letClueResDTO = letClueService.selectOne(letClueGetByIdVO.getId());
        if (letClueResDTO == null) {
            return Res.failure("查询的内容不存在，id编号:" + letClueGetByIdVO.getId());
        } else {
            return Res.success(letClueResDTO);
        }
    }

}
