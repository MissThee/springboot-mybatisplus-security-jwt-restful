package server.config.tkmapper.custom;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface MyExampleMapper<T> {
    @SelectProvider(type = MyExampleProvider.class, method = "dynamicSQL")
    T selectAvgByExample(Object var1);

    @SelectProvider(type = MyExampleProvider.class, method = "dynamicSQL")
    T selectSumByExample(Object var1);
}
