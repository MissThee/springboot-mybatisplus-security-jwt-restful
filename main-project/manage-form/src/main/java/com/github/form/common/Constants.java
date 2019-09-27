package com.github.form.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    @Value("${custom-config.file-host}:${custom-config.file-port}")//使用配置文件中host及port拼接图片url地址
    private void setSTATIC_RESOURCE_URL(String value) {
        STATIC_RESOURCE_URL = value;
    }
    public static String STATIC_RESOURCE_URL;
}
