package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunCLoginMapper;
import server.db.primary.model.sysoption.CLogin;
import server.service.FunCLoginService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunCloginImp implements FunCLoginService {
    @Autowired
    FunCLoginMapper funCLoginMapper;

    @Override
    public CLogin selectUserByUsername(String username) {
        return funCLoginMapper.selectUserByUsername(username);
    }

    @Override
    public CLogin selectUserById(Long id) {
        return funCLoginMapper.selectUserById(id);
    }

    @Override
    public List<CLogin> selectUser() {
        return funCLoginMapper.selectUserTable();
    }

    @Override
    public List<CLogin> selectUserForList() {
        Example example = new Example(CLogin.class);
        example.selectProperties("id", "cName");
        return funCLoginMapper.selectByExample(example);
    }

    @Override
    public boolean isUsernameExist(String cLoginname) {
        Example example = new Example(CLogin.class);
        example.createCriteria()
                .andEqualTo("cLoginname", cLoginname);
        return funCLoginMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createUser(CLogin clogin) {
        return funCLoginMapper.insertSelective(clogin) > 0;
    }

    @Override
    public boolean isUsernameExistExceptSelf(Long id, String cLoginname) {
        Example example = new Example(CLogin.class);
        example.createCriteria()
                .andEqualTo("cLoginname", cLoginname)
                .andNotEqualTo("id", id);
        return funCLoginMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateUser(CLogin cLogin) {
        Example example = new Example(CLogin.class);
        example.createCriteria()
                .andEqualTo("id", cLogin.getId());
        return funCLoginMapper.updateByExampleSelective(cLogin, example) > 0;
    }

    @Override
    public boolean deleteUserByIdList(List<Long> idList) {
        Example example = new Example(CLogin.class);
        example.createCriteria()
                .andIn("id", idList);
        return funCLoginMapper.deleteByExample(example) > 0;
    }

    @Override
    public CLogin selectUserOneById(Long id) {
        return funCLoginMapper.selectUserOneById(id);
    }
}
