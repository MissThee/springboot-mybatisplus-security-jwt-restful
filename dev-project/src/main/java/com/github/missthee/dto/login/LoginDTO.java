package com.github.missthee.dto.login;

import com.github.missthee.db.entity.primary.manage.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO extends User implements Serializable {
    private Set<String> roleValueList;
    private Set<String> permissionValueList;
}