package server.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import javax.lang.model.type.NullType;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.jdbc.Null;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

//通用简单表格导出工具，调用本工具的Controller返回类型需为void
public class ExcelUtils {
    /**
     * @param wb               HSSFWorkbook对象
     * @param sheetIndex       表格下标，0开始(一般为0)
     * @param startRowIndex    插入行号，0开始（一般为0）
     * @param title            表第一行总标题 如："表格标题"；若为null忽略总标题行
     * @param dataColumns      列名行。(字段名，显示列名)  对应map,如：put("实体类属性名","列名"),若put("实体类属性名","")，则生成表时不生成此列
     * @param showHeaderColumn 是否插入列名行（简单表格可为true；复杂表头为false，使用extraHeaderCell做表头）
     * @param dataList         数据集合，需与表头数组中的字段名一致，并且符合javabean规范
     * @param withIndex        是否插入序号列
     * @param extraHeaderCell  在列名行前的额外列名行，专用于制作复杂表头。HeaderColumn不含x,y时，每个List为一行，HeaderColumn(内容,合并列数) ;含x,y时使用x,y定位插入
     * @return int 返回最后插入数据的行下标+1，调用此方法后，新建行时可直接使用返回的值
     * @throws Exception 抛出异常
     */

    @SafeVarargs
    public static <T> LastRowColumnNum addRowsByData(
            HSSFWorkbook wb,
            int sheetIndex,
            int startRowIndex,
            int startColumnIndex,
            String title,
            List<DataColumn> dataColumns,
            Boolean showHeaderColumn,
            List<T> dataList,
            Boolean withIndex,
            List<HeaderCell>... extraHeaderCell) throws Exception {
        HSSFSheet sheet;
        sheet = wb.getNumberOfSheets() > 0 ? wb.getSheetAt(sheetIndex) : wb.createSheet();

        List<DataColumn> dataColumnList = new ArrayList<>();
        //删去列对象中，值为""或null的对象
        dataColumns.iterator();
        for (DataColumn dataColumn : dataColumns) {
            if (!StringUtils.isEmpty(dataColumn.getHeaderName())) {
                dataColumnList.add(dataColumn);
            }
        }

        int rowNum = startRowIndex;   // 行下标从0开始
        int columnNum = startColumnIndex;
        int columnCount = dataColumnList.size() - (withIndex ? 0 : 1); // 最大列下标（从0开始，比实际size小1）
        if (title != null) {
            // 在sheet中合并标总标题
            if (columnCount > 0) {
                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, columnCount));
            }
            // 在sheet中添加标总标题
            HSSFRow rowTitle = sheet.createRow(rowNum);
            HSSFCell cellTitle = rowTitle.createCell(0);// cell列 从0开始 第一列添加序号
            cellTitle.setCellValue(title);
            cellTitle.setCellStyle(titleStyle(wb));
            rowNum++;
        }
        if (extraHeaderCell != null && extraHeaderCell.length > 0) {
            LastRowColumnNum lastRowColumnNum = addExtraHeaderRowsByList(wb, sheetIndex, rowNum, columnNum, extraHeaderCell);
            rowNum = lastRowColumnNum.getRowNum();
            columnNum=lastRowColumnNum.getColumnNum();
        }
        if (showHeaderColumn) {
            HSSFRow rowHeader = sheet.createRow(rowNum);
            int columnIndex = startColumnIndex;
            if (withIndex) {
                // 第一列添加序号
                String columnName = "序号";
                HSSFCell sequenceCell = rowHeader.createCell(columnIndex);
                sequenceCell.setCellValue(columnName);
                sequenceCell.setCellStyle(headerStyle(wb));
                setColumnWidth(sheet, columnIndex, columnName, true, 1);
                columnIndex++;
            }
            // 为标题行赋值
            {
                for (DataColumn dataColumn : dataColumnList) {
                    String columnName = dataColumn.getHeaderName();
                    HSSFCell titleCell = rowHeader.createCell(columnIndex);
                    titleCell.setCellValue(columnName);
                    if (dataColumn.getWidth() > 1) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + dataColumn.getWidth() - 1);
                        sheet.addMergedRegion(cellRangeAddress);
                        setRegionStyle(sheet, cellRangeAddress, headerStyle(wb));
                    } else {
                        titleCell.setCellStyle(headerStyle(wb));
                    }
                    setColumnWidth(sheet, columnIndex, columnName, true, 1);
                    columnIndex = columnIndex + dataColumn.getWidth();
                    columnNum = columnIndex;
                }
            }
            rowNum++;
        }
        if (dataList != null) {
            // 遍历集合数据，产生数据行
            Iterator<T> it = dataList.iterator();
            int indexNumber = 1;//序号列自增数字
            while (it.hasNext()) {
                HSSFRow rowData = sheet.createRow(rowNum);
                int columnIndex = startColumnIndex;
                if (withIndex) {
                    //  序号列
                    HSSFCell sequenceCellValue = rowData.createCell(columnIndex);// 序号值永远是第0列
                    sequenceCellValue.setCellValue(indexNumber++);
                    sequenceCellValue.setCellStyle(dataStyle(wb));
                    columnIndex++;
                }
                T t = it.next();
                // 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
                {
                    for (DataColumn dataColumn : dataColumnList) {
                        HSSFCell dataCell = rowData.createCell(columnIndex);
                        HSSFCellStyle cellStyle = dataStyle(wb);
                        //写入值
                        if (!dataColumn.getIsEmptyData()) {
                            String getMethodName = "get" + dataColumn.getDataStr().substring(0, 1).toUpperCase() + dataColumn.getDataStr().substring(1);// 取得对应getXxx()方法
                            Class<?> tCls = t.getClass();// 泛型为Object以及所有Object的子类
                            Method getMethod = tCls.getMethod(getMethodName);// 通过方法名得到对应的方法
                            Object value = getMethod.invoke(t);// 动态调用方,得到属性值
                            if (value != null) {
                                if (value instanceof Float || value instanceof Double) {
//                                    value = String.format("%.2f", (Double) value);
                                    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                                    double newValue = new BigDecimal(value.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    dataCell.setCellValue(newValue);
                                    setColumnWidth(sheet, columnIndex, newValue, true, 1);
                                } else if (value instanceof Long || value instanceof Integer) {
                                    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                                    long newValue = (long) value;
                                    dataCell.setCellValue(newValue);
                                    setColumnWidth(sheet, columnIndex, newValue, true, 1);
                                } else {
                                    dataCell.setCellValue(value.toString());// 为当前列赋值
                                    setColumnWidth(sheet, columnIndex, value.toString(), true, 1);
                                }
                            }
                        }
                        if (dataColumn.getWidth() > 1) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + dataColumn.getWidth() - 1);
                            sheet.addMergedRegion(cellRangeAddress);
                            setRegionStyle(sheet, cellRangeAddress, cellStyle);
                        } else {
                            dataCell.setCellStyle(cellStyle);
                        }
                        columnIndex = columnIndex + dataColumn.getWidth();
                        columnNum = columnIndex;
                    }
                }
                rowNum++;
                columnNum++;

            }
        }
        return new LastRowColumnNum(rowNum, columnNum);
    }

    @SafeVarargs
    public static <T> LastRowColumnNum addRowsByData(
            HSSFWorkbook wb,
            int sheetIndex,
            int startRowIndex,
            int startColumnIndex,
            String title,
            Map<String, String> columnMap,
            Boolean showHeaderColumn,
            List<T> dataList,
            Boolean withIndex,
            List<HeaderCell>... extraHeaderCell) throws Exception {
        List<DataColumn> dataColumns = new ArrayList<>();
        for (String key : columnMap.keySet()) {
            dataColumns.add(new DataColumn(key, columnMap.get(key)));
        }
        return addRowsByData(wb, sheetIndex, startRowIndex, startColumnIndex, title, dataColumns, showHeaderColumn, dataList, withIndex, extraHeaderCell);
    }

    //headerCellLists中，第一个HeaderCell有五x,y决定使用顺序插入还是定点插入
    @SafeVarargs
    private static LastRowColumnNum addExtraHeaderRowsByList(HSSFWorkbook wb, int sheetIndex, int startRowIndex, int startColumnIndex, List<HeaderCell>... headerCellLists) {
        int insertType = 0;//插入方式：1-定点；2-顺序
        int rowsNum = 0;//有x,y时使用
        int rowsColumn = 0;//有x,y时使用
        int rowIndex = startRowIndex, columnIndex = startColumnIndex;//无x,y时使用 定点插入
        for (List<HeaderCell> headerCellList : headerCellLists) {
            for (HeaderCell headerCell : headerCellList) {
                String value = headerCell.getValue();
                Integer x = headerCell.getX();
                Integer y = headerCell.getY() == null ? null : (headerCell.getY() + startRowIndex);
                Integer w = headerCell.getWidth();
                Integer h = headerCell.getHeight();
                if (insertType == 0) {
                    if (x != null && y != null) {//有x,y时
                        insertType = 1;//定点插入
                    } else {
                        insertType = 2;//顺序插入
                    }
                }
                if (insertType == 2) {
                    x = columnIndex;
                    y = rowIndex;
                    columnIndex = x + w;
                }
                rowsNum = Math.max(y + h, rowsNum);
                rowsColumn = Math.max(x + w, rowsColumn);
                //写入单元格
                HSSFSheet sheet = wb.getSheetAt(sheetIndex);
                HSSFRow row = sheet.getRow(y) == null ? sheet.createRow(y) : sheet.getRow(y);
                HSSFCell cell = row.createCell(x);
                cell.setCellValue(value);
                if (w > 1 || h > 1) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(y, y + h - 1, x, x + w - 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    setRegionStyle(sheet, cellRangeAddress, headerStyle(wb));
                } else {
                    cell.setCellStyle(headerStyle(wb));
                }
                setColumnWidth(sheet, x, value, true, w);
            }
            rowIndex++;
        }
        return new LastRowColumnNum(rowsNum, rowsColumn);
    }

    public static void responseOut(HttpServletResponse response, HSSFWorkbook wb, String fileName) throws IOException {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        OutputStream outputStream = response.getOutputStream(); // 打开流
        wb.write(outputStream);// HSSFWorkbook写入流
        wb.close();// HSSFWorkbook关闭
        outputStream.flush();// 刷新流
        outputStream.close();// 关闭流
    }

    public static HSSFCellStyle titleStyle(HSSFWorkbook wb) {
        // 设置标题样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置单元格边框样式
        style.setBorderTop(BorderStyle.THIN);// 上边框 细边线
        style.setBorderBottom(BorderStyle.THIN);// 下边框 细边线
        style.setBorderLeft(BorderStyle.THIN);// 左边框 细边线
        style.setBorderRight(BorderStyle.THIN);// 右边框 细边线
        // 设置单元格对齐方式
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        // 设置字体样式
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 12); // 字体高度
        titleFont.setFontName("ARIAL"); // 字体样式
        titleFont.setBold(true);
        style.setFont(titleFont);
        return style;
    }

    public static HSSFCellStyle headerStyle(HSSFWorkbook wb) {
        // 设置列名样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置单元格边框样式
        style.setBorderTop(BorderStyle.THIN);// 上边框 细边线
        style.setBorderBottom(BorderStyle.THIN);// 下边框 细边线
        style.setBorderLeft(BorderStyle.THIN);// 左边框 细边线
        style.setBorderRight(BorderStyle.THIN);// 右边框 细边线
        // 设置单元格对齐方式
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中

        // 设置字体样式
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 10); // 字体高度
        titleFont.setFontName("ARIAL"); // 字体样式
        titleFont.setBold(true);
        style.setFont(titleFont);
        return style;
    }

    public static HSSFCellStyle dataStyle(HSSFWorkbook wb) {
        // 数据样式 （因为标题和数据样式不同 需要分开设置 不然会覆盖）
        HSSFCellStyle style = wb.createCellStyle();
        // 设置数据边框
        style.setBorderTop(BorderStyle.THIN);// 上边框 细边线
        style.setBorderBottom(BorderStyle.THIN);// 下边框 细边线
        style.setBorderLeft(BorderStyle.THIN);// 左边框 细边线
        style.setBorderRight(BorderStyle.THIN);// 右边框 细边线
        // 设置居中样式
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        // 设置数据字体
        Font dataFont = wb.createFont();
        dataFont.setFontHeightInPoints((short) 10); // 字体高度
        dataFont.setFontName("ARIAL"); // 字体
        style.setFont(dataFont);
        return style;
    }

    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cs);
            }
        }
    }

    public static void setColumnWidth(HSSFSheet sheet, int columnNum, Object value, boolean useMaxWidth, int regionNum) {
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

    @SafeVarargs
    public static <T> void export(HttpServletResponse response,
                                  String fileName,
                                  String title,
                                  Map<String, String> columnMap,
                                  Boolean showHeaderColumn,
                                  List<T> dataList,
                                  Boolean withIndex,
                                  List<HeaderCell>... extraHeaderCell) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet();
        List<DataColumn> dataColumns = new ArrayList<>();
        for (String key : columnMap.keySet()) {
            dataColumns.add(new DataColumn(key, columnMap.get(key)));
        }
        addRowsByData(wb, 0, 0, 0, title, dataColumns, showHeaderColumn, dataList, withIndex, extraHeaderCell);
        responseOut(response, wb, fileName);
    }

    @Getter
    public static class HeaderCell {
        private String value;
        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;

        public HeaderCell(String cellValue, int width) {
            this.value = cellValue;
            this.x = null;
            this.y = null;
            this.width = Math.max(width, 1);
            this.height = 1;
        }

        public HeaderCell(String cellValue, int x, int y, int width, int height) {
            this.value = cellValue;
            this.x = x;
            this.y = y;
            this.width = Math.max(width, 1);
            this.height = Math.max(height, 1);
        }
    }

    @Getter
    public static class DataColumn {
        private String dataStr;
        private String headerName;
        private Integer width;
        private Boolean isEmptyData = false;
        private Boolean isDBColumn = true;
        private String computeType = "";

        public DataColumn(String dataStr, String headerName, int width) {
            this.dataStr = dataStr;
            this.headerName = headerName;
            this.width = Math.max(width, 1);
        }

        public DataColumn(String dataStr, String headerName) {
            this.dataStr = dataStr;
            this.headerName = headerName;
            this.width = 1;
        }

        public DataColumn setNoDataBaseColumn() {
            this.isDBColumn = false;
            return this;
        }

        public DataColumn setEmptyData() {
            this.isEmptyData = true;
            return this;
        }

        public DataColumn setComputeType() {
            this.isEmptyData = true;
            return this;
        }
    }

    @Data
    @AllArgsConstructor
    public static class LastRowColumnNum {
        private Integer rowNum = 0;
        private Integer columnNum = 0;
    }
}