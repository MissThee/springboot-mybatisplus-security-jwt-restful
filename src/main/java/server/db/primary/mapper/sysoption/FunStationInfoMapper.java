package server.db.primary.mapper.sysoption;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.StationInfo;

import java.util.List;

@Component
public interface FunStationInfoMapper extends CommonMapper<StationInfo> {

//    List<StationInfo> selectStationInfoTable(@Param("idList") List idList);

    StationInfo selectStationInfoTableOneById(Long id);

    List<StationInfo> selectStationInfoTable(@Param("areaIds") List<Long> areaIds,
                                             @Param("stationType") Long stationType,
                                             @Param("searchFactoryId") Long searchFactoryId,
                                             @Param("searchAreaId") Long searchAreaId,
                                             @Param("searchMark") Long searchMark,
                                             @Param("searchAutoMark") Long searchAutoMark);
}