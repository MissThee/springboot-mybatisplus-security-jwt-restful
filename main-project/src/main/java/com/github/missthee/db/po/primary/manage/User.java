package com.github.missthee.db.po.primary.manage;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User   implements Serializable {
    //主键
    @Id
    private Long id;

    //昵称
    private String nickname;

    //用户名
    private String username;

    //密码
    private String password;

    //可用
    private Boolean isEnable;

    //已删除
    private Boolean isDelete;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NICKNAME = "nickname";

    public static final String DB_NICKNAME = "nickname";

    public static final String USERNAME = "username";

    public static final String DB_USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String DB_PASSWORD = "password";

    public static final String IS_ENABLE = "isEnable";

    public static final String DB_IS_ENABLE = "is_enable";

    public static final String IS_DELETE = "isDelete";

    public static final String DB_IS_DELETE = "is_delete";
}