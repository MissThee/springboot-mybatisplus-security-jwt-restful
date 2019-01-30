package server.config.tkmapper.custom;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.*;

public class MySelectProvider extends MapperTemplate {
    public MySelectProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectSeq(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        return (" SELECT SEQ_" + this.tableName(entityClass).toUpperCase() + ".currval as ID from dual");
    }
}
