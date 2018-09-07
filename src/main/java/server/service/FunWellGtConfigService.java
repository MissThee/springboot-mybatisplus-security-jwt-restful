package server.service;

import com.github.pagehelper.PageInfo;
import server.db.primary.model.sysoption.WellGtConfig;

import java.util.List;
import java.util.Map;

public interface FunWellGtConfigService {

    PageInfo selectWellGtConfigPaged(Integer pageNum, Integer pageSize,List<Long> areaIds, String searchWellName);

    WellGtConfig selectWellGtConfigOneById(Long id);

    boolean isWellGtConfigWellExist(Long wellId);

    Map<String, Object> createWellGtConfig(WellGtConfig wellGtConfig);

    boolean isWellGtConfigWellExistExceptSelf(Long id, Long wellId);

    boolean updateWellGtConfig(WellGtConfig wellGtConfig);

    boolean deleteWellGtConfigByIdList(List<Long> idList);
}
