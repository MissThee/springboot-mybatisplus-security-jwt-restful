package server.db.common;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;


@RegisterMapper
public interface MySelectMapper<T> {
    //    通用mapper获取相关表的当前序列值，需制定序列表名为 TABLE_NAME，序列名为TABLE_NAME_SEQ
    //    使用是紧跟在insert之后即可，***必须与insert在同一事务中***
    @SelectProvider(type = MySelectProvider.class, method = "dynamicSQL")
    Long selectSeq();
}