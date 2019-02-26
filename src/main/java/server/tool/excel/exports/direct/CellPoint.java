package server.tool.excel.exports.direct;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CellPoint {

    private final int originY;  //初始rowNum
    private final int originX;  //初始columnNum
    private int y;          //rowNum
    private int x;          //columnNum

    public CellPoint(int y, int x) {
        this.originY = y;
        this.originX = x;
        this.y = y;
        this.x = x;
    }

    public CellPoint moveXY(int value) {
        this.y += value;
        this.x += value;
        return this;
    }

    public CellPoint moveY(int value) {
        this.y += value;
        return this;
    }

    public CellPoint moveX(int value) {
        this.x += value;
        return this;
    }

    public CellPoint setXToOrigin() {
        this.x = originX;
        return this;
    }

    public CellPoint setYToOrigin() {
        this.y = originY;
        return this;
    }
}
