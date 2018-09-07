package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportConfWaterStationMapper;
import server.db.primary.model.reportform.ReportConfWaterStation_Group;
import server.db.primary.model.reportform.ReportDataWaterWell_Res;
import server.service.FunReportConfWaterStationService;

import java.util.List;

@Service
public class FunReportConfWaterStationImp implements FunReportConfWaterStationService {
    @Autowired
    FunReportConfWaterStationMapper funReportConfWaterStationMapper;

    @Override
    public List<ReportConfWaterStation_Group> selectTreeByAreaIds(List<Long> areaIds) {
        return funReportConfWaterStationMapper.selectReportConfWaterStationGroup(areaIds);
    }
}
