package server.service;

import server.db.primary.model.reportform.ReportRaoYangZongHe_Res;
import server.tool.ExcelUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public interface FunReportRaoYangZongHeService {
    List<ReportRaoYangZongHe_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    boolean updateData(ReportRaoYangZongHe_Res reportData);
}
