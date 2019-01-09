package server.db.primary.model.sheet;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "REPORT_STATION_DATA_CEZ")
@Accessors(chain = true)
public class ComplexSheetData {
    private Long id;

    private String reportDate;

    @Column(name ="REPORT_HOUR")     private String report_time    ;
    @Column(name ="CYG_YW1")         private Double cyg_yw1        ;                             //储油罐
    @Column(name ="CYG_JM1")         private Double cyg_jm1        ;
    @Column(name ="CYG_KR1")         private Double cyg_kr1        ;
    @Column(name ="CYG_YW2")         private Double cyg_yw2        ;
    @Column(name ="CYG_JM2")         private Double cyg_jm2        ;
    @Column(name ="CYG_KR2")         private Double cyg_kr2        ;
    @Column(name ="PI_1150")         private Double pi_1150        ;                             //外输泵
    @Column(name ="TI_1270")         private Double ti_1270        ;
    @Column(name ="FT109")           private Double ft109          ;
    @Column(name ="TI_1250")         private Double ti_1250        ;                             //进站（来液升温）换热器
    @Column(name ="PI_1090")         private Double pi_1090        ;
    @Column(name ="TI_1260")         private Double ti_1260        ;
    @Column(name ="PI_1100")         private Double pi_1100        ;
    @Column(name ="TI_1230")         private Double ti_1230        ;
    @Column(name ="TI_1240")         private Double ti_1240        ;
    @Column(name ="PI_1140")         private Double pi_1140        ;                             //分离缓冲罐
    @Column(name ="PI_1110")         private Double pi_1110        ;
    @Column(name ="PI_1120")         private Double pi_1120        ;
    @Column(name ="TI_1200")         private Double ti_1200        ;                             //加热炉
    @Column(name ="PI_1070")         private Double pi_1070        ;
    @Transient                       private Double jiarelu1       ;
    @Transient                       private Double jiarelu2       ;
    @Transient                       private Double jiarelu3       ;
    @Column(name ="RQ_CKYL")         private Double rq_ckyl        ;                              //    燃气
    @Column(name ="RQ_LJDS")         private Double rq_ljds        ;
    @Transient                       private Double ranqiliang     ;
    @Column(name ="PI_1060")         private Double pi_1060        ;                             //热水泵
    @Column(name ="TI_1150")         private Double ti_1150        ;
    @Column(name ="FT104S")          private Double ft104s         ;
    @Column(name ="FT_1040")         private Double ft_1040        ;
    @Column(name ="RSB_BY1")         private Double rsb_by1        ;
    @Column(name ="RSB_BY2")         private Double rsb_by2        ;
    @Column(name ="RSB_BY3")         private Double rsb_by3        ;
    @Column(name ="LT_1030")         private Double lt_1030        ;                             //热回水罐
    @Column(name ="HSG_WD")          private Double hsg_wd         ;                             //
    @Column(name ="PI_1080")         private Double pi_1080        ;                             //燃油泵
    @Column(name ="TI_1210")         private Double ti_1210        ;
    @Column(name ="PI_1010")         private Double pi_1010        ;                             //掺水泵
    @Column(name ="TI_1010")         private Double ti_1010        ;
    @Column(name ="FT_1010")         private Double ft_1010        ;
    @Column(name ="FT101S")          private Double ft101s         ;
    @Column(name ="CSHRQ_HGWD")      private Double cshrq_hgwd     ;                             //掺水换热器
    @Column(name ="TI_1020")         private Double ti_1020        ;
    @Column(name ="TI_1030")         private Double ti_1030        ;
    @Column(name ="TI_1220")         private Double ti_1220        ;                             //燃油换热器
    @Column(name ="WS_YL")           private Double ws_yl          ;                             //    外输记录
    @Column(name ="WS_WD")           private Double ws_wd          ;
    @Column(name ="WS_LLJ")          private Double ws_llj         ;
    @Transient                       private Double yeliang        ;

}