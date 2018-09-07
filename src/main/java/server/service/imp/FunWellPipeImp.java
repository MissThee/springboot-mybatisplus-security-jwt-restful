package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunWellPipeInfoMapper;
import server.db.primary.model.sysoption.WellPipeInfo_Group;
import server.service.FunWellPipeService;

import java.util.List;

@Service
public class FunWellPipeImp implements FunWellPipeService {
    @Autowired
    FunWellPipeInfoMapper funWellPipeInfoMapper;
//
//    @Override
//    public List<WellPipeInfo> selectPipe() {
//        Example example=new Example(WellPipeInfo.class);
//        example.selectProperties("id","pipeNode","pipeNodeNum","pipeNodeSort","pipeLong","pipeLati");
//        return funWellPipeInfoMapper.selectByExample(example);
//    }

    @Override
    public List<WellPipeInfo_Group> selectPipeGroup() {
        return funWellPipeInfoMapper.selectPipeGroup();
    }
}
