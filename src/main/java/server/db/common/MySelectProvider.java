package server.db.common;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Iterator;
import java.util.Set;

public class MySelectProvider extends MapperTemplate {
    public MySelectProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectSeq(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        return (" SELECT SEQ_" + this.tableName(entityClass).toUpperCase() + ".currval as ID from dual");
    }
}
