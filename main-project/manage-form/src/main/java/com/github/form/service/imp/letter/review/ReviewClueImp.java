package com.github.form.service.imp.letter.review;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.dto.login.AuthDTO;
import com.github.base.service.interf.login.AuthInfoService;
import com.github.base.service.interf.manage.SignatureService;
import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.db.entity.primary.*;
import com.github.common.tool.MapAndBean;
import com.github.form.db.mapper.primary.letter.casemanage.CaseManageMapper;
import com.github.form.db.mapper.primary.letter.clue.LetClueDefendantMapper;
import com.github.form.db.mapper.primary.letter.clue.LetDefendantMapper;
import com.github.form.db.mapper.primary.letter.review.ReviewClueMapper;
import com.github.form.db.mapper.primary.letter.review.ReviewClueUserHistoryMapper;
import com.github.form.models.dto.letter.review.ReviewClueDTO;
import com.github.form.models.dto.letter.review.ReviewClueOneDTO;
import com.github.form.models.dto.letter.review.ReviewGetListResDTO;
import com.github.form.models.dto.letter.review.ReviewGetOneResDTO;
import com.github.form.service.interf.letter.review.ReviewClueService;
import com.github.form.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 问题线索处置方案呈批笺 服务实现类
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-07-04
 */
@Service
public class ReviewClueImp extends ServiceImpl<ReviewClueMapper, ReviewClue> implements ReviewClueService {
    private final ReviewClueUserHistoryMapper reviewClueUserHistoryMapper;
    private final SignatureService signatureService;
    private final AuthInfoService authInfoService;
    private final MapperFacade mapperFacade;
    private final ReviewClueMapper reviewClueMapper;
    private final LetClueDefendantMapper letClueDefendantMapper;
    private final LetDefendantMapper letDefendantMapper;
    private final CaseManageMapper caseManageMapper;

    public ReviewClueImp(MapperFacade mapperFacade, ReviewClueMapper reviewClueMapper, LetClueDefendantMapper letClueDefendantMapper, LetDefendantMapper letDefendantMapper, AuthInfoService authInfoService, SignatureService signatureService, ReviewClueUserHistoryMapper reviewClueUserHistoryMapper, CaseManageMapper caseManageMapper) {
        this.mapperFacade = mapperFacade;
        this.reviewClueMapper = reviewClueMapper;
        this.letClueDefendantMapper = letClueDefendantMapper;
        this.letDefendantMapper = letDefendantMapper;
        this.authInfoService = authInfoService;
        this.signatureService = signatureService;
        this.reviewClueUserHistoryMapper = reviewClueUserHistoryMapper;
        this.caseManageMapper = caseManageMapper;
    }

    private static final JSONArray flowJA = new JSONArray() {{
        add(new JSONObject() {{
            put("permission", "reviewLetClue:bwld");
            put("name", "本委领导批示");
            put("param", new JSONArray() {{
                add("content");
            }});
        }});
        add(new JSONObject() {{
            put("permission", "reviewLetClue:fgfsj");
            put("name", "分管副书记意见");
            put("param", new JSONArray() {{
                add("content");
            }});
        }});
        add(new JSONObject() {{
            put("permission", "reviewLetClue:zgcw");
            put("name", "主管常委意见");
            put("param", new JSONArray() {{
                add("content");
            }});
        }});
        add(new JSONObject() {{
            put("permission", "reviewLetClue:scs");
            put("name", "审查室意见");
            put("param", new JSONArray() {{
                add("content");
            }});
        }});
    }};

    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public boolean createOne(String userIdStr, String letClueId) {
        List<String> letClueDefendantIdList;//查找线索相关的被反映人id集合
        {
            QueryWrapper<LetClueDefendant> qw = new QueryWrapper<>();
            qw.eq(LetClueDefendant.CLUE_ID, letClueId);
            List<LetClueDefendant> letClueDefendantList = letClueDefendantMapper.selectList(qw);
            letClueDefendantIdList = letClueDefendantList.stream().map(LetClueDefendant::getDefendantId).collect(Collectors.toList());
        }
        List<LetDefendant> letDefendantList;//查找线索相关的被反映人对象集合
        {
            QueryWrapper<LetDefendant> qw = new QueryWrapper<>();
            qw.in(LetDefendant.ID, letClueDefendantIdList);
            letDefendantList = letDefendantMapper.selectList(qw);
        }
        List<String> nameList = letDefendantList.stream().map(LetDefendant::getName).collect(Collectors.toList());
        String nameListStr = Joiner.on('，').join(nameList);
        Map<String, String> map = new HashMap<String, String>() {{
            put("title", "关于反映" + nameListStr + "问题线索的处置方案");
        }};
        QueryWrapper<ReviewClue> qw = new QueryWrapper<>();
        qw.eq(ReviewClue.LET_CLUE_ID, letClueId)
                .eq(ReviewClue.IS_FINISHED, false);
        int reviewClueCount = reviewClueMapper.selectCount(qw);
        if (reviewClueCount > 0) {
            return false;
        }
        ReviewClue reviewClue = mapperFacade.map(map, ReviewClue.class).setLetClueId(letClueId);
        makeUpdateFlow(reviewClue);
        return reviewClueMapper.insert(reviewClue) > 0;

    }

