package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.StationInfoWaterwell;

import java.util.List;

public interface FunWaterwellService {
    PageInfo selectWaterwellPaged(Integer pageNum, Integer pageSize, Long searchStationId, Long searchMark);

    StationInfoWaterwell selectWaterwellOneById(Long id);

    boolean isWaterwellNameExist(String wellWaterName);

    boolean createWaterwell(StationInfoWaterwell waterwell);

    boolean isWaterwellNameExistExceptSelf(Long id, String wellWaterName);

    boolean updateWaterwell(StationInfoWaterwell waterwell);

    boolean deleteWaterwellByIdList(List<Long> idList);
}
