package server.tool.excel.exports.direct;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import server.tool.excel.template.SimpleCell;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static server.tool.excel.exports.direct.DefaultStyle.*;
import static server.tool.excel.reflection.GetterAndSetter.invokeGetMethod;
import static server.tool.excel.response.ResponseTool.responseOut;

//通用简单表格导出工具，调用本工具的Controller返回类型需为void
public class ExcelExport {

    /**
     * 表格导出工具，导出List< Model>数据源的规律列表
     *
     * @param wb               Workbook对象
     * @param sheetIndex       表格下标，0开始【一般为0】
     * @param startRowIndex    插入行号，0开始【一般为0】
     * @param startColumnIndex 插入列号，0开始【一般为0】
     * @param title            表第一行总标题 如："表格标题"；若为null忽略总标题行【字符串】
     * @param dataColumnList   表头行。(字段名，显示列名)  对应map,如：put("实体类属性名","列名"),若put("实体类属性名","")，则生成表时不生成此列【自行构建LinkedHashMap< String ,string>】
     * @param showHeaderColumn 是否插入表头行。（简单表格可为true；复杂表头为false，使用extraHeaderCell做表头）【一般为true】
     * @param dataList         数据集合，需与表头数组中的字段名一致，并且符合javabean规范【自行查询数据List< Model>】
     * @param withIndex        是否插入序号列【一般为true】
     * @param extraHeaderCell  在列名行前的额外列名行，专用于制作复杂表头。HeaderColumn不含x,y时，每个List为一行，HeaderColumn(内容,合并列数) ;含x,y时使用x,y定位插入【一般为null】
     * @return int 返回最后插入数据的行下标+1，调用此方法后，新建行时可直接使用返回的坐标值
     * @throws Exception 抛出异常
     */
    @SafeVarargs
    public static <T> CellPoint addRows(
            Workbook wb,
            int sheetIndex,
            int startRowIndex,
            int startColumnIndex,
            String title,
            List<DataColumn> dataColumnList,
            Boolean showHeaderColumn,
            List<T> dataList,
            Boolean withIndex,
            List<SimpleCell>... extraHeaderCell) throws Exception {

        CellStyle titleStyle = titleStyle(wb);
        CellStyle headerStyle = headerStyle(wb);
        CellStyle dataStyle = dataStyle(wb);

        CellPoint cellPoint = new CellPoint(startRowIndex, startColumnIndex);

        //删去列对象中，值为""或null的对象
        dataColumnList = dataColumnList.stream().filter(e -> !StringUtils.isEmpty(e.getHeaderName())).collect(Collectors.toList());
        //新建工作簿
        Sheet sheet = buildSheet(wb, sheetIndex);
        //添加标题
        addTitle(sheet, titleStyle, cellPoint, dataList, withIndex, title);
        //自定义表头
        addExtraHeader(sheet, headerStyle, cellPoint, extraHeaderCell);
        //每列数据标准表头
        addHeader(sheet, headerStyle, cellPoint, dataColumnList, withIndex, showHeaderColumn);
        //循环插入数据行
        addData(sheet, dataStyle, cellPoint, dataColumnList, dataList, withIndex);
        return cellPoint;
    }

