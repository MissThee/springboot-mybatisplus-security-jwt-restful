package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunRaoYangZongHeMapper;
import server.db.primary.model.reportform.ReportRaoYangZongHe_Res;
import server.service.FunReportRaoYangZongHeService;
import server.tool.ExcelUtils;
import server.tool.ListCompute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class FunReportRaoYangZongHeImp implements FunReportRaoYangZongHeService {
    @Autowired
    FunRaoYangZongHeMapper funRaoYangZongHeMapper;

    @Override
    public List<ReportRaoYangZongHe_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<ReportRaoYangZongHe_Res> resList = funRaoYangZongHeMapper.selectTable(searchDateStr);
        {
            ReportRaoYangZongHe_Res resSum = new ReportRaoYangZongHe_Res();
            resSum.setName("总计");
            Set<String> columnKeyTemp1 = new HashSet<>();
            for (ExcelUtils.DataColumn dataColumn : columnMap) {
                if (!"".equals(dataColumn.getDataStr())) {
                    columnKeyTemp1.add(dataColumn.getDataStr());
                }
            }
            Map<String, String> computeTypeMap = new HashMap<String, String>() {{
                put("containing", "avg");
            }};
            ListCompute.makeComputeRow(0, resList.size(), resList, columnKeyTemp1, resSum, "sum", computeTypeMap);
            resSum.setId(null);
            resList.add(resSum);
        }
        return
                resList;
    }

    @Override
    public boolean updateData(ReportRaoYangZongHe_Res reportData) {
        return false;
    }
}
