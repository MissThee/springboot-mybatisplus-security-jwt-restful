//package server.controller.login;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import server.db.primary.model.sysoption.SysPermission;
//
//import java.util.List;
//
//public class MenuTree {
//    //li为查出来带id和parentId的数据，parentId为null的为根节点
//    private static JSONArray buildMenu(List<SysPermission> li, SysPermission objP) {
//        JSONArray nodeJA = new JSONArray();
//        for (SysPermission obj : li) {
//            if (objP == null ? (obj.getParentId() == null) : objP.getId().equals(obj.getParentId())) {  //null时为根节点，非null为子节点
//                if (!obj.getType().toUpperCase().equals("BUTTON")) {
//                    JSONObject childrenJO = new JSONObject();
//                    childrenJO.put("id", obj.getId());
//                    childrenJO.put("label", obj.getName());
//                    childrenJO.put("url", obj.getUrl());
//                    childrenJO.put("type", obj.getType());
////                    childrenJO.put("permission", obj.getPermission());
//                    childrenJO.put("children", buildMenu(li, obj));
//                    if (childrenJO.getJSONArray("children") == null || childrenJO.getJSONArray("children").size() == 0) {
//                        childrenJO.remove("children");
//                    }
//                    nodeJA.add(childrenJO);
//                }
//            }
//        }
//        return nodeJA;
//    }
//
//    public static JSONArray buildMenu(List<SysPermission> li) {
//        return buildMenu(li, null);
//    }
//
//}
