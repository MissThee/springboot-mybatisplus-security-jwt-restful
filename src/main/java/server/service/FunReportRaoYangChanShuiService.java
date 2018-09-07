package server.service;

import server.db.primary.model.reportform.ReportRaoYangChanShui_Res;
import server.tool.ExcelUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public interface FunReportRaoYangChanShuiService {

    boolean updateData(ReportRaoYangChanShui_Res reportData);

    List<ReportRaoYangChanShui_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnMap) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
