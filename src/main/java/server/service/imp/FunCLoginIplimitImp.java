package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunCLoginIplimitMapper;
import server.db.primary.model.sysoption.CLoginIplimit;
import server.service.FunCLoginIplimitService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunCLoginIplimitImp implements FunCLoginIplimitService {
    @Autowired
    FunCLoginIplimitMapper funCLoginIplimitMapper;

    @Override
    public List<CLoginIplimit> selectIplimitByUserId(Long userId) {
        Example example = new Example(CLoginIplimit.class);
        example.selectProperties("id", "ipAddr");
        example.createCriteria().andEqualTo("cLoginId", userId);
        return funCLoginIplimitMapper.selectByExample(example);
    }

    @Override
    public boolean createCLoginIplimit(CLoginIplimit cLoginIplimit) {
        cLoginIplimit.setId(null);
        return funCLoginIplimitMapper.insertSelective(cLoginIplimit) > 0;
    }

    @Override
    public boolean deleteCLoginIplimitByIdList(List<Long> idList) {
        Example example = new Example(CLoginIplimit.class);
        example.createCriteria()
                .andIn("id", idList);
        return funCLoginIplimitMapper.deleteByExample(example) > 0;
    }

}

