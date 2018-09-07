package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
@Data
@Table(name = "REPORT_STATION_DATA_CYL_WATER")
public class ReportStationDataCylWater_Res {
    private Long id;

    private String reportDate;

    @Column(name ="REPORT_HOUR"  )private String report_time;//report_time

     @Column(name ="PI_419"  ) private Double  pi_419;        //泵压
     @Column(name ="PI_418"  ) private Double  pi_418;        //泵压
     @Column(name ="PI_417"  ) private Double  pi_417;        //泵压
     @Column(name ="PI_416"  ) private Double  pi_416;        //泵压
     @Column(name ="PI_415"  ) private Double  pi_415;        //泵压
     @Column(name ="PI_414"  ) private Double  pi_414;        //泵压
     @Column(name ="PI_413"  ) private Double  pi_413;        //泵压
     @Column(name ="PI_412"  ) private Double  pi_412;        //泵压
     @Column(name ="PI_411"  ) private Double  pi_411;        //泵压
     @Column(name ="PI_420"  ) private Double  pi_420;        //干压
     @Column(name ="FT_403"  ) private Double  ft_403;        //出口汇管流量计读数(累计)
     @Column(name ="FT_888"  ) private Double  ft_888;        //出口汇管流量计读数(瞬时)
     @Transient                private Double  shuiliang1;    //FT_403-上一个FT_403 //水量
     @Column(name ="FTQ_404" ) private Double  ftq_404;       //回水汇管流量计读数(累计)
     @Column(name ="FT_404"  ) private Double  ft_404;        //回水汇管流量计读数(瞬时)
     @Transient                private Double  shuiliang2;    //FTQ_404-上一个FTQ_404 //水量
     @Column(name ="PI_410"  ) private Double  pi_410;        //出口汇管压力
     @Column(name ="LI_406"  ) private Double  li_406;        //调节罐
     @Column(name ="LI_405"  ) private Double  li_405;        //沉降罐
     @Column(name ="LI_407"  ) private Double  li_407;        //污水罐1
     @Column(name ="LI_408"  ) private Double  li_408;        //污水罐2
     @Column(name ="FT_888"  ) private Double  ft_888_1;      //总注水量
     @Transient                private Double  yewei1;        //液位
     @Transient                private Double  yewei2;        //液位

     @Column(name ="LI_401"  ) private Double  li_401;        //液位
     @Column(name ="LI_402"  ) private Double  li_402;        //液位
     @Column(name ="PI_401"  ) private Double  pi_401;        //泵压
     @Column(name ="PI_402"  ) private Double  pi_402;        //泵压
     @Column(name ="PI_403"  ) private Double  pi_403;        //泵压

     @Column(name ="LI_403"  ) private Double  li_403;        //泵压
     @Column(name ="LI_404"  ) private Double  li_404;        //泵压

     @Column(name ="PI_404"  ) private Double  pi_404;        //进口压力
     @Column(name ="PI_405"  ) private Double  pi_405;        //出口压力
     @Column(name ="PI_408"  ) private Double  pi_408;        //一级过滤出口压力
     @Column(name ="FTQ_401" ) private Double  ftq_401;       //出口流量(累计)
     @Column(name ="FT_401"  ) private Double  ft_401;        //出口流量(瞬时)

     @Column(name ="PI_406"  ) private Double  pi_406;        //进口压力
     @Column(name ="PI_407"  ) private Double  pi_407;        //出口压力
     @Column(name ="PI_409"  ) private Double  pi_409;        //一级过滤出口压力
     @Column(name ="FTQ_402" ) private Double  ftq_402;       //出口流量(累计)
     @Column(name ="FT_402"  ) private Double  ft_402;        //出口流量(瞬时)

     @Column(name ="JYBH"    ) private Double  jybh;
     @Column(name ="JYHS"    ) private Double  jyhs;
     @Column(name ="JYZG"    ) private Double  jyzg;
     @Column(name ="JYJS"    ) private Double  jyjs;
     @Column(name ="JYXN"    ) private Double  jyxn;
     @Column(name ="WSB_BY1" ) private Double  wsb_by1;
     @Column(name ="WSB_BY2" ) private Double  wsb_by2;
     @Column(name ="WSB_BY3" ) private Double  wsb_by3;
     @Column(name ="WSHY"    ) private Double  wshy;
     @Column(name ="QSB_BY"  ) private Double  qsb_by;
     @Column(name ="QSB_SS"  ) private Double  qsb_ss;
     @Column(name ="QSB_LJ"  ) private Double  qsb_lj;

}
