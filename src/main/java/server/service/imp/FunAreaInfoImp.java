package server.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunAreaInfoMapper;
import server.db.primary.model.sysoption.AreaInfo;
import server.service.FunAreaInfoService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunAreaInfoImp implements FunAreaInfoService {

    @Autowired
    FunAreaInfoMapper funAreaInfoMapper;

    @Override
    public PageInfo selectAreaPaged(Integer pageNum, Integer pageSize ) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(funAreaInfoMapper.selectAreaInfoTable( ));
    }

    @Override
    public AreaInfo selectAreaOneById(Long id) {
        return funAreaInfoMapper.selectAreaInfoTableOneById(id);
    }

    @Override
    public List<AreaInfo> selectAreaForList(Long factoryId, List<Long> areaIds, String... extraColumn) {
        Example example = new Example(AreaInfo.class);
//        example.selectProperties("id", "areaName");
        example.orderBy("areaName");
        Example.Criteria criteria = example.createCriteria();
        if (factoryId != null) {
            criteria.andEqualTo("coId", factoryId);
        }

        criteria.andIn("id", areaIds);
        List<String> columnList = new ArrayList<>();
        for (String column : extraColumn) {
            columnList.add(column);
        }
        columnList.add("id");
        columnList.add("areaName");
        example.selectProperties(columnList.toArray(new String[]{}));
        return funAreaInfoMapper.selectByExample(example);
    }

    @Override
    public boolean isAreaNameExist(String areaName) {
        Example example = new Example(AreaInfo.class);
        example.createCriteria()
                .andEqualTo("areaName", areaName);
        return funAreaInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean createArea(AreaInfo areaInfo) {
        return funAreaInfoMapper.insertSelective(areaInfo) > 0;
    }

    @Override
    public boolean isAreaNameExistExceptSelf(Long id, String areaName) {
        Example example = new Example(AreaInfo.class);
        example.createCriteria()
                .andEqualTo("areaName", areaName)
                .andNotEqualTo("id", id);
        return funAreaInfoMapper.selectByExample(example).size() > 0;
    }

    @Override
    public boolean updateArea(AreaInfo areaInfo) {
        Example example = new Example(AreaInfo.class);
        example.createCriteria()
                .andEqualTo("id", areaInfo.getId());
        return funAreaInfoMapper.updateByExampleSelective(areaInfo, example) > 0;
    }

    @Override
    public boolean deleteAreaByIdList(List<Long> idList) {
        Example example = new Example(AreaInfo.class);
        example.createCriteria()
                .andIn("id", idList);
        return funAreaInfoMapper.deleteByExample(example) > 0;
    }

    @Override
    public AreaInfo selectAreaById(Long id) {
        Example example = new Example(AreaInfo.class);
        example.createCriteria()
                .andEqualTo("id", id);
        return funAreaInfoMapper.selectOneByExample(example);
    }
}
