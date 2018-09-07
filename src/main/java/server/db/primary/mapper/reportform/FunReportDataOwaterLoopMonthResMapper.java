package server.db.primary.mapper.reportform;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month1_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month2_Res;

import java.util.List;

@Component
public interface FunReportDataOwaterLoopMonthResMapper {
    List<ReportDataOwaterLoop_Month1_Res> selectReportDataOwaterMonth1(@Param("searchDate") String searchDate, @Param("columnList") String[] columnList);
    List<ReportDataOwaterLoop_Month2_Res> selectReportDataOwaterMonth2(@Param("searchDate") String searchDate, @Param("columnList") String[] columnList);
}