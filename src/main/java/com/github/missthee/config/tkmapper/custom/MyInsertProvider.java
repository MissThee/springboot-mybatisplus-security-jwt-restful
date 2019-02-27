package com.github.missthee.config.tkmapper.custom;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

public class MyInsertProvider extends MapperTemplate {
    public MyInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertListOracle(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>\n");
        sql.append("INSERT ALL\n");
        sql.append("<foreach collection=\"list\" item=\"record\">\n");
        String tableName = SqlHelper.getDynamicTableName(entityClass, this.tableName(entityClass), "list[0]");
        String columns = SqlHelper.insertColumns(entityClass, false, false, false);
        sql.append(" INTO ").append(tableName).append(" ").append(columns).append("\n");
        sql.append(" VALUES ");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var7 = columnList.iterator();

        EntityColumn column;
        while (var7.hasNext()) {
            column = (EntityColumn) var7.next();
            if (column.getGenIdClass() != null) {
                sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("record").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                sql.append(", '").append(this.tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }
        }

        var7 = columnList.iterator();

        while (var7.hasNext()) {
            column = (EntityColumn) var7.next();
            if (column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }

        sql.append("</trim>\n");
        sql.append("</foreach>\n");
        sql.append("SELECT 1 FROM DUAL");
        return sql.toString();
    }
}
