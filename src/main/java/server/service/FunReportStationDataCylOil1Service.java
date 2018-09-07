package server.service;

import server.db.primary.model.reportform.ReportStationDataCylOil1_Res;

import java.util.List;
import java.util.Map;

public interface FunReportStationDataCylOil1Service {

    List<ReportStationDataCylOil1_Res> selectReportForm(String searchDateStr, Map<String, String> formColumnMap);

    boolean updateData(ReportStationDataCylOil1_Res reportData);
}
