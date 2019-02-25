package server.tool.excel.exports.direct;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LastRowColumnNum {
    private Integer rowNum = 0;
    private Integer columnNum = 0;
}
