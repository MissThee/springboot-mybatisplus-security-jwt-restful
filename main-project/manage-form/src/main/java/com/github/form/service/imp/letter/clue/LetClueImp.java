package com.github.form.service.imp.letter.clue;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.common.db.entity.primary.*;
import com.github.common.tool.SimplePageInfo;
import com.github.form.common.Constants;
import com.github.form.common.EnumStuffRelationType;
import com.github.form.db.mapper.primary.letter.*;
import com.github.form.db.mapper.primary.letter.casemanage.LetClueCreatorMapper;
import com.github.form.db.mapper.primary.letter.clue.*;
import com.github.form.models.dto.LetFileResDTO;
import com.github.form.models.dto.letter.clue.*;
import com.github.form.service.interf.letter.clue.LetClueService;
import com.github.form.utils.LetClueIdBuilder;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 信访表单 服务实现类
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Service
@Slf4j
public class LetClueImp extends ServiceImpl<LetClueMapper, LetClue> implements LetClueService {
    private final MapperFacade mapperFacade;
    private final LetClueMapper letClueMapper;
    private final LetClueSourceMapper letClueSourceMapper;
    private final LetDefendantMapper letDefendantMapper;
    private final LetClueDefendantMapper letClueDefendantMapper;
    private final LetDefendantJobTypeMapper letDefendantJobTypeMapper;
    private final StuffMapper stuffMapper;
    private final LetClueIllegalBehaviorMapper letClueIllegalBehaviorMapper;
    private final LetClueAreaInvolvedMapper letClueAreaInvolvedMapper;
    private final LetClueIdBuilder letClueIdBuilder;
    private final LetClueCreatorMapper letClueCreatorMapper;

    public LetClueImp(MapperFacade mapperFacade, LetClueMapper letClueMapper, LetClueSourceMapper letClueSourceMapper, LetDefendantMapper letDefendantMapper, LetClueDefendantMapper letClueDefendantMapper, LetDefendantJobTypeMapper letDefendantJobTypeMapper, StuffMapper stuffMapper, LetClueIllegalBehaviorMapper letClueIllegalBehaviorMapper, LetClueAreaInvolvedMapper letClueAreaInvolvedMapper, LetClueIdBuilder letClueIdBuilder, LetClueCreatorMapper letClueCreatorMapper) {
        this.mapperFacade = mapperFacade;
        this.letClueMapper = letClueMapper;
        this.letClueSourceMapper = letClueSourceMapper;
        this.letDefendantMapper = letDefendantMapper;
        this.letClueDefendantMapper = letClueDefendantMapper;
        this.letDefendantJobTypeMapper = letDefendantJobTypeMapper;
        this.stuffMapper = stuffMapper;
        this.letClueIllegalBehaviorMapper = letClueIllegalBehaviorMapper;
        this.letClueAreaInvolvedMapper = letClueAreaInvolvedMapper;
        this.letClueIdBuilder = letClueIdBuilder;
        this.letClueCreatorMapper = letClueCreatorMapper;
    }

