package server.service;

import server.db.primary.model.reportform.ReportConfWaterStation_Group;
import server.db.primary.model.reportform.ReportDataWaterWell_Res;

import java.util.List;

public interface FunReportConfWaterStationService {

    List<ReportConfWaterStation_Group> selectTreeByAreaIds(List<Long> areaIds);
}
