package server.service;

import server.db.primary.model.reportform.ReportStationDataCez_Res;
import server.tool.ExcelUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface FunReportStationDataCezService {
    boolean updateData(ReportStationDataCez_Res reportData);

    List<ReportStationDataCez_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnMap) throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
