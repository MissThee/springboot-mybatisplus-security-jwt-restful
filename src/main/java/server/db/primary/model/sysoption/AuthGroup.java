package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
@Data
public class AuthGroup {
    private Long id;

    private String groupNo;

    private String groupName;

    private Long groupRoleId;

    private String groupRoleNo;

    private String groupRoleName;

    private Long groupObjId;

    private String groupObjNo;

    private String groupObjName;

    private String remark;

    private Long mark;

    private Date stime;

    private Date rtime;

    private AuthRole authRole;

    private AuthObj authObj;

}