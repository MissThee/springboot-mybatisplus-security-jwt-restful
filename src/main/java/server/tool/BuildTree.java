package server.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BuildTree {
    //输入包含id,name,parentId的List<T>对象，输出树形JSONArray数据
    private static <T> JSONArray buildTree(List<T> li, T objP) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONArray nodeJA = new JSONArray();
        for (T obj : li) {
            Object objParentIdValue = obj.getClass().getMethod("getParentId").invoke(obj);
            Object objIdValue = obj.getClass().getMethod("getId").invoke(obj);
            Object objNameValue = obj.getClass().getMethod("getName").invoke(obj);
            if (objP == null ? (objParentIdValue == null) : objP.getClass().getMethod("getId").invoke(objP).equals(objParentIdValue)) {  //objP为null&&objParentIdValue为null时，是根节点
                JSONObject childrenJO = new JSONObject() {{
                    put("id", objIdValue);
                    put("name", objNameValue);
                    put("children", buildTree(li, obj));
                }};

                if (childrenJO.getJSONArray("children") == null || childrenJO.getJSONArray("children").isEmpty()) {
                    childrenJO.remove("children");
                }
                nodeJA.add(childrenJO);
            }
        }
        return nodeJA;
    }

    public static <T> JSONArray buildTree(List<T> li) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return buildTree(li, null);
    }

}
