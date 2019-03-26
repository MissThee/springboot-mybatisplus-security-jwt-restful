package com.github.missthee.controller.example.mq;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CustomModel {
    private String time = LocalDateTime.now().toString();
    private String value;
}

