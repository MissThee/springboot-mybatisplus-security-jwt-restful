package server.db.primary.mapper.reportform;

import org.springframework.stereotype.Component;
import server.db.primary.model.reportform.ReportRaoYangChanShui_Res;

import java.util.List;

@Component
public interface FunRaoYangChanShuiMapper {
    List<ReportRaoYangChanShui_Res> selectTable(String searchDate);
}
