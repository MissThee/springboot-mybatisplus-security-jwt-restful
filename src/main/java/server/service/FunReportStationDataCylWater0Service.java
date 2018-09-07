package server.service;

import server.db.primary.model.reportform.ReportStationDataCylWater0_Res;

import java.util.List;
import java.util.Map;

public interface FunReportStationDataCylWater0Service {
    List<ReportStationDataCylWater0_Res> selectReportForm(String searchDate, Map<String, String> columnMap);

    boolean updateData(ReportStationDataCylWater0_Res reportData);
}
