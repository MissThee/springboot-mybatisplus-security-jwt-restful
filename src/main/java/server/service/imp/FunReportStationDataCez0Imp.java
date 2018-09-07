package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportStationDataCez0ResMapper;
import server.db.primary.model.reportform.ReportStationDataCez0_Res;
import server.service.FunReportStationDataCez0Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportStationDataCez0Imp implements FunReportStationDataCez0Service {
    @Autowired
    FunReportStationDataCez0ResMapper funReportStationDataCez0ResMapper;

    @Override
    public boolean updateData(ReportStationDataCez0_Res reportData) {
        Example example = new Example(ReportStationDataCez0_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCez0ResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportStationDataCez0_Res> selectReportForm(String searchDate, Map<String, String> columnMap) {
        Example example = new Example(ReportStationDataCez0_Res.class);
        example.selectProperties();
        example.createCriteria()
                .andEqualTo("reportDate", searchDate);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause(
                "DECODE(report_hour,'八点班',1)," +
                        "DECODE(report_hour,'四点班',2)," +
                        "DECODE(report_hour,'零点班',3)," +
                        "DECODE(report_hour,'班小结',4)");
        List<ReportStationDataCez0_Res> mainlist = funReportStationDataCez0ResMapper.selectByExample(example);
        if (mainlist.size() < 4) {
            for (int i = mainlist.size(); i < 4; i++) {
                String reportHour = "";
                switch (i) {
                    case 0:
                        reportHour = "八点班";
                        break;
                    case 1:
                        reportHour = "四点班";
                        break;
                    case 2:
                        reportHour = "零点班";
                        break;
                    case 3:
                        reportHour = "班小结";
                        break;
                }
                ReportStationDataCez0_Res tempRes = new ReportStationDataCez0_Res();
                tempRes.setReport_hour(reportHour);
                mainlist.add(tempRes);
            }
        }
        return mainlist;
    }
}


