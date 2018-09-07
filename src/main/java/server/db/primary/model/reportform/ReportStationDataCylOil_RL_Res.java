package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_STATION_DATA_CYL_OIL")
public class ReportStationDataCylOil_RL_Res {
    private Long id;

    @Column(name = "REPORT_HOUR")
    private String report_time; //report_time  时间

    @Column(name = "TI_208") private Double  ti_208;
    @Column(name = "PI_204") private Double  pi_204;
    @Column(name = "TI_206") private Double  ti_206;
    @Column(name = "PI_202") private Double  pi_202;
    @Column(name = "JRL_CK1") private Double jrl_ck1;
    @Column(name = "TI_202") private Double  ti_202;
    @Column(name = "JRL_CK2") private Double jrl_ck2;
    @Column(name = "TI_203") private Double  ti_203;
    @Column(name = "JRL_CK3") private Double jrl_ck3;
    @Column(name = "TI_204") private Double  ti_204;
    @Column(name = "FT_201") private Double  ft_201;
    @Column(name = "FTQ_201") private Double ftq_201;
    @Column(name = "SYJ_SS2") private Double syj_ss2;
    @Column(name = "SYJ_LJ2") private Double syj_lj2;
    @Column(name = "PI_201") private Double  pi_201;
    @Column(name = "TI_201") private Double  ti_201;
    @Column(name = "FT_202") private Double  ft_202;
    @Column(name = "FTQ_202") private Double ftq_202;
    @Column(name = "PI_203") private Double  pi_203;
    @Column(name = "TI_207") private Double  ti_207;
    @Column(name = "RYB_GY1") private Double ryb_gy1;
    @Column(name = "RYB_WD1") private Double ryb_wd1;
    @Column(name = "RYB_GY2") private Double ryb_gy2;
    @Column(name = "RYB_WD2") private Double ryb_wd2;
    @Column(name = "LNG_DS") private Double  lng_ds;
    @Column(name = "LNG_YL") private Double  lng_yl;
    @Column(name = "ZCQ_DS") private Double  zcq_ds;
    @Column(name = "ZCQ_YL") private Double  zcq_yl;


    private String reportDate;
}