package server.service;

import server.db.primary.model.sheet.ReportDataOwaterLoop_Day_Res;

import java.util.List;
import java.util.Map;

public interface SheetSimpleService {

    boolean updateOwaterDayData(ReportDataOwaterLoop_Day_Res reportData);

    List<ReportDataOwaterLoop_Day_Res> selectReportDataDay(Long stationId, String searchDateStr, Map<String, String> columnMap);

}
