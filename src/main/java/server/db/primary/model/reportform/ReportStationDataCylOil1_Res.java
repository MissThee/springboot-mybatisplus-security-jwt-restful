package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

import java.util.Date;

@Data
@Table(name = "REPORT_STATION_DATA_CYL_OIL1")
public class ReportStationDataCylOil1_Res {
    private Long id;

    @Column(name = "report_hour")
    private String report_hour;//report_hour  时间
    @Column(name = "VAL_FWATER")
    private Double val_fwater;//val_fwater  进油量(m³)
    @Column(name = "val_inoil")
    private Double val_inoil;//val_inoil  外输量(吨)
    @Column(name = "val_wsoil")
    private String val_wsoil;//val_wsoil  楚二站外输量(吨)
    @Column(name = "val_moil")
    private String val_moil;//val_moil  处理量(吨)
    @Column(name = "val_coil")
    private String val_coil;//val_coil  库存(吨)
    @Column(name = "remark")
    private String remark;//remark  备注

    private String reportDate;

    private Double valTwater;

    private String valHs;

    private Short mark;

    private Date stime;


}