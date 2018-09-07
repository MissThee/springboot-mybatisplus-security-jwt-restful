package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.StationInfoValve;

import java.util.List;

public interface FunValveService {
    PageInfo selectValvePaged(Integer pageNum, Integer pageSize, Long searchFactoryId, Long searchAreaId, Long searchStationId, Long searchMark);

    StationInfoValve selectValveOneById(Long id);

    boolean createValve(StationInfoValve valveInfo);

    boolean updateValve(StationInfoValve valveInfo);

    boolean deleteValveByIdList(List<Long> idList);
}
