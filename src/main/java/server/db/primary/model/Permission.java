package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Permission {
    private Integer id;

    private String name;

    private String permission;


}