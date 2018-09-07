package server.service;

import server.db.primary.model.reportform.ReportDataWaterWell_Res;

import java.util.List;
import java.util.Map;

public interface FunReportDataWaterWellService {
    List<ReportDataWaterWell_Res> selectReportData(Long stationId, Long areaId, String searchDateStr, Map<String,String> columnMap);
    boolean updateData(ReportDataWaterWell_Res reportData);
}
