package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.common.OrderStr;
import server.db.primary.mapper.reportform.FunReportStationDataCylWaterResMapper;
import server.db.primary.model.reportform.ReportStationDataCylWater_Res;
import server.service.FunReportStationDataCylWaterService;
import server.tool.ExcelUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FunReportStationDataCylWaterImp implements FunReportStationDataCylWaterService {
    @Autowired
    FunReportStationDataCylWaterResMapper funReportStationDataCylWaterResMapper;

    @Override
    public boolean updateData(ReportStationDataCylWater_Res reportData) {
        Example example = new Example(ReportStationDataCylWater_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylWaterResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportStationDataCylWater_Res> selectReportData(String searchDateStr, List<ExcelUtils.DataColumn> dataColumnList) throws ParseException {
        List<ReportStationDataCylWater_Res> resList = new ArrayList<>();
        Set<String> columnKeyTemp = new HashSet<>();
        for (ExcelUtils.DataColumn dataColumn : dataColumnList) {
            if (!dataColumn.getIsEmptyData() && dataColumn.getIsDBColumn()) {
                columnKeyTemp.add(dataColumn.getDataStr());
            }
        }
        int mainListRealSize = 0;
        {
            Example example = new Example(ReportStationDataCylWater_Res.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("reportDate", searchDateStr);
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
//            example.orderBy("id");
//            example.setOrderByClause(" to_number(substr(report_hour,0,2))");
            example.setOrderByClause(
                    OrderStr.sqlOrderByReportHourDecodeStr);
            List<ReportStationDataCylWater_Res> mainlist = funReportStationDataCylWaterResMapper.selectByExample(example);
            mainListRealSize = mainlist.size();
            if (mainlist.size() < 12) {
                for (int i = mainlist.size(); i < 12; i++) {
                    ReportStationDataCylWater_Res tempRes = new ReportStationDataCylWater_Res();
                    if (i < 9) {
                        int hourNumber = (8 + 2 * i);
                        tempRes.setReport_time((hourNumber > 9 ? hourNumber : "0" + hourNumber) + ":00:00");
                    } else {
                        tempRes.setReport_time("0" + 2 * (i - 8) + ":00:00");
                    }
                    mainlist.add(tempRes);
                }
            }

            resList.addAll(mainlist);
        }
        {
            Example example = new Example(ReportStationDataCylWater_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "06:00:00");
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
            ReportStationDataCylWater_Res res = funReportStationDataCylWaterResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCylWater_Res();
            }
            res.setReport_time("上班底数");
            resList.add(0, res);
        }
        {
            Example example = new Example(ReportStationDataCylWater_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "04:00:00");
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
            ReportStationDataCylWater_Res res = funReportStationDataCylWaterResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCylWater_Res();
            }
            res.setReport_time("上班底数前");
            resList.add(0, res);
        }
        if (resList.size() > 1) {
            for (int i = 1; i < mainListRealSize + 2; i++) {
                resList.get(i).setShuiliang1((resList.get(i).getFt_403() == null ? 0 : resList.get(i).getFt_403()) - (resList.get(i - 1).getFt_403() == null ? 0 : resList.get(i - 1).getFt_403()));
                resList.get(i).setShuiliang2((resList.get(i).getFtq_404() == null ? 0 : resList.get(i).getFtq_404()) - (resList.get(i - 1).getFtq_404() == null ? 0 : resList.get(i - 1).getFtq_404()));
            }
        }
        resList.remove(0);
        return resList;


    }
}
