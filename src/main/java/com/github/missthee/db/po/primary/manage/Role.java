package com.github.missthee.db.po.primary.manage;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    //id
    @Id
    private Long id;

    //角色名
    private String name;

    //角色值
    private String role;

    //可用
    private Boolean isEnable;

    //已删除
    private Boolean isDelete;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NAME = "name";

    public static final String DB_NAME = "name";

    public static final String ROLE = "role";

    public static final String DB_ROLE = "role";

    public static final String IS_ENABLE = "isEnable";

    public static final String DB_IS_ENABLE = "is_enable";

    public static final String IS_DELETE = "isDelete";

    public static final String DB_IS_DELETE = "is_delete";
}