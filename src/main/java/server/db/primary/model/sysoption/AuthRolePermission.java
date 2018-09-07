package server.db.primary.model.sysoption;

import lombok.Data;

@Data
public class AuthRolePermission {
    private Long roleId;

    private String permissionId;
}