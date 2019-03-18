package com.github.missthee.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Permission  implements Serializable {
//    权限的type，parentId，isEnable，value，note等属性暂未添加
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String permission;
    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NAME = "name";

    public static final String DB_NAME = "name";

    public static final String PERMISSION = "permission";

    public static final String DB_PERMISSION = "permission";

    private static final long serialVersionUID = 1L;
}