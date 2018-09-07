package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.WellInfo;

import java.util.Date;
import java.util.List;

public interface FunWellInfoService {


    PageInfo selectWellPaged(Integer pageNum, Integer pageSize,List<Long> areaIds, Long searchFactoryId, Long searchAreaId, Long searchStationId, Long searchMark, Long searchAutoMark, Date searchStartDate, Date searchEndDate, Long searchNetType);

    WellInfo selectWellOneById(Long id);

    List<WellInfo> selectWellForList( );

    boolean isWellNameExist(String wellName);

    boolean createWell(WellInfo wellInfo);

    boolean isWellNameExistExceptSelf(Long id, String wellName);

    boolean updateWell(WellInfo wellInfo);

    boolean deleteWellByIdList(List<Long> idList);

    List<WellInfo> selectWellForListNotInWellGtConfig(List<Long> areaIds);

    List<WellInfo> selectWellForListNotInWellStateMan(List<Long> areaIds);
}
