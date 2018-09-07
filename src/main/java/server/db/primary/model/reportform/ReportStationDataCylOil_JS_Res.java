package server.db.primary.model.reportform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_STATION_DATA_CYL_OIL")

public class ReportStationDataCylOil_JS_Res {
    private Long id;

    @Column(name = "REPORT_HOUR")
    private String report_time; //report_time  时间

    @Column(name = "LI_321")        private Double  li_321;
    @Column(name = "CYGJM1")        private Double  cygjm1;
    @Column(name = "LI_322")        private Double  li_322;
    @Column(name = "CYGJM2")        private Double  cygjm2;
    @Column(name = "LI_323")        private Double  li_323;
    @Column(name = "CYGJM3")        private Double  cygjm3;
    @Column(name = "LI_326")        private Double  li_326;
    @Column(name = "LI_327")        private Double  li_327;
    @Column(name = "LI_324")        private Double  li_324;
    @Column(name = "CYGJM5")        private Double  cygjm5;
    @Column(name = "LI_325")        private Double  li_325;
    @Column(name = "CYGJM6")        private Double  cygjm6;
    @Column(name = "PI_301")        private Double  pi_301;
    @Column(name = "FLQWD1")        private Double  flqwd1;
    @Column(name = "LI_302")        private Double  li_302;
    @Column(name = "LI_301")        private Double  li_301;
    @Column(name = "PI_302")        private Double  pi_302;
    @Column(name = "FLQWD2")        private Double  flqwd2;
    @Column(name = "LI_315")        private Double  li_315;
    @Column(name = "LI_314")        private Double  li_314;
    @Column(name = "PI_303")        private Double  pi_303;
    @Column(name = "PI_305")        private Double  pi_305;
    @Column(name = "TI_301")        private Double  ti_301;
    @Column(name = "TI_304")        private Double  ti_304;
    @Column(name = "PI_306")        private Double  pi_306;
    @Column(name = "TI_305")        private Double  ti_305;
    @Column(name = "TI_302")        private Double  ti_302;
    @Column(name = "TI_303")        private Double  ti_303;
    @Column(name = "PI_307")        private Double  pi_307;
    @Column(name = "TI_306")        private Double  ti_306;
    @Column(name = "WSDS1")         private Double  wsds1;
    @Column(name = "WSYL1")         private Double  wsyl1;
    @Column(name = "WSDS2")         private Double  wsds2;
    @Column(name = "WSYL2")         private Double  wsyl2;
    @Column(name = "TI_101")        private Double  ti_101;
    @Column(name = "PI_101")        private Double  pi_101;
    @Column(name = "FT_101")        private Double  ft_101;
    @Column(name = "FTQ_101")       private Double  ftq_101;
    @Column(name = "TI_106")        private Double  ti_106;
    @Column(name = "PI_102")        private Double  pi_102;
    @Column(name = "TI_103")        private Double  ti_103;
    @Column(name = "TI_102")        private Double  ti_102;
    @Column(name = "JYXH")          private String  jyxh;
    @Column(name = "JYL")           private Double  jyl;

