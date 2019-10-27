package com.github.missthee.config.tkmapper.custom;


import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class MySelectByExampleProvider extends MapperTemplate {
    public MySelectByExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    //此方法使用example的orderBy代替groupBy
    public String selectAvgByExample(MappedStatement ms) {
        return getString(ms, "avg");
    }

    //此方法使用example的orderBy代替groupBy
    public String selectSumByExample(MappedStatement ms) {
        return getString(ms, "sum");
    }
    //此方法使用example的orderBy代替groupBy
    public String selectAvgListByExample(MappedStatement ms) {
        return getString(ms, "avg");
    }

    //此方法使用example的orderBy代替groupBy
    public String selectSumListByExample(MappedStatement ms) {
        return getString(ms, "sum");
    }
    private String getString(MappedStatement ms, String functionStr) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (this.isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        sql.append("<if test=\"distinct\">distinct</if>");
        sql.append("<choose>" +
                "<when test=\"@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)\">" +
                "<if test=\"orderByClause != null\">" +
                "${orderByClause}," +
                "</if>" +
                (orderByClause.length() > 0 ? "<if test=\"orderByClause == null\">" + orderByClause + ",</if>" : "") +
                "<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">" +
                functionStr.trim() + "(${selectColumn}) ${selectColumn}" +
                "</foreach>" +
                "</when>" +
                "<otherwise>" +
                "*" +
                "</otherwise>" +
                "</choose>");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass).replace("order by ", "group by ").replace("ORDER BY ", "GROUP BY "));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }


}
