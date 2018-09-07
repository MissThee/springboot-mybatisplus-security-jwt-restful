package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunWellInfoMapper;
import server.db.primary.mapper.sysoption.FunWellStateManMapper;
import server.db.primary.model.sysoption.WellInfo;
import server.db.primary.model.sysoption.WellStateMan;
import server.service.FunWellStateManService;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunWellStateManImp implements FunWellStateManService {
    @Autowired
    FunWellStateManMapper funWellStateManMapper;
    @Autowired
    FunWellInfoMapper funWellInfoMapper;

    @Override
    public PageInfo selectWellStateManPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, String searchWellName, Long searchAreaId, Long searchStateTypeId, Long searchMark) {
        Example example = new Example(WellStateMan.class);
        example.excludeProperties("wellId", "areaId", "stationId", "stateTypeId","opName","opName0");
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("areaId", areaIds);
        if (searchWellName != null) {
            criteria.andLike("wellName", "%" + searchWellName + "%");
        }
        criteria.andEqualTo("areaId", searchAreaId);
        criteria.andEqualTo("stateTypeId", searchStateTypeId);
        criteria.andEqualTo("mark", searchMark);

        example.setOrderByClause("area_id,well_name");
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funWellStateManMapper.selectByExample(example));
    }

    @Override
    public WellStateMan selectWellStateManOneById(Long id) {
        Example example = new Example(WellStateMan.class);
        example.selectProperties("id", "wellId", "wellName", "stateTypeId", "mark");
        example.createCriteria()
                .andEqualTo("id", id);
        return funWellStateManMapper.selectOneByExample(example);
    }

    @Override
    public boolean isWellStateManWellExist(Long wellId) {
        Example example = new Example(WellStateMan.class);
        example.createCriteria()
                .andEqualTo("wellId", wellId);
        return funWellStateManMapper.selectByExample(example).size() > 0;
    }

    @Override
    public Map<String, Object> createWellStateMan(WellStateMan wellStateMan) {
        Map<String, Object> map = new HashMap<>();
        WellInfo wellInfo = new WellInfo();
        wellInfo.setId(wellStateMan.getWellId());

        wellInfo = funWellInfoMapper.selectOne(wellInfo);
        if (wellInfo != null) {
            wellStateMan.setWellName(wellInfo.getWellName());
            wellStateMan.setAreaId(wellInfo.getAreaId());
            wellStateMan.setAreaName(wellInfo.getAreaName());
            map.put("result", funWellStateManMapper.insertSelective(wellStateMan) > 0);
            map.put("msg", "成功");
            return map;
        } else {
            map.put("result", false);
            map.put("msg", "失败,无此井,[wellId]无法找到对应井信息");
            return map;
        }
    }

    @Override
    public boolean updateWellStateMan(WellStateMan wellStateMan) {
        Example example = new Example(WellStateMan.class);
        example.createCriteria()
                .andEqualTo("id", wellStateMan.getId());
        wellStateMan.setWellId(null);
        wellStateMan.setWellName(null);
        return funWellStateManMapper.updateByExampleSelective(wellStateMan, example) > 0;
    }

    @Override
    public boolean deleteWellStateManByIdList(List<Long> idList) {
        return false;
//        Example example = new Example(WellStateMan.class);
//        example.createCriteria()
//                .andIn("id", idList);
//        return funWellStateManMapper.deleteByExample(example) > 0;
    }
}
