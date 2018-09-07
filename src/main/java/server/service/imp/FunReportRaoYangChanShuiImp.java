package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunRaoYangChanShuiMapper;
import server.db.primary.model.reportform.ReportRaoYangChanShui_Res;
import server.service.FunReportRaoYangChanShuiService;
import server.tool.ExcelUtils;
import server.tool.ListCompute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.*;

@Service
public class FunReportRaoYangChanShuiImp implements FunReportRaoYangChanShuiService {
    @Autowired
    FunRaoYangChanShuiMapper funRaoYangChanShuiMapper;

    @Override
    public List<ReportRaoYangChanShui_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnMap) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<ReportRaoYangChanShui_Res> resList = funRaoYangChanShuiMapper.selectTable(searchDateStr);
        {
            ReportRaoYangChanShui_Res resSum = new ReportRaoYangChanShui_Res();
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
        return resList;
    }

    @Override
    public boolean updateData(ReportRaoYangChanShui_Res reportData) {
        return false;
    }
}