    private static <T> CellPoint addData(Sheet sheet, CellStyle dataStyle, CellPoint cellPoint, List<DataColumn> dataColumnList, List<T> dataList, boolean withIndex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (dataList != null) {
            // 遍历集合数据，产生数据行
            Iterator<T> it = dataList.iterator();
            int indexNumber = 1;//序号列自增数字
            while (it.hasNext()) {
                Row rowData = sheet.createRow(cellPoint.getY());
                int columnIndex = cellPoint.getOriginX();
                if (withIndex) {
                    //  序号列
                    Cell sequenceCellValue = rowData.createCell(columnIndex);// 序号值永远是第0列
                    sequenceCellValue.setCellValue(indexNumber++);
                    sequenceCellValue.setCellStyle(dataStyle);
                    columnIndex++;
                }
                T t = it.next();
                // 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
                {
                    for (DataColumn dataColumn : dataColumnList) {
                        Cell dataCell = rowData.createCell(columnIndex);
                        short dataFormatBack = dataStyle.getDataFormat();
                        //写入值
                        if (!dataColumn.getIsEmptyData()) {
                            Object value = invokeGetMethod(t, dataColumn.getModelPropertyName());
                            if (value != null) {
                                if (value instanceof Float || value instanceof Double) {
                                    dataStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("@"));
                                    double newValue = new BigDecimal(value.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    dataCell.setCellValue(newValue);
                                    setColumnWidth(sheet, columnIndex, newValue, true, 1);
                                } else if (value instanceof Long || value instanceof Integer) {
                                    dataStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("@"));
                                    long newValue = (long) value;
                                    dataCell.setCellValue(newValue);
                                    setColumnWidth(sheet, columnIndex, newValue, true, 1);
                                } else {
                                    dataCell.setCellValue(value.toString());// 为当前列赋值
                                    setColumnWidth(sheet, columnIndex, value.toString(), true, 1);
                                }
                                dataStyle.setDataFormat(dataFormatBack);
                            }
                        }
                        if (dataColumn.getWidth() > 1) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(cellPoint.getY(), cellPoint.getY(), columnIndex, columnIndex + dataColumn.getWidth() - 1);
                            sheet.addMergedRegion(cellRangeAddress);
                            setRegionStyle(sheet, cellRangeAddress, dataStyle);
                        } else {
                            dataCell.setCellStyle(dataStyle);
                        }
                        columnIndex = columnIndex + dataColumn.getWidth();
                        cellPoint.setX(columnIndex);
                    }
                }
                cellPoint.moveXY(1);

            }
        }
        return cellPoint;
    }

    private static CellPoint addHeader(Sheet sheet, CellStyle headerStyle, CellPoint cellPoint, List<DataColumn> dataColumnList, boolean withIndex, boolean showHeaderColumn) {
        if (showHeaderColumn) {
            cellPoint.setXToOrigin();
            Row rowHeader = sheet.createRow(cellPoint.getY());
            int columnIndex = cellPoint.getOriginX();
            if (withIndex) {
                // 第一列添加序号
                String columnName = "序号";
                Cell sequenceCell = rowHeader.createCell(cellPoint.getX());
                sequenceCell.setCellValue(columnName);
                sequenceCell.setCellStyle(headerStyle);
                setColumnWidth(sheet, cellPoint.getX(), columnName, true, 1);
                cellPoint.moveX(1);
            }
            // 为标题行赋值
            {
                for (DataColumn dataColumn : dataColumnList) {
                    String columnName = dataColumn.getHeaderName();
                    Cell titleCell = rowHeader.createCell(cellPoint.getX());
                    titleCell.setCellValue(columnName);
                    if (dataColumn.getWidth() > 1) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(cellPoint.getY(), cellPoint.getY(), cellPoint.getX(), cellPoint.getX() + dataColumn.getWidth() - 1);
                        sheet.addMergedRegion(cellRangeAddress);
                        setRegionStyle(sheet, cellRangeAddress, headerStyle);
                    } else {
                        titleCell.setCellStyle(headerStyle);
                    }
                    setColumnWidth(sheet, cellPoint.getX(), columnName, true, 1);
                    cellPoint.moveX(dataColumn.getWidth());
                }
            }
            cellPoint.moveY(1);
        }
        return cellPoint;
    }

    private static <T> CellPoint addTitle(
            Sheet sheet,
            CellStyle titleStyle,
            CellPoint cellPoint,
            List<T> dataList,
            boolean withIndex,
            String title) {
        int columnCount = dataList.size() - (withIndex ? 0 : 1); // 最大列下标（从0开始，比实际size小1）
        if (title != null) {
            // 在sheet中添加标总标题
            Row rowTitle = sheet.createRow(cellPoint.getY());
            Cell cellTitle = rowTitle.createCell(cellPoint.getX());// cell列 从0开始 第一列添加序号
            cellTitle.setCellType(CellType.STRING);
            cellTitle.setCellValue(title);
            cellTitle.setCellStyle(titleStyle);
            // 在sheet中合并标总标题
            if (columnCount > 0) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(cellPoint.getY(), cellPoint.getY(), cellPoint.getOriginY(), columnCount);
                sheet.addMergedRegion(cellRangeAddress);
                setRegionStyle(sheet, cellRangeAddress, titleStyle);
            }
            cellPoint.moveXY(1);
        }
        return cellPoint;
    }

    //headerCellLists中，HeaderCell有无x,y决定使用顺序插入还是定点插入,顺序插入总是以上一个cell的右边一格插入
    @SafeVarargs
    private static CellPoint addExtraHeader(Sheet sheet, CellStyle headerStyle, CellPoint cellPoint, List<SimpleCell>... headerCellLists) {
        if (headerCellLists != null && headerCellLists.length > 0) {
            cellPoint.setXToOrigin();
            int startRowIndex = cellPoint.getY();
            for (List<SimpleCell> headerCellList : headerCellLists) {
                for (SimpleCell headerCell : headerCellList) {
                    String value = headerCell.getValue();
                    Integer x = headerCell.getX();                                                              //单元格列号下标
                    Integer y = headerCell.getY() == null ? null : (headerCell.getY() + startRowIndex);         //单元格行号下标
                    Integer w = headerCell.getW();                                                          //单元格宽度
                    Integer h = headerCell.getH();                                                         //单元格高度
                    if (x == null || y == null) {//无x或y时，顺序插入
                        x = cellPoint.getX();
                        y = cellPoint.getY();
                    }
                    //写入单元格
                    Row row = sheet.getRow(y) == null ? sheet.createRow(y) : sheet.getRow(y);
                    Cell cell = row.createCell(x);
                    cell.setCellValue(value);
                    if (w > 1 || h > 1) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(y, y + h - 1, x, x + w - 1);
                        sheet.addMergedRegion(cellRangeAddress);
                        setRegionStyle(sheet, cellRangeAddress, headerStyle);
                    } else {
                        cell.setCellStyle(headerStyle);
                    }
                    setColumnWidth(sheet, x, value, true, w);
                    cellPoint.setX(x + w);
                    cellPoint.setY(y);
                }
                cellPoint.moveY(1);
            }
        }
        return cellPoint;
    }


    private static Sheet buildSheet(Workbook wb, int sheetIndex) {
        //创建工作簿
        while (sheetIndex >= wb.getNumberOfSheets() - 1) {
            wb.createSheet();
        }
        return wb.getSheetAt(sheetIndex);
    }

    @SafeVarargs
    public static <T> Workbook buildWorkBook(
            WorkBookVersion workBookVersion,
            String title,
            Map<String, String> columnMap,
            Boolean showHeaderColumn,
            List<T> dataList,
            Boolean withIndex,
            List<SimpleCell>... extraHeaderCell) throws Exception {
        Workbook wb;
        switch (workBookVersion) {
            case Excel97_2003:
                wb = new HSSFWorkbook();
                break;
            case Excel2007:
                wb = new XSSFWorkbook();
                break;
            default:
                throw new Exception("invalid workBookVersion");
        }
        wb.createSheet();
        List<DataColumn> dataColumns = new ArrayList<>();
        for (
                String key : columnMap.keySet()) {
            dataColumns.add(new DataColumn(key, columnMap.get(key)));
        }
        addRows(wb, 5, 2, 2, title, dataColumns, showHeaderColumn, dataList, withIndex, extraHeaderCell);
        return wb;
    }

    @SafeVarargs
    public static <T> void export(WorkBookVersion workBookVersion,
                                  HttpServletResponse response,
                                  String fileName,
                                  String title,
                                  Map<String, String> columnMap,
                                  Boolean showHeaderColumn,
                                  List<T> dataList,
                                  Boolean withIndex,
                                  List<SimpleCell>... extraHeaderCell) throws Exception {
        String fullFileName = fileName + workBookVersion.getFileType();
        Workbook wb = buildWorkBook(workBookVersion, title, columnMap, showHeaderColumn, dataList, withIndex, extraHeaderCell);
        responseOut(response, wb, fullFileName);
    }

    /**
     * 表格导出工具，导出List< Model>数据源的规律列表
     *
     * @param wb               Workbook对象
     * @param title            表第一行总标题 如："表格标题"；若为null忽略总标题行【字符串】
     * @param showHeaderColumn 是否插入表头行。（简单表格可为true；复杂表头为false，使用extraHeaderCell做表头）【一般为true】
     * @param dataList         数据集合，需与表头数组中的字段名一致，并且符合javabean规范【自行查询数据List< Model>】
     * @param withIndex        是否插入序号列【一般为true】
     * @param extraHeaderCell  在列名行前的额外列名行，专用于制作复杂表头。HeaderColumn不含x,y时，每个List为一行，HeaderColumn(内容,合并列数) ;含x,y时使用x,y定位插入【一般为null】
     * @return int 返回最后插入数据的行下标+1，调用此方法后，新建行时可直接使用返回的坐标值
     * @throws Exception 抛出异常
     */
    @SafeVarargs
    public static <T> CellPoint addRows(
            Workbook wb,
            String title,
            Map<String, String> columnMap,
            Boolean showHeaderColumn,
            List<T> dataList,
            Boolean withIndex,
            List<SimpleCell>... extraHeaderCell) throws Exception {
        List<DataColumn> dataColumns = new ArrayList<>();
        for (String key : columnMap.keySet()) {
            dataColumns.add(new DataColumn(key, columnMap.get(key)));
        }
        return addRows(wb, 0, 0, 0, title, dataColumns, showHeaderColumn, dataList, withIndex, extraHeaderCell);
    }

    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cs);
            }
        }
    }

    public static void setColumnWidth(Sheet sheet, int columnNum, Object value, boolean useMaxWidth, int regionNum) {
        regionNum = regionNum < 1 ? 1 : regionNum;
        if (value == null) {
            return;
        }
        int codeCount = 0;
        for (int i = 0; i < value.toString().length(); i++) {
            String tempStr = value.toString().substring(i, i + 1);
            char litter = tempStr.toCharArray()[0];
            if (litter > 'A' && litter <= 'Z') {
                codeCount += 2;
            } else {
                codeCount += Math.min(tempStr.getBytes().length, 2);
            }
        }
        int sheetWidth;
        int fontWidth = 300;//单个小写英文字母所占宽度标准为256
        if (useMaxWidth) {
            sheetWidth = Math.max(sheet.getColumnWidth(columnNum), codeCount * fontWidth);
        } else {
            sheetWidth = codeCount * fontWidth;
        }
        for (int i = 0; i < regionNum; i++) {//regionNum横向格子数
            sheet.setColumnWidth(columnNum + i, Math.max(sheetWidth, 1024) / regionNum);//平均分配合并行的宽度
        }
    }
}