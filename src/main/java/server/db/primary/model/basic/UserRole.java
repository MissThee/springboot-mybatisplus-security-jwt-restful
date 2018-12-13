package server.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.CacheNamespace;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
public class UserRole implements Serializable {
    @Id
    private Integer id;
    private Integer userId;
    private Integer roleId;
}