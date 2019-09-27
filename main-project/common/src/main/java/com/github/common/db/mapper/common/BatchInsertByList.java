package com.github.common.db.mapper.common;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.type.JdbcType;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author:lycol 注入批量插入的方法, 方法名师insertBatch
 */
public class BatchInsertByList extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, getBatchInsertSql(tableInfo), modelClass);
        return this.addMappedStatement(mapperClass, CustomSqlMethod.INSERT_BATCH.getMethod(), sqlSource,
                SqlCommandType.INSERT, Collections.class, null, int.class, keyGenerator, null, null);

    }

    private String getBatchInsertSql(TableInfo tableInfo) {
        String batchInsertSql = CustomSqlMethod.INSERT_BATCH.getSql();

        StringBuilder insertColumnBuilder = new StringBuilder();
        StringBuilder itemColumnBuilder = new StringBuilder();
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        int size = fieldList.size();

        //主键列名
        if (tableInfo.getKeyColumn() != null && !tableInfo.getKeyColumn().equals("")) {
            insertColumnBuilder.append(tableInfo.getKeyColumn()).append(",");
        }
        //主键参数值
        if (tableInfo.getKeyProperty() != null && !tableInfo.getKeyProperty().equals("")) {
            itemColumnBuilder.append("#{item.").append(tableInfo.getKeyProperty()).append(",jdbcType=VARCHAR},");
        }
        //其余字段
        for (TableFieldInfo tableFieldInfo : fieldList) {
            insertColumnBuilder
                    .append(tableFieldInfo.getColumn())
                    .append(",");
            itemColumnBuilder
                    .append("#{item.")
                    .append(tableFieldInfo.getProperty())
                    .append(",jdbcType=")
                    .append(getJdbcTypeByClassType(tableFieldInfo.getPropertyType()))
                    .append("},");
        }
        if (insertColumnBuilder.length() > 0) {
            insertColumnBuilder.deleteCharAt(insertColumnBuilder.lastIndexOf(","));
        }
        if (itemColumnBuilder.length() > 0) {
            itemColumnBuilder.deleteCharAt(itemColumnBuilder.lastIndexOf(","));
        }
        String foreachSql;
        //如果是oracle数据库
//        if (tableInfo.getDbType() == DbType.ORACLE) {
//            foreachSql = "SELECT RAWTOHEX(SYS_GUID()), record.* FROM (\n" +
//                    " <foreach collection='items' item='item' index='index' separator='union all'>\n" +
//                    " select\n" +
//                    itemColumnBuilder.toString() +
//                    " FROM dual\n" +
//                    " </foreach>\n" +
//                    " ) record";
//        }

        //如果是非oracle数据库
//        else {
        foreachSql = "values" +
                " <foreach collection='items' item='item'  open='' index='index' separator=','>\n" +
                "(%s)</foreach>";

        foreachSql = String.format(foreachSql, itemColumnBuilder);
//        }
        return String.format(batchInsertSql, tableInfo.getTableName(), insertColumnBuilder, foreachSql);
    }

    private String getJdbcTypeByClassType(Class clazz) {
        if (clazz.getSuperclass() == Number.class) {
            return JdbcType.NUMERIC.name();
        }
        if (clazz == String.class) {
            return JdbcType.VARCHAR.name();
        }
        if (clazz == Date.class || clazz == java.sql.Date.class) {
            return JdbcType.DATE.name();
        }
//        if(clazz== TIMESTAMP.class){
//            return  JdbcType.TIMESTAMP.name();
//        }
        //默认返回JavaObject
        return JdbcType.JAVA_OBJECT.name();

    }

}
