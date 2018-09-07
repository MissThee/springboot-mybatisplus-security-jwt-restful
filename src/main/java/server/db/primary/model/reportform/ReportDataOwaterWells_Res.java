package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_DATA_OWATER_WELLS")
public class ReportDataOwaterWells_Res {
    private Long id;

    @Column(name ="LOOP_NAME" )
    private String loop_name;//loop_name
    @Column(name ="REPORT_WELL_NUM" )
    private Long report_well_num;//report_well_num
    @Column(name ="REPORT_WELL_NAME" )
    private String report_well_name;//report_well_name
    @Column(name ="PRESS_OIL_08" )
    private Double press_oil_08;//press_oil_08
    @Column(name ="PRESS_OIL_10" )
    private Double press_oil_10;//press_oil_10
    @Column(name ="PRESS_OIL_12" )
    private Double press_oil_12;//press_oil_12
    @Column(name ="PRESS_OIL_14" )
    private Double press_oil_14;//press_oil_14
    @Column(name ="PRESS_OIL_16" )
    private Double press_oil_16;//press_oil_16
    @Column(name ="PRESS_OIL_18" )
    private Double press_oil_18;//press_oil_18
    @Column(name ="PRESS_OIL_20" )
    private Double press_oil_20;//press_oil_20
    @Column(name ="PRESS_OIL_22" )
    private Double press_oil_22;//press_oil_22
    @Column(name ="PRESS_OIL_00" )
    private Double press_oil_00;//press_oil_00
    @Column(name ="PRESS_OIL_02" )
    private Double press_oil_02;//press_oil_02
    @Column(name ="PRESS_OIL_04" )
    private Double press_oil_04;//press_oil_04
    @Column(name ="PRESS_OIL_06" )
    private Double press_oil_06;//press_oil_06

    private String reportDate;

    private Long reportWellId;

    private Long wellId;

    private String wellName;

    private Long reportStationId;

    private String reportStationName;

    private Long loopId;


    private Long areaId;

    private String areaName;



    private String remark;

    private Double mark;

    private Date stime;

}