package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class User {
    private Integer id;

    private String name;

    private String username;

    private String password;

    private List<Role> roleList;

}