package com.github.form.service.interf.letter.review;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.db.entity.primary.ReviewClue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.form.models.dto.letter.review.ReviewGetListResDTO;
import com.github.form.models.dto.letter.review.ReviewGetOneResDTO;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <p>
 * 问题线索处置方案呈批笺 服务类
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-07-04
 */
@Service
public interface ReviewClueService extends IService<ReviewClue> {
    boolean createOne(String userIdStr, String letClueId) throws Exception;

    boolean updateOne(String userIdStr, Long id, Map<String, String> form) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MyMethodArgumentNotValidException;

    ReviewGetOneResDTO selectOne(String id);

    ReviewGetListResDTO selectList(String userIdStr, Integer pageIndex, Integer pageSize, Integer isFinished);

}
