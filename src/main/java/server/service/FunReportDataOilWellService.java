package server.service;

import server.db.primary.model.reportform.ReportDataOilWell;
import server.db.primary.model.reportform.ReportDataOilWell_Res;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FunReportDataOilWellService {
    List<ReportDataOilWell_Res> selectReportData(Long areaId,Long stationId, String searchDateStr, Map<String,String> columnMap);

    boolean updateData(ReportDataOilWell_Res reportData);
}
