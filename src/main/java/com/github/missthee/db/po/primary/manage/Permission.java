package com.github.missthee.db.po.primary.manage;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class Permission implements Serializable {
    @Id
    private Long id;

    private String name;

    private String permission;

    private Boolean isEnable;

    private Boolean isDelete;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NAME = "name";

    public static final String DB_NAME = "name";

    public static final String PERMISSION = "permission";

    public static final String DB_PERMISSION = "permission";

    public static final String IS_ENABLE = "isEnable";

    public static final String DB_IS_ENABLE = "is_enable";

    public static final String IS_DELETE = "isDelete";

    public static final String DB_IS_DELETE = "is_delete";
}