package server.tool.excel;

import lombok.Getter;

@Getter
public class SimpleCell {
    private String value;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;

    public SimpleCell(String cellValue, int w) {
        this.value = cellValue;
        this.x = null;
        this.y = null;
        this.w = Math.max(w, 1);
        this.h = 1;
    }

    public SimpleCell(String cellValue, int w, int h) {
        this.value = cellValue;
        this.x = null;
        this.y = null;
        this.w = Math.max(w, 1);
        this.h = Math.max(h, 1);
    }

    public SimpleCell(String cellValue, int x, int y, int w, int h) {
        this.value = cellValue;
        this.x = x;
        this.y = y;
        this.w = Math.max(w, 1);
        this.h = Math.max(h, 1);
    }
}