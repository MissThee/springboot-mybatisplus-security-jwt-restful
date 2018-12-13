package server.db.common;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.List;

@RegisterMapper
public interface MyExampleMapper<T> {
    @SelectProvider(type = MyExampleProvider.class, method = "dynamicSQL")
    T selectAvgByExample(Object var1);

    @SelectProvider(type = MyExampleProvider.class, method = "dynamicSQL")
    T selectSumByExample(Object var1);
}
