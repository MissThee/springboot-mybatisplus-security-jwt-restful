package com.github.form.db.mapper.primary.letter.clue;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.LetClue;
import com.github.form.models.dto.letter.clue.LetClueListResDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 信访表单 Mapper 接口
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Component
public interface LetClueMapper extends BaseMapper<LetClue> {

    List<LetClueListResDTO> selectSimplePage(
            @Param("lineStart") Integer lineStart,
            @Param("pageSize") Integer pageSize,
            @Param("resultTypeId") String resultTypeId,
            @Param("startReceptionTime") LocalDate startReceptionTime,
            @Param("endReceptionTime") LocalDate endReceptionTime,
            @Param("content") String content,
            @Param("defendantName") String defendantName);

    Long selectTotal(@Param("resultTypeId") String resultTypeId,
                     @Param("startReceptionTime") LocalDate startReceptionTime,
                     @Param("endReceptionTime") LocalDate endReceptionTime,
                     @Param("content") String content,
                     @Param("defendantName") String defendantName);

}
