package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.WellDataMaintian;

import java.util.List;

public interface FunWellDataMaintianService {
    PageInfo selectWellPaged(Integer pageNum, Integer pageSize, List<Long> areaIds);

    WellDataMaintian selectWellOneById(Long id);

    boolean isWellIdExist(Long wellId);

    boolean isWellIdExistExceptSelf(Long id, Long wellId);

    boolean deleteWellByIdList(List<Long> idList);

    boolean createWellData(WellDataMaintian wellDataMaintian);

    boolean updateWellData(WellDataMaintian wellDataMaintian);
}
