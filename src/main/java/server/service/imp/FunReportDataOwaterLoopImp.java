package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.common.OrderStr;
import server.db.primary.mapper.reportform.FunReportDataOwaterLoopDayResMapper;
import server.db.primary.mapper.reportform.FunReportDataOwaterLoopMonthResMapper;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Day_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month1_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month2_Res;
import server.service.FunReportDataOwaterLoopService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportDataOwaterLoopImp implements FunReportDataOwaterLoopService {
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

    @Autowired
    FunReportDataOwaterLoopMonthResMapper funReportDataOwaterLoopMonthResMapper;

    @Override
    public List<ReportDataOwaterLoop_Month1_Res> selectReportDataMonth1(Long stationId, String searchDate, Map<String, String> columnMap) {
        return funReportDataOwaterLoopMonthResMapper.selectReportDataOwaterMonth1(searchDate, columnMap.keySet().toArray(new String[0]));
    }

    @Override
    public List<ReportDataOwaterLoop_Month2_Res> selectReportDataMonth2(Long stationId, String searchDate, Map<String, String> columnMap) {
        return funReportDataOwaterLoopMonthResMapper.selectReportDataOwaterMonth2(searchDate, columnMap.keySet().toArray(new String[0]));
    }
}
