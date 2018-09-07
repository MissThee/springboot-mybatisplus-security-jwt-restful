package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunStationInfoMapper;
import server.db.primary.model.sysoption.StationInfo;
import server.service.FunStationInfoService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunStationInfoImp implements FunStationInfoService {

    @Autowired
    FunStationInfoMapper funStationInfoMapper;

    @Override
    public PageInfo selectStationPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, Long stationType, Long searchFactoryId, Long searchAreaId, Long searchMark, Long searchAutoMark) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funStationInfoMapper.selectStationInfoTable(areaIds,stationType, searchFactoryId, searchAreaId, searchMark, searchAutoMark));
    }

    @Override
    public StationInfo selectStationOneById(Long id) {
        return funStationInfoMapper.selectStationInfoTableOneById(id);
    }


    @Override
    public List<StationInfo> selectStationForList(Long areaId, List<Long> areaIds, Long stationType, String... extraColumn) {
        Example example = new Example(StationInfo.class);
//        example.selectProperties("id", "stationName");
        example.orderBy("stationName");
        Example.Criteria criteria = example.createCriteria();
        if (areaId != null) {
            criteria.andEqualTo("areaId", areaId);
        }
        if (stationType != null) {
            criteria.andEqualTo("stationType", stationType);
        }

        criteria.andIn("areaId", areaIds);
        List<String> columnList = new ArrayList<>();
        for (String column : extraColumn) {
            columnList.add(column);
        }
        columnList.add("id");
        columnList.add("stationName");
        example.selectProperties(columnList.toArray(new String[]{}));
        return funStationInfoMapper.selectByExample(example);
    }

    @Override
    public boolean isStationNameExist(String stationName) {
        Example example = new Example(StationInfo.class);
        example.createCriteria()
                .andEqualTo("stationName", stationName);
        return funStationInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createStation(StationInfo stationInfo) {
        return funStationInfoMapper.insertSelective(stationInfo) > 0;
    }

    @Override
    public boolean isStationNameExistExceptSelf(Long id, String stationName) {
        Example example = new Example(StationInfo.class);
        example.createCriteria()
                .andEqualTo("stationName", stationName)
                .andNotEqualTo("id", id);
        return funStationInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateStation(StationInfo stationInfo) {
        Example example = new Example(StationInfo.class);
        example.createCriteria()
                .andEqualTo("id", stationInfo.getId());
        return funStationInfoMapper.updateByExampleSelective(stationInfo, example) > 0;
    }

    @Override
    public boolean deleteStationByIdList(List<Long> idList) {
        Example example = new Example(StationInfo.class);
        example.createCriteria()
                .andIn("id", idList);
        return funStationInfoMapper.deleteByExample(example) > 0;
    }
}
