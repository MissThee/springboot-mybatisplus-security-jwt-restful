package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunAreaInfoMapper;
import server.db.primary.mapper.sysoption.FunStationInfoValveMapper;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.StationInfoValve;
import server.service.FunValveService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunValveImp implements FunValveService {
    @Autowired
    FunStationInfoValveMapper funStationInfoValveMapper;
    @Autowired
    FunAreaInfoMapper funAreaInfoMapper;

    @Override
    public PageInfo selectValvePaged(Integer pageNum, Integer pageSize, Long searchFactoryId, Long searchAreaId, Long searchStationId, Long searchMark) {

        Example example = new Example(StationInfoValve.class);
        example.excludeProperties("stationId", "areaId", "areaName","remark","valveUnit" );
        Example.Criteria criteria = example.createCriteria();
        if (searchStationId != null) {
            criteria.andEqualTo("stationId", searchStationId);
        } else if (searchAreaId != null) {
            criteria.andEqualTo("areaId", searchAreaId);
        } else if (searchFactoryId != null) {
            Example areaExample = new Example(AreaInfo.class);
            areaExample.createCriteria().andEqualTo("coId", searchFactoryId);
            List<AreaInfo> areaInfoList = funAreaInfoMapper.selectByExample(areaExample);
            List<Long> areaIdList = new ArrayList<>();
            for (AreaInfo areaInfo : areaInfoList) {
                areaIdList.add(areaInfo.getId());
            }
            criteria.andIn("areaId", areaIdList);
        }
        if (searchMark != null) {
            criteria.andEqualTo("mark", searchMark);
        }
//        example.orderBy("stationId","valveNameNum");
        example.setOrderByClause("station_id,valve_name_num");
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funStationInfoValveMapper.selectByExample(example));
    }

    @Override
    public StationInfoValve selectValveOneById(Long id) {
        Example example = new Example(StationInfoValve.class);
        example.createCriteria().andEqualTo("id", id);
        example.excludeProperties("stationName", "areaName", "remark" ,"stime");
        return funStationInfoValveMapper.selectOneByExample(example);
    }

    @Override
    public boolean createValve(StationInfoValve valveInfo) {
        return funStationInfoValveMapper.insertSelective(valveInfo)>0;
    }

    @Override
    public boolean updateValve(StationInfoValve valveInfo) {
        Example example = new Example(StationInfoValve.class);
        example.createCriteria()
                .andEqualTo("id", valveInfo.getId());
        return funStationInfoValveMapper.updateByExampleSelective(valveInfo, example) > 0;
    }

    @Override
    public boolean deleteValveByIdList(List<Long> idList) {
        Example example = new Example(StationInfoValve.class);
        example.createCriteria()
                .andIn("id", idList);
        return funStationInfoValveMapper.deleteByExample(example) > 0;
    }
}
