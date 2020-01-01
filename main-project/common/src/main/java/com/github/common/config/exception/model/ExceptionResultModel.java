package com.github.common.config.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//异常时返回给前端的数据格式对象。
//暂时就一个msg进行异常消息返回，如果需要更细致的内容，手动添加属性，并在调用的地方赋值就可以了
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResultModel {
    public String msg;
}
