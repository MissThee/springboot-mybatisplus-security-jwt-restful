package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.StationInfo;

import java.util.List;

public interface FunStationInfoService {
    PageInfo selectStationPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, Long stationType, Long searchFactoryId, Long searchAreaId, Long searchMark, Long searchAutoMark);

    StationInfo selectStationOneById(Long id);

    List<StationInfo> selectStationForList(Long areaId, List<Long> areaIds, Long stationType, String... extraColumn);

    boolean isStationNameExist(String stationName);

    boolean createStation(server.db.primary.model.sysoption.StationInfo stationInfo);

    boolean isStationNameExistExceptSelf(Long id, String stationName);

    boolean updateStation(server.db.primary.model.sysoption.StationInfo stationInfo);

    boolean deleteStationByIdList(List<Long> idList);
}
