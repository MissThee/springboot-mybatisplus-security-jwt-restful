package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportStationDataCylOil0Mapper;
import server.db.primary.mapper.reportform.FunReportStationDataCylOil1Mapper;
import server.db.primary.model.reportform.ReportStationDataCylOil0_Res;
import server.service.FunReportStationDataCylOil0Service;
import server.service.FunReportStationDataCylOil1Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportStationDataCylOil0Imp implements FunReportStationDataCylOil0Service {
    @Autowired
    FunReportStationDataCylOil0Mapper funReportStationDataCylOil0Mapper;

    @Override
    public List<ReportStationDataCylOil0_Res> selectReportForm(String searchDate, Map<String, String> columnMap) {
        Example example = new Example(ReportStationDataCylOil0_Res.class);
        example.selectProperties();
        example.createCriteria()
                .andEqualTo("reportDate", searchDate);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause(
                "DECODE(report_hour,'八点班',1)," +
                        "DECODE(report_hour,'四点班',2)," +
                        "DECODE(report_hour,'零点班',3)," +
                        "DECODE(report_hour,'班小结',4)");
        funReportStationDataCylOil0Mapper.selectByExample(example);
        List<ReportStationDataCylOil0_Res> mainlist = funReportStationDataCylOil0Mapper.selectByExample(example);
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
                ReportStationDataCylOil0_Res tempRes = new ReportStationDataCylOil0_Res();
                tempRes.setReport_hour(reportHour);
                mainlist.add(tempRes);
            }
        }
        return mainlist;

    }

    @Override
    public boolean updateData(ReportStationDataCylOil0_Res reportData) {
        Example example = new Example(ReportStationDataCylOil0_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylOil0Mapper.updateByExampleSelective(reportData, example) > 0;
    }
}
