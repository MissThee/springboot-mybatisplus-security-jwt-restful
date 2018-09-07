package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.WellStateMan;

import java.util.List;
import java.util.Map;

public interface FunWellStateManService {
    PageInfo selectWellStateManPaged(Integer pageNum, Integer pageSize, List<Long> areaIds, String searchWellName, Long searchAreaId, Long searchStateTypeId, Long searchMark);

    WellStateMan selectWellStateManOneById(Long id);

    boolean isWellStateManWellExist(Long wellId);

    Map<String, Object> createWellStateMan(WellStateMan wellStateMan);

    boolean updateWellStateMan(WellStateMan wellStateMan);

    boolean deleteWellStateManByIdList(List<Long> idList);

}
