package server.service.imp;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunAuthGroupMapper;
import server.db.primary.model.sysoption.AuthGroup;
import server.service.FunAuthGroupService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunAuthGroupImp implements FunAuthGroupService {
    @Autowired
    FunAuthGroupMapper funAuthGroupMapper;

    @Override
    public List<AuthGroup> selectGroup() {
        Example example = new Example(AuthGroup.class);
        return funAuthGroupMapper.selectByExample(example);
    }

    @Override
    public AuthGroup selectGroupById(Long id) {
        AuthGroup authGroup = new AuthGroup();
        authGroup.setId(id);
        return funAuthGroupMapper.selectOne(authGroup);
    }

    @Override
    public List<AuthGroup> selectGroupForList() {
        Example example = new Example(AuthGroup.class);
        example.selectProperties("id", "groupName", "groupNo");
        example.orderBy("groupNo");
        return funAuthGroupMapper.selectByExample(example);
    }

    @Override
    public boolean isGroupNameExist(String name) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andEqualTo("groupName", name);
        return funAuthGroupMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean isGroupNoExist(String groupNo) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andEqualTo("groupNo", groupNo);
        return funAuthGroupMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createGroup(AuthGroup authGroup) {
        return funAuthGroupMapper.insertSelective(authGroup) > 0;
    }

    @Override
    public boolean isGroupNoExistExceptSelf(Long id, String groupNo) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andEqualTo("groupNo", groupNo)
                .andNotEqualTo("id", id);
        return funAuthGroupMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean isGroupNameExistExceptSelf(Long id, String name) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andEqualTo("groupName", name)
                .andNotEqualTo("id", id);
        return funAuthGroupMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateGroup(AuthGroup authGroup) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andEqualTo("id", authGroup.getId());
        return funAuthGroupMapper.updateByExampleSelective(authGroup, example) > 0;
    }

    @Override
    public boolean deleteGroupByIdList(List<Long> idList) {
        Example example = new Example(AuthGroup.class);
        example.createCriteria()
                .andIn("id", idList);
        return funAuthGroupMapper.deleteByExample(example) > 0;
    }

    @Override
    public List<AuthGroup> selectGroupTable() {
        return funAuthGroupMapper.selectGroupTable();
    }

    @Override
    public AuthGroup selectGroupTableOneById(Long id) {
        return funAuthGroupMapper.selectGroupOneById(id);
    }


}
