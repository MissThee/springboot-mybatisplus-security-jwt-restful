package com.github.missthee.db.dto.login;

import com.github.missthee.db.entity.primary.manage.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoginDTO extends User {
    private Collection<String> roleValueList;
    private Collection<String> permissionValueList;
    private String unitName;
}