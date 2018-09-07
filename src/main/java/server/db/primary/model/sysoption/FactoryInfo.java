package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.List;

@Data
public class FactoryInfo {
    private Long id;

    private String name;

    private String a11CodeFather;

    private List<AreaInfo> areaInfoList;
}