package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportDataWaterWellResMapper;
import server.db.primary.model.reportform.ReportDataWaterWell_Res;
import server.service.FunReportDataWaterWellService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportDataWaterWellImp implements FunReportDataWaterWellService {
    @Autowired
    FunReportDataWaterWellResMapper funReportDataWaterWellResMapper;

    @Override
    public List<ReportDataWaterWell_Res> selectReportData(Long stationId, Long areaId, String searchDateStr, Map<String, String> columnMap) {
        Example example = new Example(ReportDataWaterWell_Res.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("areaId", areaId);
        criteria.andEqualTo("reportStationId", stationId);
        criteria.andEqualTo("reportDate", searchDateStr);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause("AREA_ID,REPORT_STATION_ID,REPORT_WATER_WELL_ID");
        return funReportDataWaterWellResMapper.selectByExample(example);
    }

    @Override
    public boolean updateData(ReportDataWaterWell_Res reportData) {
        Example example = new Example(ReportDataWaterWell_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportDataWaterWellResMapper.updateByExampleSelective(reportData, example) > 0;
    }
}
