package com.github.missthee.config.tkmapper.custom;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;


@RegisterMapper
public interface MyInsertMapper<T> {
    //通用mapper。Oracle批量插入，非selective
    @InsertProvider(type = MyInsertProvider.class, method = "dynamicSQL")
    int insertListOracle(List<T> var1);
}