    private String reportDate;

//    private Double gia101;
//
//    private Double gia104;
//
//    private Double ti105;
//
//    private Double pi103;
//
//    private Double ti107;
//
//    private Double ti108;
//
//    private Double ti109;
//
//    private Double ti110;
//
//    private Double ti111;
//
//    private Double ti112;
//
//    private Double ti113;
//
//    private Double ti114;
//
//    private Double pi104;
//
//    private Double gia102;
//
//    private Double gia103;
//
//    private Double pi201;
//
//    private Double ti201;
//
//    private Double ti202;
//
//    private Double ti203;
//
//    private Double ti204;
//
//    private Double ti205;
//
//    private Double ti206;
//
//    private Double pi202;
//
//    private Double pi203;
//
//    private Double ti207;
//
//    private Double pi204;
//
//    private Double ti208;
//
//    private Double pi304;
//
//    private Double ti307;
//
//    private Double pi310;
//
//    private Double mt301;
//
//    private Double pi308;
//
//    private Double li201;
//
//    private Double li202;
//
//    private Double li303;
//
//    private Double li316;
//
//    private Double li304;
//
//    private Double li305;
//
//    private Double li306;
//
//    private Double li309;
//
//    private Double li310;
//
//    private Double li311;
//
//    private Double li312;
//
//    private Double li313;
//
//    private Double zi101;
//
//    private Double zi102;
//
//    private Double zi205;
//
//    private Double zi206;
//
//    private Double zi301;
//
//    private Double zi302;
//
//    private Double zi303;
//
//    private Double zi304;
//
//    private Double zi305;
//
//    private Double zi311;
//
//    private Double zi312;
//
//    private Double zi308;
//
//    private Double zi201;
//
//    private Double heart;
//
//    private Double ft102;
//
//    private Double ftq102;
//
//    private Double ft103;
//
//    private Double ftq103;
//
//    private Double ft104;
//
//    private Double ftq104;
//
//    private Double ft201;
//
//    private Double ftq201;
//
//    private Double ft202;
//
//    private Double ftq202;
//
//    private Double e1cwpaU;
//
//    private Double e1cwpaI;
//
//    private Double e1cwpbU;
//
//    private Double e1cwpbI;
//
//    private Double e1cwpcU;
//
//    private Double e1cwpcI;
//
//    private Double e1cwpZy;
//
//    private Double e1cwpZw;
//
//    private Double e1cwpZ;
//
//    private Double e2cwpaU;
//
//    private Double e2cwpaI;
//
//    private Double e2cwpbU;
//
//    private Double e2cwpbI;
//
//    private Double e2cwpcU;
//
//    private Double e2cwpcI;
//
//    private Double e2cwpZy;
//
//    private Double e2cwpZw;
//
//    private Double e2cwpZ;
//
//    private Double e1hwpaU;
//
//    private Double e1hwpaI;
//
//    private Double e1hwpbU;
//
//    private Double e1hwpbI;
//
//    private Double e1hwpcU;
//
//    private Double e1hwpcI;
//
//    private Double e1hwpZy;
//
//    private Double e1hwpZw;
//
//    private Double e1hwpZ;
//
//    private Double e2hwpaU;
//
//    private Double e2hwpaI;
//
//    private Double e2hwpbU;
//
//    private Double e2hwpbI;
//
//    private Double e2hwpcU;
//
//    private Double e2hwpcI;
//
//    private Double e2hwpZy;
//
//    private Double e2hwpZw;
//
//    private Double e2hwpZ;
//
//    private Double e3hwpaU;
//
//    private Double e3hwpaI;
//
//    private Double e3hwpbU;
//
//    private Double e3hwpbI;
//
//    private Double e3hwpcU;
//
//    private Double e3hwpcI;
//
//    private Double e3hwpZy;
//
//    private Double e3hwpZw;
//
//    private Double e3hwpZ;
//
//    private Double e1wspaU;
//
//    private Double e1wspaI;
//
//    private Double e1wspbU;
//
//    private Double e1wspbI;
//
//    private Double e1wspcU;
//
//    private Double e1wspcI;
//
//    private Double e1wspZy;
//
//    private Double e1wspZw;
//
//    private Double e1wspZ;
//
//    private Double e2wspaU;
//
//    private Double e2wspaI;
//
//    private Double e2wspbU;
//
//    private Double e2wspbI;
//
//    private Double e2wspcU;
//
//    private Double e2wspcI;
//
//    private Double e2wspZy;
//
//    private Double e2wspZw;
//
//    private Double e2wspZ;
//
//    private Double twpaU;
//
//    private Double twpaI;
//
//    private Double twpbU;
//
//    private Double twpbI;
//
//    private Double twpcU;
//
//    private Double twpcI;
//
//    private Double twpZy;
//
//    private Double twpZw;
//
//    private Double twpZ;
//
//    private Double ftsp102;
//
//    private Double ftsmv102;
//
//    private Double ftsp103;
//
//    private Double ftsmv103;
//
//    private Double ftsp104;
//
//    private Double ftsmv104;
//
//    private Double zi4160;
//
//    private Double zi4170;
//
//    private Double zi4180;
//
//    private Double mcls;
//
//    private String ryl;
//
//    private String ryyl;
//
//    private String rsl;
//
//    private String bz;
//
//    private Short mark;
//
//    private Date stime;
//
//    private Double cygkr1;
//
//    private Double cygkr2;
//
//    private Double cygkr3;
//
//    private Double cygjm4;
//
//    private Double cygkr4;
//
//    private Double cygkr5;
//
//    private Double cygkr6;
//
//    private Double syjSs2;
//
//    private Double syjLj2;
//
//    private Double lngDs;
//
//    private Double lngYl;
//
//    private Double zcqDs;
//
//    private Double zcqYl;
//
//    private Double jrlCk1;
//
//    private Double jrlCk2;
//
//    private Double jrlCk3;
//
//    private Double rybGy1;
//
//    private Double rybWd1;
//
//    private Double rybGy2;
//
//    private Double rybWd2;

}