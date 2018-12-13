package server.dto.login;

import lombok.Data;
import server.db.primary.model.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class LoginDTO implements Serializable {
    private Integer id;
    private String name;
    private String username;
    private Set<String> roleValueList;
    private Set<String> permissionValueList;
}