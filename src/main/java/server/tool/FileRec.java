package server.tool;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

//接收上传文件
@Component
public class FileRec {
    private static String rootPath;
    private static DataSize fileMaxSize;
    private static String webPath;

    @Value("${custom-config.upload.path}")
    public void setRootPath(String a) {
        rootPath = a;
    }

    @Value("${spring.servlet.multipart.max-file-size}")
    public void setFileMaxSize(DataSize a) {
        fileMaxSize = a;
    }

    @Value("http://${server.address}:${server.port}/")
    public void setWebPath(String a) {
        webPath = a;
    }

    //上传至 setRootPath 变量设置的目录中
    ///返回0成功 1出错 2没有文件 3文件过大
    public JSONObject fileUpload(MultipartFile file, String path) {
        JSONObject res = new JSONObject();
        if (file.isEmpty()) {
            res.put("result", false);
            res.put("msg", "没有文件");
        }
        if (file.getSize() > fileMaxSize.toBytes()) {
            res.put("result", false);
            res.put("msg", "单个文件不能超过" + String.format("%.0f", Double.parseDouble(String.valueOf(fileMaxSize.toKilobytes())) / 1024) + "M");
        }
        try {
            // 获取完整文件名
            String fileName = file.getOriginalFilename() == null ? "未命名文件" + LocalDateTime.now() : file.getOriginalFilename();
            // 获取扩展名
            String extensionName = fileName.lastIndexOf(".") >= 0 ? fileName.substring(fileName.lastIndexOf(".")) : "";
            // 文件在静态资源文件夹中路径
            String dataDirectory = File.separator + "files" + File.separator + "upload" + File.separator + (StringUtils.isEmpty(path) ? "" : (path + File.separator));
            // 拼接设置的额外静态资源目录路径
            String filePath = rootPath.concat(dataDirectory);
            File dest = new File(filePath, fileName);
            //目录已存在
            if (dest.getParentFile().exists()) {
                //文件已存在，重命名，加时间戳
                if (dest.exists()) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + System.currentTimeMillis() + "." + extensionName;
                    dest = new File(filePath, fileName);
                }
                //目录不存在,创建目录
            } else {
                if (!dest.getParentFile().mkdirs()) {
                    throw new IOException("创建目录失败");
                }
            }
            // 上传到指定目录
            file.transferTo(dest);
            //dest.renameTo(new File(dest.getParent()+"/111") );
            //将图片流转换进行BASE64加码
            //BASE64Encoder encoder = new BASE64Encoder();
            //String data = encoder.encode(file.getBytes());
            //修url分隔符
            String data = webPath + dataDirectory.replaceAll("\\\\", "/") + fileName;
            res.put("result", true);
            res.put("data", data);
            res.put("msg", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            res.put("result", false);
            res.put("msg", "上传出错。详情：" + e);
        }
        return res;
    }

    public JSONObject fileUpload(MultipartFile file) {
        return fileUpload(file, "");
    }
}
