package server.service.imp.compute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.compute.ComputeMapper;
import server.db.primary.model.compute.Compute;
import server.service.interf.compute.ComputeService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ComputeImp implements ComputeService {
    private final ComputeMapper computeMapper;

    @Autowired
    public ComputeImp(ComputeMapper computeMapper) {
        this.computeMapper = computeMapper;
    }


    @Override
    public List<Compute> selectGroupBy() {
        Example example = new Example(Compute.class);
        example.selectProperties(Compute.COLUMN1,Compute.COLUMN2);
        example.orderBy(Compute.ID);
        return computeMapper.selectAvgListByExample(example);
    }
}
