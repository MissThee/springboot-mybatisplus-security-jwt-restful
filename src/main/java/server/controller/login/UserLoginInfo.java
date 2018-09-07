//package server.controller.login;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import server.db.primary.model.sysoption.CLogin;
//import server.db.primary.model.sysoption.SysGroup;
//import server.db.primary.model.sysoption.SysPermission;
//import server.db.primary.model.sysoption.SysRole;
//import server.db.primary.model.sysoption.SysUser;
//import server.service.FunCLoginService;
//import server.service.SysUserService;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

//@Component
//public class UserLoginInfo {
//    @Autowired
//    SysUserService sysUserService;
//
//    Map getInfo(SysUser user) {
//        Map<String, Object> map = new HashMap<>();
//        {
//            List<SysPermission> authPermissionList = new ArrayList<>();
//            for (SysRole authRole : user.getRoleList()) {
//                authPermissionList.addAll(authRole.getPermissionList());
//            }
//            JSONArray authPermissionLJA = MenuTree.buildMenu(authPermissionList);
//            map.put("menu", authPermissionLJA);//返回添加menu菜单
//        }
//        {
//            SysGroup group = user.getGroup();
//            JSONObject unitJO = new JSONObject();
//            if (group != null) {
//                if (group.getId() != null && group.getUnitType() != null) {
//                    unitJO = sysUserService.selectUnitByIdAndType(group.getUnitId(), group.getUnitType());
//                }
//                unitJO.put("unitType", group.getUnitType());
//                group.setUnitId(null);
//                group.setUnitType(null);
//            }
//            user.setUnit(unitJO);//user添加单位信息
//        }
//        {
//            user.setPassword(null);
//            user.setRoleList(null);
//            user.setGroupId(null);
//            map.put("user", user);
//        }
//        return map;
//    }
//}
