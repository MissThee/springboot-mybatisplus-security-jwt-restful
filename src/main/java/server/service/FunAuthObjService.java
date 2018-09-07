package server.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import server.db.primary.model.sysoption.AuthObj;
import server.db.primary.model.sysoption.FactoryInfo;

import java.util.List;

public interface FunAuthObjService {
    List<AuthObj> selectObj();

    AuthObj selectObjById(Long id);

    List<AuthObj> selectObjForList();

    boolean isObjExist(String objNo);

    boolean isNameExist(String objName);

    boolean createObj(AuthObj authObj, List<Long> areaIdList);

    boolean isObjExistExceptSelf(Long id, String objNo);

    boolean isNameExistExceptSelf(Long id, String objName);

    boolean updateObj(AuthObj authObj, List<Long> areaIdList);

    boolean deleteObjByIdList(List<Long> idList);

    JSONArray getLogoList();


}
