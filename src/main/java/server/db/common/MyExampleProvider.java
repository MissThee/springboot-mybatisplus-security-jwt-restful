package server.db.common;


import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

public class MyExampleProvider extends MapperTemplate {
    public MyExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    //此方法使用example的orderBy代替groupBy
    public String selectAvgByExample(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        sql.append(exampleSelectColumns(entityClass, "avg"));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass).replace("order by ", "group by ").replace("ORDER BY ", "GROUP BY "));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    //此方法使用example的orderBy代替groupBy
    public String selectSumByExample(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        sql.append(exampleSelectColumns(entityClass, "sum"));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass).replace("order by ", "group by ").replace("ORDER BY ", "GROUP BY "));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    private static String exampleSelectColumns(Class<?> entityClass, String sumFunctionStr) {
        return "<choose>" +
                "<when test=\"@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)\">" +
                "<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">" +
                sumFunctionStr.trim() + "(${selectColumn}) ${selectColumn}" +
                "</foreach>" +
                "</when>" +
                "<otherwise>" +
                getAllColumns(entityClass) +
                "</otherwise>" +
                "</choose>";
    }

    private static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        for (EntityColumn entityColumn : columnSet) {
            sql.append(entityColumn.getColumn()).append(",");
        }
        return sql.substring(0, sql.length() - 1);
    }

}
