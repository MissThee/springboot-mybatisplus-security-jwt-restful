package server.service;

import com.alibaba.fastjson.JSONArray;
import server.db.primary.model.sysoption.FactoryInfo;

import java.util.List;

public interface FunFactoryInfoService {
    JSONArray getUnitTree();

    List<FactoryInfo> selectFactoryForList(String... extraColumn);

    FactoryInfo selectFactoryById(Long id);
}
