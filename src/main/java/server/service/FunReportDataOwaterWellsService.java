package server.service;

import server.db.primary.model.reportform.ReportDataOilWell_Res;
import server.db.primary.model.reportform.ReportDataOwaterWells_Res;

import java.util.List;
import java.util.Map;

public interface FunReportDataOwaterWellsService {
    boolean updateData(ReportDataOwaterWells_Res reportData);

    List<ReportDataOwaterWells_Res> selectReportData(Long stationId, String searchDateStr, Map<String, String> columnMap);
}
