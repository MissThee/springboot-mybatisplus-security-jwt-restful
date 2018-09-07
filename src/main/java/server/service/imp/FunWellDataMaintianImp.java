package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunWellDataMaintianMapper;
import server.db.primary.model.sysoption.WellDataMaintian;
import server.db.primary.model.sysoption.WellInfo;
import server.service.FunWellDataMaintianService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunWellDataMaintianImp implements FunWellDataMaintianService {
    @Autowired
    FunWellDataMaintianMapper funWellDataMaintianMapper;

    @Override
    public PageInfo selectWellPaged(Integer pageNum, Integer pageSize, List<Long> areaIds) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funWellDataMaintianMapper.selectWellDataByAreaIds(areaIds));
    }

    @Override
    public WellDataMaintian selectWellOneById(Long id) {
        Example example = new Example(WellDataMaintian.class);
        example.createCriteria()
                .andEqualTo("id", id);
        example.excludeProperties("wellId", "pressHoledown", "opId", "opName", "mark", "stime");
        return funWellDataMaintianMapper.selectOneByExample(example);
    }

    @Override
    public boolean isWellIdExist(Long wellId) {
        Example example = new Example(WellDataMaintian.class);
        example.createCriteria()
                .andEqualTo("wellId", wellId);
        return funWellDataMaintianMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createWellData(WellDataMaintian wellDataMaintian) {
        return funWellDataMaintianMapper.insertSelective(wellDataMaintian) > 0;
    }

    @Override
    public boolean isWellIdExistExceptSelf(Long id, Long wellId) {
        Example example = new Example(WellDataMaintian.class);
        example.createCriteria()
                .andEqualTo("wellId", wellId)
                .andNotEqualTo("id", id);
        return funWellDataMaintianMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateWellData(WellDataMaintian wellDataMaintian) {
        Example example = new Example(WellDataMaintian.class);
        example.createCriteria()
                .andEqualTo("id", wellDataMaintian.getId());
        return funWellDataMaintianMapper.updateByExampleSelective(wellDataMaintian, example) > 0;
    }

    @Override
    public boolean deleteWellByIdList(List<Long> idList) {
        Example example = new Example(WellInfo.class);
        example.createCriteria()
                .andIn("id", idList);
        return funWellDataMaintianMapper.deleteByExample(example) > 0;
    }


}
