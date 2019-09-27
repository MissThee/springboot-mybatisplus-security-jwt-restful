package com.github.form.service.interf.casemanage;

import com.github.common.db.entity.primary.CaseManage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.tool.SimplePageInfo;
import com.github.form.models.dto.letter.casemanage.CaseManageListResDTO;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-09-19
 */
public interface CaseManageService extends IService<CaseManage> {

    SimplePageInfo<CaseManageListResDTO> selectSimplePage(Integer pageIndex, Integer pageSize, String letClueId,String defendantName,String content, LocalDate startReceptionTime, LocalDate endReceptionTime, Integer leftDayNum);

}
