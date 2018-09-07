package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.AreaInfo;

import java.util.List;

public interface FunAreaInfoService {
    PageInfo selectAreaPaged(Integer pageNum, Integer pageSize );

    AreaInfo selectAreaOneById(Long id);

    List<AreaInfo> selectAreaForList(Long factoryId, List<Long> areaIds, String... extraColumn);

    boolean isAreaNameExist(String areaName);

    boolean createArea(AreaInfo cLogin);

    boolean isAreaNameExistExceptSelf(Long id, String areaName);

    boolean updateArea(AreaInfo cLogin);

    boolean deleteAreaByIdList(List<Long> idList);

    AreaInfo selectAreaById(Long id);
}
