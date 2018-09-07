package server.service;

import server.db.primary.model.reportform.ReportStationDataCylOil_JS_Res;
import server.db.primary.model.reportform.ReportStationDataCylOil_RL_Res;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface FunReportStationDataCylOilService {
    List<ReportStationDataCylOil_JS_Res> selectJSReportData(String searchDateStr, Map<String, String> columnMap) throws ParseException;
    boolean updateData(ReportStationDataCylOil_JS_Res reportData);

    List<ReportStationDataCylOil_RL_Res> selectRLReportData(String searchDateStr, Map<String, String> columnMap) throws ParseException;
    boolean updateData(ReportStationDataCylOil_RL_Res reportData);
}
