package server.db.primary.mapper.sysoption;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.WellDataMaintian;

import java.util.List;

@Component
public interface FunWellDataMaintianMapper extends CommonMapper<WellDataMaintian> {

    List<WellDataMaintian> selectWellDataByAreaIds(@Param("areaIds") List<Long> areaIds);
}