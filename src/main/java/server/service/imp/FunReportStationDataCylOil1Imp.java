package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportStationDataCylOil1Mapper;
import server.db.primary.model.reportform.ReportStationDataCylOil1_Res;
import server.service.FunReportStationDataCylOil1Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportStationDataCylOil1Imp implements FunReportStationDataCylOil1Service {
    @Autowired
    FunReportStationDataCylOil1Mapper funReportStationDataCylOil1Mapper;

    @Override
    public List<ReportStationDataCylOil1_Res> selectReportForm(String searchDate, Map<String, String> columnMap) {
        Example example = new Example(ReportStationDataCylOil1_Res.class);
        example.selectProperties();
        example.createCriteria()
                .andEqualTo("reportDate", searchDate);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause(
                "DECODE(report_hour,'八点班',1)," +
                        "DECODE(report_hour,'四点班',2)," +
                        "DECODE(report_hour,'零点班',3)," +
                        "DECODE(report_hour,'班小结',4)");
        List<ReportStationDataCylOil1_Res> mainlist = funReportStationDataCylOil1Mapper.selectByExample(example);
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
                    ReportStationDataCylOil1_Res tempRes = new ReportStationDataCylOil1_Res();
                    tempRes.setReport_hour(reportHour);
                    mainlist.add(tempRes);
            }
        }
        return mainlist;
    }

    @Override
    public boolean updateData(ReportStationDataCylOil1_Res reportData) {
        Example example = new Example(ReportStationDataCylOil1_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylOil1Mapper.updateByExampleSelective(reportData, example) > 0;
    }
}
