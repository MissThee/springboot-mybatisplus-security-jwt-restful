package com.github.common.config.mybatis.resultinmap;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
//mybatis使用Map接收查询结果时，将查询结果中字段，由下划线转为小驼峰格式的配置。需在数据库配置中引用此配置
@Component
public class MapWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return object instanceof Map;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new CustomWrapper(metaObject,(Map)object);
    }
}