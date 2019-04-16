package com.github.missthee.db.dto.login;

import com.github.missthee.db.entity.primary.manage.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoginDTO extends User {
    private Set<String> roleValueList;
    private Set<String> permissionValueList;
    private String unitName;
}