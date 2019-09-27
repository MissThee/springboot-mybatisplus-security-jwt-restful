package com.github.form.controller.letter.review;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import com.github.form.models.dto.letter.review.ReviewGetListResDTO;
import com.github.form.models.dto.letter.review.ReviewGetOneResDTO;
import com.github.form.models.vo.letter.review.ReviewCreateVO;
import com.github.form.models.vo.letter.review.ReviewGetListVO;
import com.github.form.models.vo.letter.review.ReviewGetOneVO;
import com.github.form.models.vo.letter.review.ReviewUpdateVO;
import com.github.form.service.interf.letter.review.ReviewClueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("review/letclue")
@PreAuthorize("isAuthenticated() and (hasPermission(null,'[ADMIN]') or hasPermission(null,'reviewLetClue'))")
public class LetClueReviewController {
    private final ReviewClueService reviewClueService;

    public LetClueReviewController(ReviewClueService reviewClueService) {
        this.reviewClueService = reviewClueService;
    }

    @PutMapping
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res createOne(HttpServletRequest httpServletRequest, @RequestBody @Validated ReviewCreateVO reviewCreateVO) throws Exception {
        String userIdStr = JavaJWT.getId(httpServletRequest);
        String letClueId = reviewCreateVO.getLetClueId();
        return Res.res(reviewClueService.createOne(userIdStr, letClueId));
    }

    @PatchMapping
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public Res updateOne(HttpServletRequest httpServletRequest, @RequestBody ReviewUpdateVO reviewUpdateVO) throws MyMethodArgumentNotValidException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String userIdStr = JavaJWT.getId(httpServletRequest);
        boolean result = reviewClueService.updateOne(userIdStr, reviewUpdateVO.getId(), reviewUpdateVO.getForm());
        if (result) {
            return Res.success();
        } else {
            return Res.failure("审批状态已变更，请刷新查看最新信息");
        }
    }

    @PostMapping("list")
    public Res selectList(HttpServletRequest httpServletRequest, @RequestBody @Validated ReviewGetListVO reviewGetListVO) {
        String userIdStr = JavaJWT.getId(httpServletRequest);
        ReviewGetListResDTO reviewGetListResDTO = reviewClueService.selectList(userIdStr, reviewGetListVO.getPageIndex(), reviewGetListVO.getPageSize(), reviewGetListVO.getIsFinished());
        return Res.success(reviewGetListResDTO);
    }

    @PostMapping
    public Res selectOne(HttpServletRequest httpServletRequest, @RequestBody @Validated ReviewGetOneVO reviewGetOneVO) {
        ReviewGetOneResDTO reviewGetOneResDTO = reviewClueService.selectOne(reviewGetOneVO.getId());
        if(reviewGetOneResDTO==null){
            return Res.failure("未查询到对应数据");
        }
        return Res.success(reviewGetOneResDTO);
    }
}
