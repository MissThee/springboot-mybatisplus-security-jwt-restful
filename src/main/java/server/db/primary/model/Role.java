package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Role {
    private Integer id;

    private String name;

    private String role;

    private List<Permission> permissionList;
}