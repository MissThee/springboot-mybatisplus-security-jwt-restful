package server.service;

import server.db.primary.model.reportform.ReportConfStation_Group;

import java.util.List;

public interface FunReportConfStationService {
    List<ReportConfStation_Group> selectTreeByAreaIds(List<Long> areaIds);

    List<ReportConfStation_Group> selectTreeByAreaIdsAndIdRange(List<Long> areaIds,Long id);
}
