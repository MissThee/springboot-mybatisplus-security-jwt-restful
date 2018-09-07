package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunAreaInfoMapper;
import server.db.primary.mapper.sysoption.FunWellInfoMapper;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.WellInfo;
import server.service.FunWellInfoService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FunWellInfoImp implements FunWellInfoService {

    @Autowired
    FunWellInfoMapper funWellInfoMapper;
    @Autowired
    FunAreaInfoMapper funAreaInfoMapper;

    @Override
    public PageInfo selectWellPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, Long searchFactoryId, Long searchAreaId, Long searchStationId, Long searchMark, Long searchAutoMark, Date searchStartDate, Date searchEndDate, Long searchNetType) {
        Example example = new Example(WellInfo.class);
        example.selectProperties("id", "wellName", "stationName", "wellTypeName", "oilStorageName", "wellLong", "wellLat", "autoMark", "commTypeName", "rtuIpaddr", "elecAddr", "vMark", "vIpaddr", "vVirtualPort", "mark", "stime");
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
        if (searchAutoMark != null) {
            criteria.andEqualTo("autoMark", searchAutoMark);
        }
        if (searchStartDate != null) {
            criteria.andGreaterThanOrEqualTo("stime", searchStartDate);
        }
        if (searchEndDate != null) {
            criteria.andLessThanOrEqualTo("stime", searchEndDate);
        }
        if (searchNetType != null) {
            criteria.andLessThanOrEqualTo("commType", searchNetType);
        }
        criteria.andIn("areaId", areaIds);
        example.setOrderByClause("area_id,well_num");
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funWellInfoMapper.selectByExample(example));
    }

    @Override
    public WellInfo selectWellOneById(Long id) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andEqualTo("id", id);
        example.selectProperties("id", "wellName", "wellNum", "mark", "areaId", "stationId", "wellType", "stime", "oilStorageType", "autoMark", "commType", "rtuIpaddr", "vMark", "vIpaddr", "vVirtualPort", "wellLong", "wellLat", "runState", "a11Code");
        return funWellInfoMapper.selectOneByExample(example);
    }

    @Override
    public List<WellInfo> selectWellForList() {
        Example example = new Example(WellInfo.class);
        example.selectProperties("id", "wellName");
        example.orderBy("wellName");
//        Example.Criteria criteria = example.createCriteria();
//        if (areaId != null) {
//            criteria.andEqualTo("areaId", areaId);
//        }
//        if (stationType != null) {
//            criteria.andEqualTo("stationType", stationType);
//        }
//        List<String> columnList = new ArrayList<>();
//        for (String column : extraColumn) {
//            columnList.add(column);
//        }
//        columnList.add("id");
//        columnList.add("stationName");
//        example.selectProperties(columnList.toArray(new String[]{}));
        return funWellInfoMapper.selectByExample(example);
    }

    @Override
    public boolean isWellNameExist(String wellName) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andEqualTo("wellName", wellName);
        return funWellInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createWell(WellInfo wellInfo) {
        wellInfo.setWProperty(0L);
        wellInfo.setWellNameAlias(wellInfo.getWellName());
        wellInfo.setGroupName(wellInfo.getWellName());
        return funWellInfoMapper.insertSelective(wellInfo) > 0;
    }

    @Override
    public boolean isWellNameExistExceptSelf(Long id, String wellName) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andEqualTo("wellName", wellName)
                .andNotEqualTo("id", id);
        return funWellInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateWell(WellInfo wellInfo) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andEqualTo("id", wellInfo.getId());
        wellInfo.setWellNameAlias(wellInfo.getWellName());
        wellInfo.setGroupName(wellInfo.getWellName());
        return funWellInfoMapper.updateByExampleSelective(wellInfo, example) > 0;
    }

    @Override
    public boolean deleteWellByIdList(List<Long> idList) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andIn("id", idList);
        return funWellInfoMapper.deleteByExample(example) > 0;
    }

    @Override
    public List<WellInfo> selectWellForListNotInWellGtConfig(List<Long> areaIds) {
        return funWellInfoMapper.selectWellForListNotInWellGtConfig(areaIds);
    }

    @Override
    public List<WellInfo> selectWellForListNotInWellStateMan(List<Long> areaIds) {
        return funWellInfoMapper.selectWellForListNotInWellStateMan(areaIds);
    }
}
