package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportDataOilWellMapper;
import server.db.primary.mapper.reportform.FunReportDataOilWellResMapper;
import server.db.primary.model.reportform.ReportDataOilWell;
import server.db.primary.model.reportform.ReportDataOilWell_Res;
import server.service.FunReportDataOilWellService;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FunReportDataOilWellImp implements FunReportDataOilWellService {

    @Autowired
    FunReportDataOilWellMapper funReportDataOilWellMapper;
    @Autowired
    FunReportDataOilWellResMapper funReportDataOilWellResMapper;

    @Override
    public List<ReportDataOilWell_Res> selectReportData(Long areaId, Long stationId, String searchDateStr, Map<String, String> columnMap) {
        Example example = new Example(ReportDataOilWell_Res.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("areaId", areaId);
        criteria.andEqualTo("reportStationId", stationId);
        criteria.andEqualTo("reportDate", searchDateStr);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause("LOOP_ID,REPORT_WELL_NUM");
        return funReportDataOilWellResMapper.selectByExample(example);
    }

    @Override
    public boolean updateData(ReportDataOilWell_Res reportData) {
        Example example = new Example(ReportDataOilWell_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportDataOilWellResMapper.updateByExampleSelective(reportData, example) > 0;
    }

}
