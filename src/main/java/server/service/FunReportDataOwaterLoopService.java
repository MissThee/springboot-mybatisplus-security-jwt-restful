package server.service;

import server.db.primary.model.reportform.ReportDataOwaterLoop_Day_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month1_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month2_Res;

import java.util.List;
import java.util.Map;

public interface FunReportDataOwaterLoopService {

    boolean updateOwaterDayData(ReportDataOwaterLoop_Day_Res reportData);

    List<ReportDataOwaterLoop_Day_Res> selectReportDataDay(Long stationId, String searchDateStr, Map<String, String> columnMap);

    List<ReportDataOwaterLoop_Month1_Res> selectReportDataMonth1(Long stationId, String searchDate, Map<String, String> columnMap);

    List<ReportDataOwaterLoop_Month2_Res> selectReportDataMonth2(Long stationId, String searchDate, Map<String, String> owaterMonth1ColumnMap);
}
