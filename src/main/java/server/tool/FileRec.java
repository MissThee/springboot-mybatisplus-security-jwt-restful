package server.tool;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//接收上传文件
@Component
public class FileRec {
    private static String rootPath;
    private static int fileMaxSize;
    private static String webPath;

    @Value("${custom-config.upload.path}")
    public void setRootPath(String a) {
        rootPath = a;
    }

    @Value("${custom-config.upload.file-max-size}")
    public void setFileMaxSize(String a) {
        a = a.replaceAll(" ", "");
        String[] aArr = a.split("\\*");
        int size = 1;
        for (String s : aArr) {
            size *= Integer.parseInt(s);
        }
        fileMaxSize = size;
    }

    @Value("${custom-config.server.host-port}")
    public void setWebPath(String a) {
        webPath = a;
    }

    //上传至/files/upload/
    ///返回0成功 1出错 2没有文件 3文件过大
    public static JSONObject fileUpload(MultipartFile file, String path) {
        JSONObject res = new JSONObject();
        if (file.isEmpty()) {
            res.put("result", false);
            res.put("msg", "没有文件");
        }
        if (file.getSize() > fileMaxSize) {
            res.put("result", false);
            res.put("msg", "单个文件不能超过" + fileMaxSize / (1024 * 1024) + "M");
        }
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取扩展名
            String extensionName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("extensionName:" + extensionName);
            // 文件在静态资源文件夹中路径   必须使用files作为根目录，files已做拦截白名单处理
            String dataDirectory = "files" + File.separator + "upload" + File.separator + (StringUtils.isEmpty(path) ? "" : (path + File.separator));
            // 上传完整路径
            String filePath = rootPath.concat(dataDirectory);
            File dest = new File(filePath, fileName);
            if (dest.getParentFile().exists()) {
                if (dest.exists()) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + String.valueOf(System.currentTimeMillis()) + "." + extensionName;
                    dest = new File(filePath, fileName);
                }
            } else {
                if (!dest.getParentFile().mkdirs()) {
                    throw new IOException("创建目录失败");
                }
            }
            // 上传到指定目录
            file.transferTo(dest);
//                dest.renameTo(new File(dest.getParent()+"/111") );
            // 将图片流转换进行BASE64加码
            //BASE64Encoder encoder = new BASE64Encoder();
            //String data = encoder.encode(file.getBytes());
            // 将反斜杠转换为正斜杠
            String data = webPath + dataDirectory.replaceAll("\\\\", "/") + fileName;
            res.put("result", true);
            res.put("data", data);
            res.put("msg", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            res.put("result", false);
            res.put("msg", "上传出错。详情：" + e.toString());
        }
        return res;
    }

    public static JSONObject fileUpload(MultipartFile file) {
        return fileUpload(file, "");
    }
}