    @Override
    public LetClueResDTO selectOne(String letClueId) throws UnsupportedEncodingException {
        LetClue letClue = letClueMapper.selectById(letClueId);
        if (letClue == null) {
            return null;
        }
        LetClueResDTO letClueResDTO = mapperFacade.map(letClue, LetClueResDTO.class);
        //查询线索来源
        List<Integer> sourceIdList;
        {
            QueryWrapper<LetClueSource> qw = new QueryWrapper<>();
            qw.eq(LetClueSource.CLUE_ID, letClueId);
            List<LetClueSource> letClueSourceList = letClueSourceMapper.selectList(qw);
            sourceIdList = letClueSourceList.stream().map(LetClueSource::getSourceId).collect(Collectors.toList());
        }
        letClueResDTO.setDicSourceIdList(sourceIdList.size() > 0 ? sourceIdList : new ArrayList<>());
        //构建被反映人
        //查询被反映人id集合
        List<String> defendantIdList;
        {
            QueryWrapper<LetClueDefendant> qw = new QueryWrapper<>();
            qw.eq(LetClueDefendant.CLUE_ID, letClueId);
            List<LetClueDefendant> letClueDefendantList = letClueDefendantMapper.selectList(qw);
            defendantIdList = letClueDefendantList.stream().map(LetClueDefendant::getDefendantId).collect(Collectors.toList());
        }
        if (defendantIdList.size() > 0) {
            //查询 被反映人 对象集合
            List<LetDefendant> letDefendantList;
            {
                QueryWrapper<LetDefendant> qw = new QueryWrapper<>();
                qw.in(LetDefendant.ID, defendantIdList);
                letDefendantList = letDefendantMapper.selectList(qw);
            }
            //查询涉及到的 被反映人干部类型 对象集合
            List<LetDefendantJobType> letDefendantJobTypeList;
            {
                QueryWrapper<LetDefendantJobType> qw = new QueryWrapper<>();
                qw.in(LetDefendantJobType.DEFENDANT_ID, defendantIdList);
                letDefendantJobTypeList = letDefendantJobTypeMapper.selectList(qw);
            }
            //构造被反映人DTO
            List<LetDefendantResDTO> letDefendantResDTOS = new ArrayList<>();
            for (LetDefendant letDefendant : letDefendantList) {
                LetDefendantResDTO letDefendantResDTO = mapperFacade.map(letDefendant, LetDefendantResDTO.class);
                List<Integer> jobTypeIdList = letDefendantJobTypeList.stream()
                        .filter(item -> item.getDefendantId().equals(letDefendantResDTO.getId()))
                        .map(LetDefendantJobType::getJobTypeId)
                        .collect(Collectors.toList());
                if (jobTypeIdList.size() > 0) {
                    letDefendantResDTO.setJobTypeIdList(jobTypeIdList);
                }
                letDefendantResDTOS.add(letDefendantResDTO);
            }
            letClueResDTO.setLetDefendantList(letDefendantResDTOS);
        } else {
            letClueResDTO.setLetDefendantList(new ArrayList<>());
        }
        //查询扫描件
        List<Stuff> stuffList;
        {
            QueryWrapper<Stuff> qw = new QueryWrapper<>();
            qw.eq(Stuff.RELATION_ID, letClueId)
                    .eq(Stuff.RELATION_TYPE, EnumStuffRelationType.XianSuo.getValue());
            stuffList = stuffMapper.selectList(qw);
        }
        if (stuffList.size() > 0) {
            List<LetFileResDTO> letFileResDTOList = new ArrayList<>();
            for (Stuff stuff : stuffList) {
                LetFileResDTO letFileResDTO = new LetFileResDTO();
                String fileUrl = "";
                if (Constants.STATIC_RESOURCE_URL.endsWith("/") && stuff.getFile().startsWith("/")) {
                    fileUrl = Constants.STATIC_RESOURCE_URL + stuff.getFile().substring(1);
                } else if (!Constants.STATIC_RESOURCE_URL.endsWith("/") && !stuff.getFile().startsWith("/")) {
                    fileUrl = Constants.STATIC_RESOURCE_URL + "/" + stuff.getFile();
                } else {
                    fileUrl = Constants.STATIC_RESOURCE_URL + stuff.getFile();
                }
                if (fileUrl.length() > 0) {
                    fileUrl = "http://" +fileUrl;
                }
                letFileResDTO.setFile(stuff.getFile());
                letFileResDTO.setName(stuff.getName());
                letFileResDTO.setUrl(fileUrl);
                letFileResDTOList.add(letFileResDTO);
            }
            letClueResDTO.setFileList(letFileResDTOList);
        } else {
            letClueResDTO.setFileList(new ArrayList<>());
        }
        //查询主要违法行为id集合
        List<Integer> letClueIllegalBehaviorIdList;
        {
            QueryWrapper<LetClueIllegalBehavior> qw = new QueryWrapper<>();
            qw.eq(LetClueIllegalBehavior.CLUE_ID, letClueId);
            List<LetClueIllegalBehavior> letClueIllegalBehaviorList = letClueIllegalBehaviorMapper.selectList(qw);
            letClueIllegalBehaviorIdList = letClueIllegalBehaviorList.stream().map(LetClueIllegalBehavior::getIllegalBehaviorId).collect(Collectors.toList());
        }
        letClueResDTO.setDicIllegalBehaviorIdList(letClueIllegalBehaviorIdList.size() > 0 ? letClueIllegalBehaviorIdList : new ArrayList<>());
        //查询涉及领域
        {
            QueryWrapper<LetClueAreaInvolved> qw = new QueryWrapper<>();
            qw.eq(LetClueAreaInvolved.CLUE_ID, letClueId);
            List<LetClueAreaInvolved> letClueAreaInvolvedList = letClueAreaInvolvedMapper.selectList(qw);
            if (letClueAreaInvolvedList.size() > 0) {
                letClueResDTO.setDicAreaInvolvedIdList(letClueAreaInvolvedList.stream().map(LetClueAreaInvolved::getIllegalBehaviorId).collect(Collectors.toList()));
            } else {
                letClueResDTO.setDicAreaInvolvedIdList(new ArrayList<>());
            }
        }
        return letClueResDTO;
    }

