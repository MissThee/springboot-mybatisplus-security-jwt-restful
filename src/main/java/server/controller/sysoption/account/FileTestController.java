//package server.controller.sysoption;
//
//import com.github.pagehelper.PageInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import server.db.primary.model.userinfo.SysUser;
//import server.service.SysUserService;
//import server.tool.ExcelUtils;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////用户管理
//@RequestMapping("/file")
//@RestController
//public class FileTestController {
//
//    @Value("${custom-config.page.pageNumber}")
//    private int defaultPageNum;
//    @Value("${custom-config.page.pageSize}")
//    private int defaultPageSize;
//
//    @Autowired
//    SysUserService sysUserService;
//
//    @GetMapping("/getFileIndex")
//    public void getFileIndex(HttpServletResponse httpServletResponse) throws Exception {
//        List<SysUser> sysUserPageTable = sysUserService.selectUserGroupRole(1, 10);
//        Map<String, Object> map = new HashMap<>();
//        Map<String, String> columnMap = new HashMap<>();
//        columnMap.put("id", "键值");
//        ExcelUtils.exportWithIndex(httpServletResponse, "测试表", "标题标题标题标题标题标题标题标题标题标题标题标题标题标题", columnMap, sysUserPageTable.getList());
////        return Res.successData(map);
//    }
//
//    @GetMapping("/getFile")
//    public void getFile(HttpServletResponse httpServletResponse) throws Exception {
//        List<SysUser> sysUserPageTable = sysUserService.selectUserGroupRole(1, 10);
//        Map<String, Object> map = new HashMap<>();
//        Map<String, String> columnMap = new HashMap<>();
//        columnMap.put("id", "键值");
//        ExcelUtils.export(httpServletResponse, "测试表", "标题标题标题标题标题标题标题标题标题标题标题标题标题标题", columnMap, sysUserPageTable.getList());
////        return Res.successData(map);
//    }
//}
