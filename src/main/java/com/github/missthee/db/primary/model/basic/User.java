package com.github.missthee.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    private String username;

    private String password;

    private String salt;
    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NICKNAME = "nickname";

    public static final String DB_NICKNAME = "nickname";

    public static final String USERNAME = "username";

    public static final String DB_USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String DB_PASSWORD = "password";

    public static final String SALT = "salt";

    public static final String DB_SALT = "salt";
}