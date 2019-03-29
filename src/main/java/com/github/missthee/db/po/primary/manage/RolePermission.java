package com.github.missthee.db.po.primary.manage;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "role_permission")
@Data
@Accessors(chain = true)
public class RolePermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String ROLE_ID = "roleId";

    public static final String DB_ROLE_ID = "role_id";

    public static final String PERMISSION_ID = "permissionId";

    public static final String DB_PERMISSION_ID = "permission_id";
}