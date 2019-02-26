package server.tool.excel.response;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public class ResponseTool {
    public static void responseOut(HttpServletResponse response, Workbook wb, String fullFileName) throws IOException {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fullFileName, "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        OutputStream outputStream = response.getOutputStream(); // 打开流
        wb.write(outputStream);// HSSFWorkbook写入流
        wb.close();// HSSFWorkbook关闭
        outputStream.flush();// 刷新流
        outputStream.close();// 关闭流
    }
}
