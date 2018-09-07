package server.db.primary.mapper.reportform;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import server.db.common.CommonMapper;
import server.db.primary.model.reportform.ReportConfStation;
import server.db.primary.model.reportform.ReportConfStation_Group;

import java.util.List;

@Component
public interface FunReportConfStationMapper extends CommonMapper<ReportConfStation> {
    List<ReportConfStation_Group> selectReportConfStationGroup(@Param("areaIds") List<Long> areaIds);
    List<ReportConfStation_Group> selectReportConfStationGroupInIdRange(@Param("areaIds") List<Long> areaIds,@Param("id") Long id);
}