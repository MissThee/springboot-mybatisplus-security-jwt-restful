package server.db.primary.mapper.sysoption;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.AreaInfo_Table;

import java.util.List;

@Component
public interface FunAreaInfoMapper extends CommonMapper<AreaInfo> {
    AreaInfo selectAreaInfoTableOneById(Long id);

    List<AreaInfo_Table> selectAreaInfoTable( );
}