package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.reportform.FunReportConfStationMapper;
import server.db.primary.model.reportform.ReportConfStation_Group;
import server.service.FunReportConfStationService;

import java.util.List;

@Service
public class FunReportConfStationImp implements FunReportConfStationService {
    @Autowired
    FunReportConfStationMapper funReportConfStationMapper;

    @Override
    public List<ReportConfStation_Group> selectTreeByAreaIds(List<Long> areaIds) {
//        Example example = new Example(ReportConfStation.class);
//        example.selectProperties("id", "reportStationName", "areaId", "areaName");
//        example.createCriteria()
//                .andIn("areaId", areaIds);
//
//        List<ReportConfStation> reportConfStationList = funReportConfStationMapper.selectByExample(example);
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (ReportConfStation reportConfStation : reportConfStationList) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("","");
//            list.add();
//        }
        return funReportConfStationMapper.selectReportConfStationGroup(areaIds);
    }

    @Override
    public List<ReportConfStation_Group> selectTreeByAreaIdsAndIdRange(List<Long> areaIds, Long id) {
        return funReportConfStationMapper.selectReportConfStationGroupInIdRange(areaIds,id);
    }
}
