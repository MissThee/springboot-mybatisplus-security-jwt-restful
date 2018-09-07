package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportStationDataCylWater0ResMapper;
import server.db.primary.model.reportform.ReportStationDataCylWater0_Res;
import server.service.FunReportStationDataCylWater0Service;
import server.service.FunReportStationDataCylWaterService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportStationDataCylWater0Imp implements FunReportStationDataCylWater0Service {
    @Autowired
    FunReportStationDataCylWater0ResMapper funReportStationDataCylWater0ResMapper;

    @Override
    public List<ReportStationDataCylWater0_Res> selectReportForm(String searchDate, Map<String, String> columnMap) {
        Example example = new Example(ReportStationDataCylWater0_Res.class);
        example.selectProperties();
        example.createCriteria()
                .andEqualTo("reportDate", searchDate);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        return funReportStationDataCylWater0ResMapper.selectByExample(example);
    }

    @Override
    public boolean updateData(ReportStationDataCylWater0_Res reportData) {
        Example example = new Example(ReportStationDataCylWater0_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylWater0ResMapper.updateByExampleSelective(reportData, example) > 0;
    }
}
