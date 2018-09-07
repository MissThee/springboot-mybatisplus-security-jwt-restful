package server.service;

import server.db.primary.model.reportform.ReportStationDataCylOil0_Res;
import server.db.primary.model.reportform.ReportStationDataCylOil1_Res;

import java.util.List;
import java.util.Map;

public interface FunReportStationDataCylOil0Service {

    List<ReportStationDataCylOil0_Res> selectReportForm(String searchDateStr, Map<String, String> formColumnMap);

    boolean updateData(ReportStationDataCylOil0_Res reportData);
}
