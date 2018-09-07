package server.db.primary.mapper.sysoption;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.sysoption.FactoryInfo;

import java.util.List;

@Component
public interface FunFactoryInfoMapper extends CommonMapper<FactoryInfo> {
    List<FactoryInfo> selecFactoryAreaTree();

    List<FactoryInfo> selectUnitTree();
}