package com.github.form.db.mapper.primary.letter.casemanage;

import com.github.common.db.entity.primary.CaseManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.form.models.dto.letter.casemanage.CaseManageListResDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-09-19
 */
@Component
public interface CaseManageMapper extends BaseMapper<CaseManage> {

    List<CaseManageListResDTO> selectSimplePage(
            @Param("lineStart") Integer lineStart,
            @Param("pageSize") Integer pageSize,
            @Param("letClueId") String letClueId,
            @Param("defendantName") String defendantName,
            @Param("content") String  content,
            @Param("startReceptionTime") LocalDate startReceptionTime,
            @Param("endReceptionTime") LocalDate endReceptionTime,
            @Param("leftDayNum") Integer leftDayNum
    );

    Long selectTotal(
            @Param("letClueId") String letClueId,
            @Param("defendantName") String defendantName,
            @Param("content") String  content,
            @Param("startReceptionTime") LocalDate startReceptionTime,
            @Param("endReceptionTime") LocalDate endReceptionTime,
            @Param("leftDayNum") Integer leftDayNum
    );

}
