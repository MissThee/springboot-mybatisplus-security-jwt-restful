package server.db.primary.model.basic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.CacheNamespace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    private String username;

    private String password;

    private String salt;

}