package com.github.form.service.interf.letter.clue;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.db.entity.primary.LetClue;
import com.github.common.tool.SimplePageInfo;
import com.github.form.models.dto.letter.clue.LetClueCreateDTO;
import com.github.form.models.dto.letter.clue.LetClueListResDTO;
import com.github.form.models.dto.letter.clue.LetClueResDTO;
import com.github.form.models.dto.letter.clue.LetClueUpdateDTO;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

/**
 * <p>
 * 信访表单 服务类
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
public interface LetClueService extends IService<LetClue> {
    LetClueResDTO selectOne(String id) throws UnsupportedEncodingException;
    SimplePageInfo<LetClueListResDTO> selectSimplePage(Integer pageIndex, Integer pageSize, String resultTypeId, LocalDate startReceptionTime, LocalDate endReceptionTime, String content, String defendantName);
    Boolean deleteOne(String letClueId);
    Boolean updateLetClueResultType(String id, Integer resultTypeId);
    String createOne(LetClueCreateDTO letClueCreateDTO) throws Exception;
    String updateLetClue(LetClueUpdateDTO letClueUpdateDTO) throws Exception;

}
