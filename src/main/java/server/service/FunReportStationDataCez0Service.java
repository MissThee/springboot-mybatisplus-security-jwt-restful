package server.service;

import server.db.primary.model.reportform.ReportStationDataCez0_Res;

import java.util.List;
import java.util.Map;

public interface FunReportStationDataCez0Service {
    boolean updateData(ReportStationDataCez0_Res reportData);

    List<ReportStationDataCez0_Res> selectReportForm(String searchDateStr, Map<String, String> formColumnMap);
}
