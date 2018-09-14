package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.common.OrderStr;
import server.db.primary.mapper.sheet.FunReportStationDataCez0ResMapper;
import server.db.primary.mapper.sheet.FunReportStationDataCezResMapper;
import server.db.primary.mapper.sheet.FunReportStationDataCylOilExtraMapper;
import server.db.primary.model.sheet.ReportStationDataCez0_Res;
import server.db.primary.model.sheet.ReportStationDataCez_Res;
import server.db.primary.model.sheet.ReportStationDataCylOil_Extra;
import server.service.SheetComplexService;
import server.tool.ExcelUtils;
import server.tool.ListCompute;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SheetComplexImp implements SheetComplexService {
    @Autowired
    FunReportStationDataCezResMapper funReportStationDataCezResMapper;
    @Autowired
    FunReportStationDataCylOilExtraMapper funReportStationDataCylOilMapper;

    @Override
    public boolean updateReportData(ReportStationDataCez_Res reportData) {
        Example example = new Example(ReportStationDataCez_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCezResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportStationDataCez_Res> selectReportData(String searchDateStr, ArrayList<ExcelUtils.DataColumn> columnList) throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<ReportStationDataCez_Res> resList = new ArrayList<>();
        Set<String> columnKeyTemp = new HashSet<>();
        for (ExcelUtils.DataColumn dataColumn : columnList) {
            if (!dataColumn.getIsEmptyData() && dataColumn.getIsDBColumn()) {
                columnKeyTemp.add(dataColumn.getDataStr());
            }
        }
        int mainListRealSize = 0;
        {
            Example example = new Example(ReportStationDataCez_Res.class);
            example.createCriteria()
                    .andEqualTo("reportDate", searchDateStr);
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
//            example.orderBy("id");
//            example.setOrderByClause(" to_number(substr(report_hour,0,2))");
            example.setOrderByClause(
                    OrderStr.sqlOrderByReportHourDecodeStr);
            List<ReportStationDataCez_Res> mainList = funReportStationDataCezResMapper.selectByExample(example);
            mainListRealSize = mainList.size();
            if (mainList.size() < 12) {
                for (int i = mainList.size(); i < 12; i++) {
                    ReportStationDataCez_Res tempRes = new ReportStationDataCez_Res();
                    if (i < 9) {
                        int hourNumber = (8 + 2 * i);
                        tempRes.setReport_time((hourNumber > 9 ? hourNumber : "0" + hourNumber) + ":00:00");
                    } else {
                        tempRes.setReport_time("0" + 2 * (i - 8) + ":00:00");
                    }
                    mainList.add(tempRes);
                }
            }
            {
                Example example1 = new Example(ReportStationDataCylOil_Extra.class);
                example1.createCriteria()
                        .andEqualTo("reportDate", searchDateStr);
                example1.setOrderByClause(
                        OrderStr.sqlOrderByReportHourDecodeStr);
                List<ReportStationDataCylOil_Extra> extraList = funReportStationDataCylOilMapper.selectByExample(example1);
                for (int i = 0; i < Math.min(mainListRealSize, extraList.size()); i++) {
                    mainList.get(i).setJiarelu1(extraList.get(i).getTi_202());
                    mainList.get(i).setJiarelu2(extraList.get(i).getTi_203());
                    mainList.get(i).setJiarelu3(extraList.get(i).getTi_204());
                }
            }
            resList.addAll(mainList);
        }

        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
            String fixDateStr = sdf.format(calendarDate.getTime());

            Example example = new Example(ReportStationDataCez_Res.class);
            example.createCriteria()
                    .andEqualTo("reportDate", fixDateStr)
                    .andEqualTo("report_time", "06:00:00");
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
            ReportStationDataCez_Res res = funReportStationDataCezResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCez_Res();
            }
            res.setReport_time("上班底数");
            {
                Example example1 = new Example(ReportStationDataCylOil_Extra.class);
                example1.createCriteria()
                        .andEqualTo("reportDate", fixDateStr);
                example1.setOrderByClause(
                        OrderStr.sqlOrderByReportHourDecodeStr);
                ReportStationDataCylOil_Extra extraRes = funReportStationDataCylOilMapper.selectOneByExample(example1);
                res.setJiarelu1(extraRes.getTi_202());
                res.setJiarelu2(extraRes.getTi_203());
                res.setJiarelu3(extraRes.getTi_204());
            }
            resList.add(0, res);
        }
        {
            Example example = new Example(ReportStationDataCez_Res.class);
            Example.Criteria criteria = example.createCriteria();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(sdf.parse(searchDateStr));
            calendarDate.add(Calendar.DAY_OF_YEAR, -1);//日期加10天
            String fixDateStr = sdf.format(calendarDate.getTime());
            criteria.andEqualTo("reportDate", fixDateStr);
            criteria.andEqualTo("report_time", "04:00:00");
            example.selectProperties(columnKeyTemp.toArray(new String[0]));
            ReportStationDataCez_Res res = funReportStationDataCezResMapper.selectOneByExample(example);
            if (res == null) {
                res = new ReportStationDataCez_Res();
            }
            res.setReport_time("上班底数前");
            resList.add(0, res);
        }
        if (resList.size() > 1) {
            for (int i = 1; i < mainListRealSize + 2; i++) {
                resList.get(i).setRanqiliang((resList.get(i).getRq_ljds() == null ? 0 : resList.get(i).getRq_ljds()) - (resList.get(i - 1).getRq_ljds() == null ? 0 : resList.get(i - 1).getRq_ljds()));
                resList.get(i).setYeliang((resList.get(i).getWs_llj() == null ? 0 : resList.get(i).getWs_llj()) - (resList.get(i - 1).getWs_llj() == null ? 0 : resList.get(i - 1).getWs_llj()));
            }
        }
        resList.remove(0);
        {
            Set<String> columnKeyTemp1 = new HashSet<>();
            for (ExcelUtils.DataColumn dataColumn : columnList) {
                if (!"".equals(dataColumn.getDataStr())) {
                    columnKeyTemp1.add(dataColumn.getDataStr());
                }
            }
            ReportStationDataCez_Res resAvg = new ReportStationDataCez_Res();
            resAvg.setReport_time("平均值");
            ListCompute.makeComputeRow(1, mainListRealSize + 1, resList, columnKeyTemp1, resAvg, "avg", new HashMap<>());
            resAvg.setId(null);
            resList.add(resAvg);
        }
        return resList;
    }

    @Autowired
    FunReportStationDataCez0ResMapper funReportStationDataCez0ResMapper;

    @Override
    public boolean updateReportForm(ReportStationDataCez0_Res reportData) {
        Example example = new Example(ReportStationDataCez0_Res.class);
        example.createCriteria()
                .andEqualTo("id", reportData.getId());
        return funReportStationDataCez0ResMapper.updateByExampleSelective(reportData, example) > 0;
    }

    @Override
    public List<ReportStationDataCez0_Res> selectReportForm(String searchDate, Map<String, String> columnMap) {
        Example example = new Example(ReportStationDataCez0_Res.class);
        example.selectProperties();
        example.createCriteria()
                .andEqualTo("reportDate", searchDate);
        example.selectProperties(columnMap.keySet().toArray(new String[0]));
        example.setOrderByClause(
                "DECODE(report_hour,'八点班',1)," +
                        "DECODE(report_hour,'四点班',2)," +
                        "DECODE(report_hour,'零点班',3)," +
                        "DECODE(report_hour,'班小结',4)");
        List<ReportStationDataCez0_Res> mainlist = funReportStationDataCez0ResMapper.selectByExample(example);
        if (mainlist.size() < 4) {
            for (int i = mainlist.size(); i < 4; i++) {
                String reportHour = "";
                switch (i) {
                    case 0:
                        reportHour = "八点班";
                        break;
                    case 1:
                        reportHour = "四点班";
                        break;
                    case 2:
                        reportHour = "零点班";
                        break;
                    case 3:
                        reportHour = "班小结";
                        break;
                }
                ReportStationDataCez0_Res tempRes = new ReportStationDataCez0_Res();
                tempRes.setReport_hour(reportHour);
                mainlist.add(tempRes);
            }
        }
        return mainlist;
    }
}
