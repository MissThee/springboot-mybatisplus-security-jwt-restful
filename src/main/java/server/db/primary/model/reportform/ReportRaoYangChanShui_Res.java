package server.db.primary.model.reportform;

import lombok.Data;

@Data
public class ReportRaoYangChanShui_Res {
    private Long id;
    private String name;
    private Long open_wells=0L;
    private Double liquid_day=0D;
    private Double oil_day=0D;
    private Double containing=0D;
    private Double temp_water=0D;
    private Double press_water=0D;
    private Double water_val=0D;
    private Double temp_oil=0D;
    private Double liquid_val=0D;
}
