package com.github.form.service.imp.casemanage;

import com.github.common.db.entity.primary.CaseManage;
import com.github.common.tool.SimplePageInfo;
import com.github.form.db.mapper.primary.letter.casemanage.CaseManageMapper;
import com.github.form.models.dto.letter.casemanage.CaseManageListResDTO;
import com.github.form.service.interf.casemanage.CaseManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-09-19
 */
@Service
public class CaseManageImp extends ServiceImpl<CaseManageMapper, CaseManage> implements CaseManageService {
    private final CaseManageMapper caseManageMapper;

    public CaseManageImp(CaseManageMapper caseManageMapper) {
        this.caseManageMapper = caseManageMapper;
    }

    @Override
    public SimplePageInfo<CaseManageListResDTO> selectSimplePage(Integer pageIndex, Integer pageSize, String letClueId,String defendantName,String content, LocalDate startReceptionTime, LocalDate endReceptionTime, Integer leftDayNum) {
        //查询
        Integer lineStart = (pageIndex - 1) * pageSize;
        List<CaseManageListResDTO> caseManageListResDTOList = caseManageMapper.selectSimplePage(lineStart, pageSize, letClueId, defendantName, content, startReceptionTime, endReceptionTime, leftDayNum);
        Long total = caseManageMapper.selectTotal(letClueId, defendantName, content, startReceptionTime, endReceptionTime, leftDayNum);
        return new SimplePageInfo<>(caseManageListResDTOList, total);
    }
}
