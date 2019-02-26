package server.tool.excel.exports.direct;

import lombok.Getter;

@Getter
public class DataColumn {
    private String modelPropertyName;           //属性名
    private String headerName;                  //表头名
    private Integer width;                      //单元格宽度
    private Boolean isEmptyData = false;        //不插入数据标记
    private Boolean isDBColumn = true;          //数据库中此字段有映射字段（用于配合tkmapper筛选需查询的字段使用）

    public DataColumn(String modelPropertyName, String headerName, int width) {
        this.modelPropertyName = modelPropertyName;
        this.headerName = headerName;
        this.width = Math.max(width, 1);
    }

    public DataColumn(String modelPropertyName, String headerName) {
        this.modelPropertyName = modelPropertyName;
        this.headerName = headerName;
        this.width = 1;
    }

    //此列是否写入数据。表格生成方法中，使用此值判断是否插入此列数据（当需要显示此列表头，但不插入数据时使用）
    public DataColumn setEmptyData() {
        this.isEmptyData = true;
        return this;
    }

    //配合通用mapper。可使用此值判断是否将此字段写入查询语句中（表格中不需要出现此列，但须查询出数据来做计算等其他用途时使用）
    public DataColumn setNoDataBaseColumn() {
        this.isDBColumn = false;
        return this;
    }
}
