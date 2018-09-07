package server.db.primary.mapper.sysoption;

import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.WellPipeInfo;
import server.db.primary.model.sysoption.WellPipeInfo_Group;

import java.util.List;

@Component
public interface FunWellPipeInfoMapper extends CommonMapper<WellPipeInfo> {

    List<WellPipeInfo_Group> selectPipeGroup();
}