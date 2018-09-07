package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportDataOwaterWellsResMapper;
import server.db.primary.model.reportform.ReportDataOilWell_Res;
import server.db.primary.model.reportform.ReportDataOwaterWells_Res;
import server.service.FunReportDataOwaterWellsService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class FunReportDataOwaterWellsImp implements FunReportDataOwaterWellsService {
    @Autowired
    FunReportDataOwaterWellsResMapper funReportDataOwaterWellsResMapper;

    @Override
    public boolean updateData(ReportDataOwaterWells_Res reportData) {
        Example example = new Example(ReportDataOwaterWells_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportDataOwaterWellsResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportDataOwaterWells_Res> selectReportData(Long stationId, String searchDateStr, Map<String, String> columnMap) {
        Example example = new Example(ReportDataOwaterWells_Res.class);
        example.createCriteria()
                .andEqualTo("reportStationId", stationId)
                .andEqualTo("reportDate", searchDateStr);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause("LOOP_ID,REPORT_WELL_NUM");
        return funReportDataOwaterWellsResMapper.selectByExample(example);
    }
}
