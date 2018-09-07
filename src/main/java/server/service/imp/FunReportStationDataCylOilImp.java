package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.common.OrderStr;
import server.db.primary.mapper.reportform.FunReportStationDataCylOilJSResMapper;
import server.db.primary.mapper.reportform.FunReportStationDataCylOilRLResMapper;
import server.db.primary.model.reportform.ReportStationDataCylOil_JS_Res;
import server.db.primary.model.reportform.ReportStationDataCylOil_RL_Res;
import server.service.FunReportStationDataCylOilService;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FunReportStationDataCylOilImp implements FunReportStationDataCylOilService {

    @Autowired
    FunReportStationDataCylOilJSResMapper funReportStationDataCylOilJSResMapper;
    @Autowired
    FunReportStationDataCylOilRLResMapper funReportStationDataCylOilRLResMapper;

    @Override
    public List<ReportStationDataCylOil_JS_Res> selectJSReportData(String searchDateStr, Map<String, String> columnMap) throws ParseException {
        List<ReportStationDataCylOil_JS_Res> resList = new ArrayList<>();
        {
            Example example = new Example(ReportStationDataCylOil_JS_Res.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("reportDate", searchDateStr);
            example.selectProperties(columnMap.keySet().toArray(new String[0]));
//            example.orderBy("id");
//            example.setOrderByClause(" to_number(substr(report_hour,0,2))");
            example.setOrderByClause(
                    OrderStr.sqlOrderByReportHourDecodeStr);
            List<ReportStationDataCylOil_JS_Res> mainlist = funReportStationDataCylOilJSResMapper.selectByExample(example);
            if (mainlist.size() < 12) {
                for (int i = mainlist.size(); i < 12; i++) {
                    ReportStationDataCylOil_JS_Res tempRes = new ReportStationDataCylOil_JS_Res();
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
            Example example = new Example(ReportStationDataCylOil_JS_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "06:00:00");
            example.selectProperties(columnMap.keySet().toArray(new String[0]));
            ReportStationDataCylOil_JS_Res res = funReportStationDataCylOilJSResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCylOil_JS_Res();
            }
            res.setReport_time("上班底数");
            resList.add(0, res);
        }

        {
            Example example = new Example(ReportStationDataCylOil_JS_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "06:00:00");
            //不能进行合计的列，实例中的列名
            List<String> noGroupByColumnList = new ArrayList<String>() {{
                add("report_date");
                add("report_time");
                add("jyxh");
            }};
            List<String> inGroupByColumnList = new ArrayList<>(columnMap.keySet());
            inGroupByColumnList.removeAll(noGroupByColumnList);
            example.selectProperties(inGroupByColumnList.toArray(new String[0]));
            example.orderBy("report_time");
            ReportStationDataCylOil_JS_Res res = funReportStationDataCylOilJSResMapper.selectAvgByExample(example);
            if (res == null) {
                res = new ReportStationDataCylOil_JS_Res();
            }
            res.setId(null);
            res.setReport_time("平均值");
            resList.add(res);

        }
        return resList;
    }

    @Override
    public boolean updateData(ReportStationDataCylOil_JS_Res reportData) {
        Example example = new Example(ReportStationDataCylOil_JS_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylOilJSResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportStationDataCylOil_RL_Res> selectRLReportData(String searchDateStr, Map<String, String> columnMap) throws ParseException {
        List<ReportStationDataCylOil_RL_Res> resList = new ArrayList<>();
        {
            Example example = new Example(ReportStationDataCylOil_RL_Res.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("reportDate", searchDateStr);
            example.selectProperties(columnMap.keySet().toArray(new String[0]));
//            example.orderBy("id");
//            example.setOrderByClause(" to_number(substr(report_hour,0,2))");
            example.setOrderByClause(
                    OrderStr.sqlOrderByReportHourDecodeStr);
            List<ReportStationDataCylOil_RL_Res> mainlist = funReportStationDataCylOilRLResMapper.selectByExample(example);
            if (mainlist.size() < 12) {
                for (int i = mainlist.size(); i < 12; i++) {
                    ReportStationDataCylOil_RL_Res tempRes = new ReportStationDataCylOil_RL_Res();
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
            Example example = new Example(ReportStationDataCylOil_RL_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "06:00:00");
            example.selectProperties(columnMap.keySet().toArray(new String[0]));
            ReportStationDataCylOil_RL_Res res = funReportStationDataCylOilRLResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCylOil_RL_Res();
            }
            res.setReport_time("上班底数");
            resList.add(0, res);
        }

//        {
//            Example example = new Example(ReportStationDataCylOil_RL_Res.class);
//            Example.Criteria criteria = example.createCriteria();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar calendarDate = Calendar.getInstance();
//            calendarDate.setTime(sdf.parse(searchDateStr));
//            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
//            String fixDateStr = sdf.format(calendarDate.getTime());
//            criteria.andEqualTo("reportDate", fixDateStr);
//            criteria.andEqualTo("report_time", "06:00:00");
//            //不能进行合计的列，实例中的列名
//            List<String> noGroupByColumnList = new ArrayList<String>() {{
//                add("report_date");
//                add("report_time");
//                add("jyxh");
//            }};
//            List<String> inGroupByColumnList = new ArrayList<>(columnMap.keySet());
//            inGroupByColumnList.removeAll(noGroupByColumnList);
//            example.selectProperties(inGroupByColumnList.toArray(new String[0]));
//            example.orderBy("report_time");
//            ReportStationDataCylOil_RL_Res res = funReportStationDataCylOilRLResMapper.selectAvgByExample(example);
//            if (res == null) {
//                res = new ReportStationDataCylOil_RL_Res();
//            }
//            res.setId(-1L);
//            res.setReport_time("平均值");
//            resList.add(res);
//
//        }
        return resList;
    }

    @Override
    public boolean updateData(ReportStationDataCylOil_RL_Res reportData) {
        Example example = new Example(ReportStationDataCylOil_RL_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCylOilRLResMapper.updateByExampleSelective(reportData, example) > 0;
    }
}
