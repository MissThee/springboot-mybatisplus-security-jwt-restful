package com.github.missthee.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RolePermission implements Serializable {
    @Id
    private Integer id;
    private Integer roleId;
    private Integer permissionId;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String ROLE_ID = "roleId";

    public static final String DB_ROLE_ID = "role_id";

    public static final String PERMISSION_ID = "permissionId";

    public static final String DB_PERMISSION_ID = "permission_id";
}