package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.common.OrderStr;
import server.db.primary.mapper.sheet.FunReportDataOwaterLoopDayResMapper;
import server.db.primary.model.sheet.ReportDataOwaterLoop_Day_Res;
import server.service.SheetSimpleService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SheetSimpleImp implements SheetSimpleService {
    @Autowired
    FunReportDataOwaterLoopDayResMapper funReportDataOwaterLoopResMapper;

    @Override
    public boolean updateOwaterDayData(ReportDataOwaterLoop_Day_Res reportData) {
        Example example = new Example(ReportDataOwaterLoop_Day_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportDataOwaterLoopResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportDataOwaterLoop_Day_Res> selectReportDataDay(Long stationId, String searchDateStr, Map<String, String> columnMap) {
        Example example = new Example(ReportDataOwaterLoop_Day_Res.class);
        example.createCriteria()
                .andEqualTo("reportStationId", stationId)
                .andEqualTo("reportDate", searchDateStr);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause("REPORT_STATION_ID,REPORT_LOOP_ID," +
                OrderStr.sqlOrderByReportHourCaseWhenStr +
                ",ID");
        return funReportDataOwaterLoopResMapper.selectByExample(example);
    }

}
