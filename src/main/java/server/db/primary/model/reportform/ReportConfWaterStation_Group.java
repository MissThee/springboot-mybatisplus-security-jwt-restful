package server.db.primary.model.reportform;

import lombok.Data;

import java.util.List;

@Data
public class ReportConfWaterStation_Group {
    private Long id;
    private String type = "AREA";
    private String label;

    private List<ReportConfWaterStation_Child> child;

    @Data
    static class ReportConfWaterStation_Child {
        private Long id;
        private String type = "STATION";
        private String label;
    }
}