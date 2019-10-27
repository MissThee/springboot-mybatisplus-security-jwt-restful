package com.github.missthee.config.tkmapper.custom;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface MySelectByExampleMapper<T> {
    @SelectProvider(type = MySelectByExampleProvider.class, method = "dynamicSQL")
    T selectAvgByExample(Object var1);

    @SelectProvider(type = MySelectByExampleProvider.class, method = "dynamicSQL")
    T selectSumByExample(Object var1);

    @SelectProvider(type = MySelectByExampleProvider.class, method = "dynamicSQL")
    List<T> selectAvgListByExample(Object var1);

    @SelectProvider(type = MySelectByExampleProvider.class, method = "dynamicSQL")
    List<T> selectSumListByExample(Object var1);
}
