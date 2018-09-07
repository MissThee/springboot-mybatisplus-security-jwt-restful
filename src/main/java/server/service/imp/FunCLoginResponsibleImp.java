package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunCLoginResponsibleMapper;
import server.db.primary.model.sysoption.CLoginResponsible;
import server.service.FunCLoginResponsibleService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunCLoginResponsibleImp implements FunCLoginResponsibleService {
    @Autowired
    FunCLoginResponsibleMapper funCLoginResponsibleMapper;

    @Override
    public List<CLoginResponsible> selectResponsibleByUserId(Long userId) {
        Example example = new Example(CLoginResponsible.class);
        example.selectProperties("id", "name");
        example.createCriteria().andEqualTo("cLoginId", userId);
        return funCLoginResponsibleMapper.selectByExample(example);
    }

    @Override
    public boolean createCLoginResponsible(CLoginResponsible cLoginResponsible) {
        cLoginResponsible.setId(null);
        return funCLoginResponsibleMapper.insertSelective(cLoginResponsible) > 0;
    }

    @Override
    public boolean deleteCLoginResponsibleByIdList(List<Long> idList) {
        Example example = new Example(CLoginResponsible.class);
        example.createCriteria()
                .andIn("id", idList);
        return funCLoginResponsibleMapper.deleteByExample(example) > 0;
    }
}
