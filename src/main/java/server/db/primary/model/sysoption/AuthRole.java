package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AuthRole {

    private Long id;

    private String roleNo;

    private String roleName;

    private String remark;

    private Long roleMark;

    private Long mark;

    private Date stime;

    private Date rtime;

    private List<AuthRolePermission> permissionList;

}