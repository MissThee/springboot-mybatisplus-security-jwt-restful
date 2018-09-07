package server.db.primary.mapper.sysoption;

import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.CLogin;

import java.util.List;

@Component
public interface FunCLoginMapper extends CommonMapper<CLogin> {
    CLogin selectUserByUsername(String username);

    CLogin selectUserById(Long id);

    List<CLogin> selectUserTable();

    CLogin selectUserOneById(Long id);
}