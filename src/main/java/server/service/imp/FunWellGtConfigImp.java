package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunWellGtConfigMapper;
import server.db.primary.mapper.sysoption.FunWellInfoMapper;
import server.db.primary.model.sysoption.WellGtConfig;
import server.db.primary.model.sysoption.WellInfo;
import server.service.FunWellGtConfigService;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunWellGtConfigImp implements FunWellGtConfigService {

    @Autowired
    FunWellGtConfigMapper funWellGtConfigMapper;
    @Autowired
    FunWellInfoMapper funWellInfoMapper;

    @Override
    public PageInfo selectWellGtConfigPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, String searchWellName) {
        Example example = new Example(WellGtConfig.class);
        example.selectProperties("id", "wellName", "dym", "bg", "bj", "ygnj", "yctgnj", "rod1OsDiameter", "rod1Length", "rod2OsDiameter", "rod2Length", "rod3OsDiameter", "rod3Length", "rod4OsDiameter", "rod4Length", "stime");
        Example.Criteria criteria = example.createCriteria();
        if (searchWellName != null) {
            criteria.andLike("wellName", "%" + searchWellName + "%");
        }
        criteria.andIn("areaId", areaIds);
        example.setOrderByClause("area_id,well_name");
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funWellGtConfigMapper.selectByExample(example));
    }

    @Override
    public WellGtConfig selectWellGtConfigOneById(Long id) {
        Example example = new Example(WellGtConfig.class);
        example.selectProperties("id", "wellId", "wellName", "yymd", "smd", "trqxdmd", "ysrjqyb", "bhyl", "dmtqyynd", "yqcyl", "yqczbsd", "yqczbwd", "hsl", "yy", "ty", "hy", "dym", "bg", "jklw", "scqyb", "blx", "bjb", "bj", "zsc", "ygnj", "yctgnj", "rod1Type", "rod1OsDiameter", "rod1Grade", "rod1Length", "rod2Type", "rod2OsDiameter", "rod2Grade", "rod2Length", "rod3Type", "rod3OsDiameter", "rod3Grade", "rod3Length", "rod4Type", "rod4OsDiameter", "rod4Grade", "rod4Length", "mdzt", "qmflxl", "clxs");
        example.createCriteria()
                .andEqualTo("id", id);
        return funWellGtConfigMapper.selectOneByExample(example);
    }

    @Override
    public boolean isWellGtConfigWellExist(Long wellId) {
        Example example = new Example(WellGtConfig.class);
        example.createCriteria()
                .andEqualTo("wellId", wellId);
        return funWellGtConfigMapper.selectByExample(example).size() > 0;
    }

    @Override
    public Map<String, Object> createWellGtConfig(WellGtConfig wellGtConfig) {
        Map<String, Object> map = new HashMap<>();
        WellInfo wellInfo = new WellInfo();
        wellInfo.setId(wellGtConfig.getWellId());

        wellInfo = funWellInfoMapper.selectOne(wellInfo);
        if (wellInfo != null) {
            wellGtConfig.setWellName(wellInfo.getWellName());
            wellGtConfig.setWellNum(wellInfo.getWellNum());
            wellGtConfig.setAreaId(wellInfo.getAreaId());
            wellGtConfig.setAreaName(wellInfo.getAreaName());
            map.put("result", funWellGtConfigMapper.insertSelective(wellGtConfig) > 0);
            map.put("msg", "成功");
            return map;
        } else {
            map.put("result", false);
            map.put("msg", "失败,无此井,[wellId]无法找到对应井信息");
            return map;
        }
    }

    @Override
    public boolean isWellGtConfigWellExistExceptSelf(Long id, Long wellId) {
        Example example = new Example(WellGtConfig.class);
        example.createCriteria()
                .andEqualTo("wellId", wellId)
                .andNotEqualTo("id", id);
        return funWellGtConfigMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateWellGtConfig(WellGtConfig wellGtConfig) {
        Example example = new Example(WellGtConfig.class);
        example.createCriteria()
                .andEqualTo("id", wellGtConfig.getId());
        return funWellGtConfigMapper.updateByExampleSelective(wellGtConfig, example) > 0;
    }

    @Override
    public boolean deleteWellGtConfigByIdList(List<Long> idList) {
        Example example = new Example(WellGtConfig.class);
        example.createCriteria()
                .andIn("id", idList);
        return funWellGtConfigMapper.deleteByExample(example) > 0;
    }
}
