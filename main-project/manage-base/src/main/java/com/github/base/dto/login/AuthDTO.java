package com.github.base.dto.login;

import com.github.common.db.entity.primary.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AuthDTO extends SysUser {
    private Collection<String> roleValueList;
    private Collection<String> permissionValueList;
    private Long unitId;
    private String unitName;
}