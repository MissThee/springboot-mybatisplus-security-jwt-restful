package server.tool.excel.exports.direct;

public enum WorkBookVersion {
    Excel97_2003, Excel2007;

    public String getFileType() {
        switch (this) {
            case Excel97_2003:
                return ".xls";
            case Excel2007:
                return ".xlsx";
            default:
                return "";
        }
    }
}
