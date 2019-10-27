package com.github.base.controller.example.test.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        new Thread(() -> {
            OutputStream os = new OutputStream() {
                @Override
                public void write(int b) {

                }
            };
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                //获得查询字符串(get)
                String queryString = exchange.getRequestURI().getQuery();
                Map<String, String> queryStringInfo = formData2Dic(queryString);
                hashMap.put("queryStringInfo", queryStringInfo);
                //获得表单提交数据(post)
                String postString = Arrays.toString(IOUtils.readFully(exchange.getRequestBody(), 0, true));
                Map<String, String> postInfo = formData2Dic(postString);
                hashMap.put("postInfo", postInfo);
                exchange.sendResponseHeaders(200, 0);
                os = exchange.getResponseBody();
                os.write(hashMap.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
    public static Map<String, String> formData2Dic(String formData) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        if (formData == null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        for (String item : items) {
            final String[] keyAndVal = item.split("=");
            if (keyAndVal.length == 2) {
                final String key = URLDecoder.decode(keyAndVal[0], "utf8");
                final String val = URLDecoder.decode(keyAndVal[1], "utf8");
                result.put(key, val);
            }
        }
        return result;
    }
}
