package server.db.primary.mapper.reportform;

import org.springframework.stereotype.Component;
import server.db.primary.model.reportform.ReportRaoYangZongHe_Res;

import java.util.List;

@Component
public interface FunRaoYangZongHeMapper {
    List<ReportRaoYangZongHe_Res> selectTable(String searchDate);
}
