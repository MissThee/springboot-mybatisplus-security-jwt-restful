package server.db.primary.mapper.reportform;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.reportform.ReportConfWaterStation;
import server.db.primary.model.reportform.ReportConfWaterStation_Group;

import java.util.List;

@Component
public interface FunReportConfWaterStationMapper extends CommonMapper<ReportConfWaterStation> {

    List<ReportConfWaterStation_Group> selectReportConfWaterStationGroup( @Param("areaIds") List<Long> areaIds);
}