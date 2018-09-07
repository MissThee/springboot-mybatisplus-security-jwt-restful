package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunAuthRoleMapper;
import server.db.primary.model.sysoption.AuthGroup;
import server.db.primary.model.sysoption.AuthRole;
import server.service.FunAuthRoleService;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class FunAuthRoleImp implements FunAuthRoleService {
    @Autowired
    FunAuthRoleMapper funAuthRoleMapper;

    @Override
    public List<AuthRole> selectRole() {
        Example example = new Example(AuthRole.class);
        example.orderBy("roleMark");
        return funAuthRoleMapper.selectByExample(example);
    }

    @Override
    public AuthRole selectRoleById(Long id) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("id", id);
        example.excludeProperties("stime", "rtime");
        return funAuthRoleMapper.selectOneByExample(example);
    }

    @Override
    public List<AuthRole> selectRoleForList() {
        Example example = new Example(AuthRole.class);
        example.selectProperties("id", "roleName", "roleNo");
        example.orderBy("roleNo");
        return funAuthRoleMapper.selectByExample(example);
    }

    @Override
    public boolean isRoleExist(String roleNo) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("roleNo", roleNo);
        return funAuthRoleMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean isNameExist(String roleName) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("roleName", roleName);
        return funAuthRoleMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createRole(AuthRole authRole) {
        return funAuthRoleMapper.insertSelective(authRole) > 0;
    }

    @Override
    public boolean isRoleExistExceptSelf(Long id, String roleNo) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("roleNo", roleNo)
                .andNotEqualTo("id", id);
        return funAuthRoleMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean isNameExistExceptSelf(Long id, String roleName) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("roleName", roleName)
                .andNotEqualTo("id", id);
        return funAuthRoleMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateRole(AuthRole authRole) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andEqualTo("id", authRole.getId());
        return funAuthRoleMapper.updateByExampleSelective(authRole, example) > 0;
    }

    @Override
    public boolean deleteRoleByIdList(List<Long> idList) {
        Example example = new Example(AuthRole.class);
        example.createCriteria()
                .andIn("id", idList);
        return funAuthRoleMapper.deleteByExample(example) > 0;
    }
}
