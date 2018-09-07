package server.db.primary.model.reportform;

import lombok.Data;

@Data
public class ReportRaoYangZongHe_Res {
    private Long id;
    private String name;
    private Long total_wells = 0L;
    private Long open_wells = 0L;
    private Long stop_wells = 0L;
    private Long job_wells = 0L;
    private Long abnormal_wells = 0L;
    private Double liquid_daily = 0D;
    private Double oil_daily = 0D;
    private Double containing = 0D;
    private Long ab_communication = 0L;
    private Long stoppage_machine = 0L;
    private Long over_alarm = 0L;
    private Long working_alarm = 0L;
}
