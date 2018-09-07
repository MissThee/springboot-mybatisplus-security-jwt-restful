package server.service;

import server.db.primary.model.reportform.ReportStationDataCylWater_Res;
import server.tool.ExcelUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface FunReportStationDataCylWaterService {
    boolean updateData(ReportStationDataCylWater_Res reportData);

    List<ReportStationDataCylWater_Res> selectReportData(String searchDateStr, List<ExcelUtils.DataColumn> dataColumnList) throws ParseException;
}