    @Override
    public SimplePageInfo<LetClueListResDTO> selectSimplePage(Integer pageIndex, Integer pageSize, String resultTypeId, LocalDate startReceptionTime, LocalDate endReceptionTime, String content, String defendantName) {
        //查询
        Integer lineStart = (pageIndex - 1) * pageSize;
        List<LetClueListResDTO> letClueListResDTOList = letClueMapper.selectSimplePage(lineStart, pageSize, resultTypeId, startReceptionTime, endReceptionTime, content, defendantName);
        Long total = letClueMapper.selectTotal(resultTypeId, startReceptionTime, endReceptionTime, content, defendantName);
        return new SimplePageInfo<>(letClueListResDTOList, total);
    }

    @Override
    public Boolean deleteOne(String id) {
        if (id == null) {
            return false;
        }
        LetClue letClue = new LetClue();
        letClue.setId(id);
        letClue.setIsDelete(true);
        return letClueMapper.updateById(letClue) > 0;
    }

    @Override
    public Boolean updateLetClueResultType(String id, Integer resultTypeId) {
        LetClue letClue = new LetClue();
        letClue.setId(id);
        letClue.setResultTypeId(resultTypeId);
        return letClueMapper.updateById(letClue) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public String createOne(LetClueCreateDTO letClueCreateDTO) throws Exception {
        return CreateOrUpdateLetClue(letClueCreateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public String updateLetClue(LetClueUpdateDTO letClueUpdateDTO) throws Exception {
        return CreateOrUpdateLetClue(letClueUpdateDTO);
    }

    //以下为对添加/更新 信访方法
    private String CreateOrUpdateLetClue(LetClueCreateDTO letClueUpdateDTO) throws Exception {
        String letClueId;
        boolean isCreate;
        if (StringUtils.isEmpty(letClueUpdateDTO.getId())) {
            isCreate = true;
            letClueId = letClueIdBuilder.getId();//生成线索的id号
        } else {
            isCreate = false;
            letClueId = letClueUpdateDTO.getId();//生成线索的id号
        }
// TODO 增加多线程执行 1、2两部分代码
// TODO 1、2任意一个抛出异常则主线程需抛出异常出发@Transactional，进行回滚
// TODO 1、2都执行完后，住线程返回true
// TODO 本方法测试eolinker、信访表单(重制)、添加线索，登录后直接请求本地服务即可成功  https://www.eolinker.com/#/home/ams/project/inside/api/list?groupID=588511&projectName=%E7%BA%AA%E6%A3%80%E5%A7%94&projectHashKey=HuT6pKfcdf8e7de30f65d70fe3d494a7e905188e9f054aa&spaceKey=Y4TRAqNf9ff442f760783c9598d8ae43c24bfc32ad59a58

// TODO 1.----------------------------------------------------------------------------
        //添加/更新 信访表
        {
            LetClue letClue = new LetClue();
            letClue.setId(letClueId);
            letClue.setReceptionTime(letClueUpdateDTO.getReceptionTime());
            letClue.setContent(letClueUpdateDTO.getContent());
            letClue.setResultTypeId(letClueUpdateDTO.getResultTypeId());
            if (isCreate) {
                letClueMapper.insert(letClue);
            } else {
                letClueMapper.updateById(letClue);
            }
        }
        //添加/更新 信访-线索来源 关系表
        if (letClueUpdateDTO.getDicSourceIdList() != null) {
            List<Integer> dicSources = letClueUpdateDTO.getDicSourceIdList();
            List<LetClueSource> letClueSourceList = new ArrayList<>();
            for (Integer dicSource : dicSources) {
                LetClueSource letClueSource = new LetClueSource();
                letClueSource.setClueId(letClueId);
                letClueSource.setSourceId(dicSource);
                letClueSourceList.add(letClueSource);
            }
            if (!isCreate) {//删除旧信息
                QueryWrapper<LetClueSource> qw = new QueryWrapper<>();
                qw.eq(LetClueSource.CLUE_ID, letClueId);
                letClueSourceMapper.delete(qw);
            }
            if (letClueSourceList.size() > 0) {
                boolean insertResult = letClueSourceMapper.insertBatch(letClueSourceList) > 0;
                if (!insertResult) {
                    throw new Exception("letClueSourceList failed");
                }
            }
//            letClueSourceMapper.insertLetClueSources(letClueSourceList);
        }
        //添加/更新 信访-主要违法行为关 联表
        if (letClueUpdateDTO.getDicIllegalBehaviorIdList() != null) {
            List<Integer> dicIllegalBehaviorIdList = letClueUpdateDTO.getDicIllegalBehaviorIdList();
            List<LetClueIllegalBehavior> letClueIllegalBehaviorList = new ArrayList<>();
            for (Integer dicIllegalBehaviorId : dicIllegalBehaviorIdList) {
                LetClueIllegalBehavior letClueIllegalBehavior = new LetClueIllegalBehavior();
                letClueIllegalBehavior.setClueId(letClueId);
                letClueIllegalBehavior.setIllegalBehaviorId(dicIllegalBehaviorId);
                letClueIllegalBehaviorList.add(letClueIllegalBehavior);
            }
            if (!isCreate) {//删除旧信息
                QueryWrapper<LetClueIllegalBehavior> qw = new QueryWrapper<>();
                qw.eq(LetClueIllegalBehavior.CLUE_ID, letClueId);
                letClueIllegalBehaviorMapper.delete(qw);
            }
            if (letClueIllegalBehaviorList.size() > 0) {
                boolean insertResult = letClueIllegalBehaviorMapper.insertBatch(letClueIllegalBehaviorList) > 0;
                if (!insertResult) {
                    throw new Exception("letClueIllegalBehaviorList insert failed");
                }
            }
        }
        //添加/更新 信访-涉及领域 关联表
        if (letClueUpdateDTO.getDicAreaInvolvedIdList() != null) {
            List<Integer> dicAreaInvolvedIdList = letClueUpdateDTO.getDicAreaInvolvedIdList();
            List<LetClueAreaInvolved> letClueAreaInvolvedList = new ArrayList<>();
            for (Integer dicAreaInvolvedId : dicAreaInvolvedIdList) {
                LetClueAreaInvolved letClueAreaInvolved = new LetClueAreaInvolved();
                letClueAreaInvolved.setClueId(letClueId);
                letClueAreaInvolved.setIllegalBehaviorId(dicAreaInvolvedId);
                letClueAreaInvolvedList.add(letClueAreaInvolved);
            }
            if (!isCreate) {//删除旧信息
                QueryWrapper<LetClueAreaInvolved> qw = new QueryWrapper<>();
                qw.eq(LetClueAreaInvolved.CLUE_ID, letClueId);
                letClueAreaInvolvedMapper.delete(qw);
            }
            if (letClueAreaInvolvedList.size() > 0) {
                boolean insertResult = letClueAreaInvolvedMapper.insertBatch(letClueAreaInvolvedList) > 0;
                if (!insertResult) {
                    throw new Exception("letClueAreaInvolvedList insert failed");
                }
            }
        }
        //添加/更新 文件表
        if (letClueUpdateDTO.getFileList() != null) {
            List<Stuff> stuffList = new ArrayList<>();
            List<LetFileResDTO> fileList = letClueUpdateDTO.getFileList();
            for (LetFileResDTO letFileDTO : fileList) {
                Stuff stuff = new Stuff();
                stuff.setRelationType(EnumStuffRelationType.XianSuo.getValue());
                stuff.setRelationId(letClueId);
                stuff.setName(letFileDTO.getName());
                stuff.setFile(letFileDTO.getFile());
                stuff.setCreateDate(LocalDateTime.now());
                stuffList.add(stuff);
            }
            if (!isCreate) {//删除旧信息
                QueryWrapper<Stuff> qw = new QueryWrapper<>();
                qw.eq(Stuff.RELATION_ID, letClueId)
                        .eq(Stuff.RELATION_TYPE, EnumStuffRelationType.XianSuo.getValue());
                stuffMapper.delete(qw);
            }
            if (stuffList.size() > 0) {
                boolean insertResult = stuffMapper.insertBatch(stuffList) > 0;
                if (!insertResult) {
                    throw new Exception("stuffList insert failed");
                }
            }
        }
// TODO 2.----------------------------------------------------------------------------
        //添加/更新 被反映人信息
        if (letClueUpdateDTO.getLetDefendantList() != null) {
            List<LetClueUpdateDTO.LetDefendantDTO> letDefendantDTOList = letClueUpdateDTO.getLetDefendantList();

            List<LetDefendant> letDefendantList = new ArrayList<>();
            List<LetClueDefendant> letClueDefendantList = new ArrayList<>();
            List<LetDefendantJobType> letDefendantJobTypeList = new ArrayList<>();
            int defendantCount = 0; //被检举人id计数，使用时由1开始编号

            for (LetClueUpdateDTO.LetDefendantDTO letDefendantDTO : letDefendantDTOList) {
                String defendantId = letClueId + String.format("%02d", ++defendantCount);
                //收集预增加 被反映人
                {
                    LetDefendant letDefendant = new LetDefendant();
                    letDefendant.setId(defendantId);
                    letDefendant.setName(letDefendantDTO.getName());
                    letDefendant.setCompanyName(letDefendantDTO.getCompanyName());
                    letDefendant.setPostName(letDefendantDTO.getPostName());
                    letDefendant.setJobRankId(letDefendantDTO.getJobRankId());
                    letDefendantList.add(letDefendant);
//                    letDefendantMapper.insert(letDefendant);
                }
                //收集预增加 信访-被检举人关系对象
                {
                    LetClueDefendant letClueDefendant = new LetClueDefendant();
                    letClueDefendant.setClueId(letClueId);
                    letClueDefendant.setDefendantId(defendantId);
                    letClueDefendantList.add(letClueDefendant);
                }
                //收集预增加 被反映人-干部类型关系对象
                {
                    if (letDefendantDTO.getJobTypeIdList() != null && letDefendantDTO.getJobTypeIdList().size() > 0) {
                        List<Integer> jobTypeIdList = letDefendantDTO.getJobTypeIdList();
                        for (Integer jobTypeId : jobTypeIdList) {
                            LetDefendantJobType letDefendantJobType = new LetDefendantJobType();
                            letDefendantJobType.setJobTypeId(jobTypeId);
                            letDefendantJobType.setDefendantId(defendantId);
                            letDefendantJobTypeList.add(letDefendantJobType);
                        }
                    }
                }
            }

            //将以上被反映人信息批量插入
            List<String> letClueDefendantOldIdList = new ArrayList<>();//预删除的被反映人id集合
            if (!isCreate) {
                QueryWrapper<LetClueDefendant> qw = new QueryWrapper<>();
                qw.eq(LetClueDefendant.CLUE_ID, letClueId);
                List<LetClueDefendant> letClueDefendantOldList = letClueDefendantMapper.selectList(qw);
                letClueDefendantOldIdList = letClueDefendantOldList.stream().map(LetClueDefendant::getDefendantId).collect(Collectors.toList());
            }
            {
                if (!isCreate && letClueDefendantOldIdList.size() > 0) {//删除旧信息
                    QueryWrapper<LetDefendant> qw = new QueryWrapper<>();
                    qw.in(LetDefendant.ID, letClueDefendantOldIdList);
                    letDefendantMapper.delete(qw);
                }
                //被反映人
                if (letDefendantList.size() > 0) {
                    boolean insertResult = letDefendantMapper.insertBatch(letDefendantList) > 0;
                    if (!insertResult) {
                        throw new Exception("letDefendantList insert failed");
                    }
                }
            }
            {
                if (!isCreate) {//删除旧信息
                    QueryWrapper<LetClueDefendant> qw = new QueryWrapper<>();
                    qw.eq(LetClueDefendant.CLUE_ID, letClueId);
                    letClueDefendantMapper.delete(qw);
                }
                //信访-被反映人关系
                if (letClueDefendantList.size() > 0) {
                    boolean insertResult = letClueDefendantMapper.insertBatch(letClueDefendantList) > 0;
                    if (!insertResult) {
                        throw new Exception("letClueDefendantList insert failed");
                    }
                }
            }
            {
                if (!isCreate && letClueDefendantOldIdList.size() > 0) {//删除旧信息
                    QueryWrapper<LetDefendantJobType> qw = new QueryWrapper<>();
                    qw.in(LetDefendantJobType.DEFENDANT_ID, letClueDefendantOldIdList);
                    letDefendantJobTypeMapper.delete(qw);
                }
                //被反映人-干部类型关系
                if (letDefendantJobTypeList.size() > 0) {
                    boolean insertResult = letDefendantJobTypeMapper.insertBatch(letDefendantJobTypeList) > 0;
                    if (!insertResult) {
                        throw new Exception("letDefendantJobTypeList insert failed");
                    }
                }
            }
        }
        //TODO 截止
        try {
            letClueCreatorMapper.insert(new LetClueCreator().setLetClueId(letClueId).setUserId(letClueUpdateDTO.getCreatorId()));
        } catch (Exception e) {
            log.error("线索添加创建人失败");
        }
        return letClueId;
    }

}
