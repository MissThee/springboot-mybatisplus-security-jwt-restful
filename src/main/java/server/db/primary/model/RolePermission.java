package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RolePermission {
    private Integer roleId;

    private Integer permissionId;


}