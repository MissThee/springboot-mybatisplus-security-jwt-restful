package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunStationInfoWaterwellMapper;
import server.db.primary.model.sysoption.StationInfoWaterwell;
import server.service.FunWaterwellService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunWaterwellImp implements FunWaterwellService {

    @Autowired
    FunStationInfoWaterwellMapper funStationInfoWaterwellMapper;

    @Override
    public PageInfo selectWaterwellPaged(Integer pageNum, Integer pageSize, Long searchStationId, Long searchMark) {

        Example example = new Example(StationInfoWaterwell.class);
        example.excludeProperties("stationId", "areaId", "areaName", "remark");
        Example.Criteria criteria = example.createCriteria();
        if (searchStationId != null) {
            criteria.andEqualTo("stationId", searchStationId);
        }
        if (searchMark != null) {
            criteria.andEqualTo("mark", searchMark);
        }
        example.orderBy("wellWaterName");
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funStationInfoWaterwellMapper.selectByExample(example));
    }

    @Override
    public StationInfoWaterwell selectWaterwellOneById(Long id) {
        Example example = new Example(StationInfoWaterwell.class);
        example.createCriteria().andEqualTo("id", id);
        example.excludeProperties("stationName", "areaName", "stime", "remark");
        return funStationInfoWaterwellMapper.selectOneByExample(example);
    }

    @Override
    public boolean isWaterwellNameExist(String wellWaterName) {
        Example example = new Example(StationInfoWaterwell.class);
        example.createCriteria()
                .andEqualTo("wellWaterName", wellWaterName);
        return funStationInfoWaterwellMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createWaterwell(StationInfoWaterwell waterwell) {
        return funStationInfoWaterwellMapper.insertSelective(waterwell) > 0;
    }

    @Override
    public boolean isWaterwellNameExistExceptSelf(Long id, String wellWaterName) {
        Example example = new Example(StationInfoWaterwell.class);
        example.createCriteria()
                .andEqualTo("wellWaterName", wellWaterName)
                .andNotEqualTo("id", id);
        return funStationInfoWaterwellMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateWaterwell(StationInfoWaterwell waterwell) {
        Example example = new Example(StationInfoWaterwell.class);
        example.createCriteria()
                .andEqualTo("id", waterwell.getId());
        return funStationInfoWaterwellMapper.updateByExampleSelective(waterwell, example) > 0;
    }

    @Override
    public boolean deleteWaterwellByIdList(List<Long> idList) {
        Example example = new Example(StationInfoWaterwell.class);
        example.createCriteria()
                .andIn("id", idList);
        return funStationInfoWaterwellMapper.deleteByExample(example) > 0;
    }
}
