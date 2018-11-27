package server.service;

import server.db.primary.model.sheet.ReportStationDataCez0_Res;
import server.db.primary.model.sheet.ReportStationDataCez_Res;
import server.tool.ExcelExport;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SheetComplexService {
    boolean updateReportData(ReportStationDataCez_Res reportData);

    List<ReportStationDataCez_Res> selectReportData(String searchDateStr, ArrayList<ExcelExport.DataColumn> columnMap) throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    boolean updateReportForm(ReportStationDataCez0_Res reportData);

    List<ReportStationDataCez0_Res> selectReportForm(String searchDateStr, Map<String, String> formColumnMap);
}
