package server.db.primary.model.sysoption;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
public class CLogin {
    private Long id;

    private String cLoginname;

    private String cLoginpwd;

    private String cName;

    private Long cGid;

    private String cGno;

    private String cGname;

    private String remark;

    private Long ipLimitMark;

    private Long mark;

    private Date stime;

    private Date rtime;

    private Long urlType;

    private String urlFile;

    private AuthGroup authGroup;

    @Transient
    private List<String> permissionIdList;
}