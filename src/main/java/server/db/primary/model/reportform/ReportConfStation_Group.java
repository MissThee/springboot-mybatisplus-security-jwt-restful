package server.db.primary.model.reportform;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportConfStation_Group {
    private Long id;
    private String type = "AREA";
    private String label;

    private List<ReportConfStation_Child> child;

    @Data
    static class ReportConfStation_Child {
        private Long id;
        private String type = "STATION";
        private String label;
    }
}