    @Override
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public boolean updateOne(String userIdStr, Long id, Map<String, String> form) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MyMethodArgumentNotValidException {
        ReviewClue reviewClue = reviewClueMapper.selectById(id);
        AuthDTO authDTO = authInfoService.selectUserById(userIdStr);
        if (!authDTO.getPermissionValueList().contains(reviewClue.getCurrentStep())) {
            throw new AccessDeniedException("您无权审批此信息，请联系管理员，获取相关权限");
        }

        if (reviewClue.getIsFinished()) {
            return false;
        }
        ReviewClue reviewClueForUpdate = MapAndBean.mapToBean(form, ReviewClue.class)
                .setId(id)
                .setCurrentStep(reviewClue.getCurrentStep())
                .setTotalStep(reviewClue.getTotalStep());

        List<String> canEditKeyList = getCanEditKeyList(reviewClue);

        if (canEditKeyList != null) {
            //注意此处必须使用拷贝赋值，引用类型将造成原map中属性被删除！！
            Set<String> invalidVariableList = new HashSet<>(form.keySet());//【不符合字段集合】存入前端提交的所有属性：key
            //将用户输入的所有字段加入【不符合字段集合】。通过遍历流程图中的字段，从【不符合字段集合】中逐一剔除。若全部剔除，则通过；有剩余则抛出异常，返回消息
            invalidVariableList.removeAll(canEditKeyList);
            if (invalidVariableList.size() > 0) {
                throw new MyMethodArgumentNotValidException("表单无以下字段，不可提交这些值：" + Joiner.on(",").join(invalidVariableList));
            }
        }

        for (String key : form.keySet()) {
            String prefix = StringUtil.getWordBeforeFirstUpperCase(key);
            //每一步审批均为xxxId、xxxSign、xxxName、xxxDate四项，替换xxx，插入值
            if (userIdStr != null) {
                {//设置本账号id 到表单
                    Method method = reviewClueForUpdate.getClass().getMethod("set" + prefix + "Id", Integer.class);
                    method.invoke(reviewClueForUpdate, authDTO.getId().intValue());
                }
                {//设置本账号签名 到表单
                    QueryWrapper<SysSignature> qw = new QueryWrapper<>();
                    qw.eq(SysSignature.USER_ID, authDTO.getId())
                            .orderByDesc(SysSignature.IS_DEFAULT)
                            .orderByDesc(SysSignature.CREATE_DATE);
                    List<SysSignature> sysSignatureList = signatureService.list(qw);
                    String sysSignaturePath = null;
                    if (sysSignatureList.size() > 0) {//取
                        sysSignaturePath = sysSignatureList.get(0).getSignFilePath();
                    }
                    Method method = reviewClueForUpdate.getClass().getMethod("set" + prefix + "Sign", String.class);
                    method.invoke(reviewClueForUpdate, sysSignaturePath);
                }
                {//设置本账号姓名 到表单
                    Method method = reviewClueForUpdate.getClass().getMethod("set" + prefix + "Name", String.class);
                    method.invoke(reviewClueForUpdate, authDTO.getNickname());
                }
                {//设置审批时间 到表单
                    Method method = reviewClueForUpdate.getClass().getMethod("set" + prefix + "Date", LocalDateTime.class);
                    method.invoke(reviewClueForUpdate, LocalDateTime.now());
                }
            }
        }

        boolean flowIsFinish = makeUpdateFlow(reviewClueForUpdate);
        if (reviewClueMapper.updateById(reviewClueForUpdate) > 0) {
            reviewClueUserHistoryMapper.insert(new ReviewClueUserHistory().setReviewId(reviewClue.getId()).setUserId(authDTO.getId()));
            if (flowIsFinish) {
                caseManageMapper.insert(new CaseManage().setLetClueId(reviewClue.getLetClueId()));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ReviewGetOneResDTO selectOne(String id) {
        ReviewClue reviewClue = reviewClueMapper.selectById(id);
        if(reviewClue==null){
            return null;
        }
        ReviewGetOneResDTO reviewGetOneResDTO = new ReviewGetOneResDTO()
                .setId(reviewClue.getId())
                .setForm(mapperFacade.map(reviewClue, ReviewClueOneDTO.class));//添加返回值
        List<String> canEditKeyList = getCanEditKeyList(reviewClue);
        reviewGetOneResDTO.setCanEditKeyList(canEditKeyList);//添加返回值
        return reviewGetOneResDTO;
    }

    @Override
    public ReviewGetListResDTO selectList(String userIdStr, Integer pageIndex, Integer pageSize, Integer isFinished) {
        AuthDTO authDTO = authInfoService.selectUserById(userIdStr);
        //查询总数
        long total = 0;
        List<ReviewClueDTO> reviewClueDTOList = new ArrayList<>();
        List<String> permissionValueList = new ArrayList<>(authDTO.getPermissionValueList());
        if (permissionValueList.size() == 0) {
            return new ReviewGetListResDTO()
                    .setFormList(reviewClueDTOList)
                    .setTotal(total);
        }
        List<Long> idList;
        {
            QueryWrapper<ReviewClue> qw = new QueryWrapper<>();
            qw.select(ReviewClue.ID);
            switch (isFinished) {
                case 0://未结束，即需要我审批的
                    permissionValueList.add("[empty]");
                    qw.in(ReviewClue.CURRENT_STEP, permissionValueList);
                    break;
                case 1://已处理，即我参与过的
                {
                    QueryWrapper<ReviewClueUserHistory> reviewClueUserHistoryQW = new QueryWrapper<>();
                    reviewClueUserHistoryQW.eq(ReviewClueUserHistory.USER_ID, authDTO.getId());
                    List<ReviewClueUserHistory> reviewClueUserHistoryList = reviewClueUserHistoryMapper.selectList(reviewClueUserHistoryQW);
                    List<Long> userReviewIdList = reviewClueUserHistoryList.stream().map(ReviewClueUserHistory::getReviewId).collect(Collectors.toList());
                    userReviewIdList.add(-1L);
                    qw.in(ReviewClue.ID, userReviewIdList);
                }
                break;
                case 2://全部，以上两个总和
                    qw.in(ReviewClue.CURRENT_STEP, permissionValueList).or();
                {
                    QueryWrapper<ReviewClueUserHistory> reviewClueUserHistoryQW = new QueryWrapper<>();
                    reviewClueUserHistoryQW.eq(ReviewClueUserHistory.USER_ID, authDTO.getId());
                    List<ReviewClueUserHistory> reviewClueUserHistoryList = reviewClueUserHistoryMapper.selectList(reviewClueUserHistoryQW);
                    List<Long> userReviewIdList = reviewClueUserHistoryList.stream().map(ReviewClueUserHistory::getReviewId).collect(Collectors.toList());
                    userReviewIdList.add(-1L);
                    qw.in(ReviewClue.ID, userReviewIdList);
                }
                break;
            }
            qw.orderByDesc(ReviewClue.LET_CLUE_ID);
            PageHelper.startPage(pageIndex, pageSize);
            List<ReviewClue> reviewClueList = reviewClueMapper.selectList(qw);
            PageInfo<ReviewClue> reviewCluePageInfo = new PageInfo<>(reviewClueList);
            idList = reviewCluePageInfo.getList().stream().map(ReviewClue::getId).collect(Collectors.toList());
            total = reviewCluePageInfo.getTotal();
        }
        {
            if (idList.size() > 0) {
                QueryWrapper<ReviewClue> qw = new QueryWrapper<>();
                qw.in(ReviewClue.ID, idList)
                        .orderByDesc(ReviewClue.LET_CLUE_ID);
                List<ReviewClue> reviewClueList = reviewClueMapper.selectList(qw);
                for (ReviewClue reviewClue : reviewClueList) {
                    ReviewClueDTO reviewClueDTO = mapperFacade.map(reviewClue, ReviewClueDTO.class);
                    if (reviewClue.getIsFinished()) {
                        reviewClueDTO.setStepName("已结束");
                    } else {
                        JSONArray jsonArray = JSONObject.parseArray(reviewClue.getTotalStep());
                        for (Object jsonObject : jsonArray) {
                            if (((JSONObject) jsonObject).getString("permission").equals(reviewClue.getCurrentStep())) {
                                reviewClueDTO.setStepName(((JSONObject) jsonObject).getString("name"));
                            }
                        }
                    }
                    reviewClueDTOList.add(reviewClueDTO);
                }
            }
        }
        ReviewGetListResDTO reviewGetListResDTO = new ReviewGetListResDTO()
                .setFormList(reviewClueDTOList)
                .setTotal(total);
        return reviewGetListResDTO;
    }

    /**
     * 构建更新步骤用的对象
     *
     * @param reviewClue
     * @return 返回此流程更新后，是否到了技术步骤
     */
    private boolean makeUpdateFlow(ReviewClue reviewClue) {
        if (reviewClue.getIsFinished() == null || !reviewClue.getIsFinished()) {
            if (StringUtils.isEmpty(reviewClue.getTotalStep())) {
                reviewClue.setTotalStep(flowJA.toJSONString());
            }
            if (StringUtils.isEmpty(reviewClue.getCurrentStep())) {
                reviewClue.setCurrentStep(flowJA.getJSONObject(0).getString("permission"));
            } else {
                for (int i = 0; i < flowJA.size(); i++) {
                    if (reviewClue.getCurrentStep().equals(flowJA.getJSONObject(i).getString("permission"))) {
                        if (i < flowJA.size() - 1) {
                            reviewClue.setCurrentStep(flowJA.getJSONObject(i + 1).getString("permission"));
                        } else {
                            //审批结束
                            reviewClue.setCurrentStep("");//清空审批步骤
                            reviewClue.setIsFinished(true);//修改审批结束标志
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    private List<String> getCanEditKeyList(ReviewClue reviewClue) {
        List<String> canEditKeyList = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(reviewClue.getTotalStep());
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            String permission = jsonObject.getString("permission");
            if (permission.equals(reviewClue.getCurrentStep())) {
                String[] permissionValues = permission.split(":");
                if (permissionValues.length == 2) {
                    if (jsonObject.containsKey("param")) {
                        JSONArray paramJA = jsonObject.getJSONArray("param");
                        if (paramJA.size() > 0) {
                            for (Object paramObject : paramJA) {
                                String param = paramObject.toString();
                                canEditKeyList.add(permissionValues[1] + param.substring(0, 1).toUpperCase() + param.substring(1));
                            }
                        }
                    }
                } else {
                    canEditKeyList = null;
                }
                break;
            }
        }
        return canEditKeyList;
    }
}
