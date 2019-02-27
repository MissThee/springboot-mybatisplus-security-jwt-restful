package server.db.primary.dto.login;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class LoginDTO implements Serializable {
    private Integer id;
    private String nickname;
    private String username;
    private Set<String> roleValueList;
    private Set<String> permissionValueList;
}