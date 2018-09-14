package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRole {
    private Integer userId;

    private Integer roleId;
}