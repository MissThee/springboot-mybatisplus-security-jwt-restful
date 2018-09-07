package server.db.primary.model.sysoption;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AreaInfo_Table extends AreaInfo {
    private int meteringStationCount;
    private int chargeWellCount;
    private int onlineWellCount;
}
