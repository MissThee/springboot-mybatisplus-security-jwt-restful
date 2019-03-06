package com.github.missthee.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

//接收上传文件
public class FileRec {
    private static String rootPath;
    private static DataSize fileMaxSize;

    @Value("${custom-config.upload.path}")
    public void setRootPath(String a) {
        if (a.endsWith("\\") || a.endsWith("/")) {
            a = a.substring(0, a.length() - 1);
        }
        rootPath = a + File.separator;
    }

    @Value("${spring.servlet.multipart.max-file-size}")
    public void setFileMaxSize(DataSize a) {
        fileMaxSize = a;
    }

    //上传至 setRootPath 变量设置的目录中
    public static String fileUpload(MultipartFile file, String path) throws FileNotFoundException, SizeLimitExceededException {
        if (path != null) {
            path = path.replace("/", File.separator).replace("\\", File.separator);
        }
        if (file == null) {
            throw new FileNotFoundException("No File Uploaded");
        }
        if (file.isEmpty()) {
            throw new FileNotFoundException("Empty File");
        }
        if (file.getSize() > fileMaxSize.toBytes()) {
            throw new SizeLimitExceededException("File too large. " + "Less than " + String.format(" % .0f", Double.parseDouble(String.valueOf(fileMaxSize.toKilobytes())) / 1024) + "M each file");
        }
        try {
            // 获取完整文件名
            String fileName = file.getOriginalFilename() == null ? "DefaultName" + LocalDateTime.now() : file.getOriginalFilename();
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
                    throw new IOException("Fail to create dir");
                }
            }
            // 上传到指定目录
            file.transferTo(dest);
            //dest.renameTo(new File(dest.getParent()+"/111") );
            //将图片流转换进行BASE64加码
            //BASE64Encoder encoder = new BASE64Encoder();
            //String data = encoder.encode(file.getBytes());
            //修正url分隔符。此地址不包含host:port 内容，在返回时自行拼接
            return dataDirectory.replace("\\", "/") + fileName;
        } catch (IOException e) {
            return null;
        }
    }

    public static String fileUpload(MultipartFile file) throws FileNotFoundException, SizeLimitExceededException {
        return fileUpload(file, "");
    }


}
