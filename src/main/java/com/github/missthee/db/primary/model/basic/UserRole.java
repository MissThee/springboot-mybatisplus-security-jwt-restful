package server.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.CacheNamespace;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserRole implements Serializable {
    @Id
    private Integer id;
    private Integer userId;
    private Integer roleId;
    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String USER_ID = "userId";

    public static final String DB_USER_ID = "user_id";

    public static final String ROLE_ID = "roleId";

    public static final String DB_ROLE_ID = "role_id";
}