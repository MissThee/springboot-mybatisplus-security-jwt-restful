package server.db.common;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.provider.SpecialProvider;

import java.util.List;


@RegisterMapper
public interface MyInsertMapper<T> {
    //通用mapper。Oracle批量插入，非selective
    @InsertProvider(type = MyInsertProvider.class, method = "dynamicSQL")
    int insertListOracle(List<T> var1);
}