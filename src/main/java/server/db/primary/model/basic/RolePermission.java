package server.db.primary.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.CacheNamespace;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@CacheNamespace
@Entity
public class RolePermission implements Serializable {
    @Id
    private Integer id;
    private Integer roleId;
    private Integer permissionId;
}