package com.github.missthee.config.tkmapper.common;


import com.github.missthee.config.tkmapper.custom.MySelectByExampleMapper;
import com.github.missthee.config.tkmapper.custom.MyInsertMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T>, MyInsertMapper<T>, MySelectByExampleMapper<T> {
    //通用mapper内含生成器生成的一般方法，若自己的mapper继承CommonMapper，则mapper.java与mapper.xml中不能再出现与通用mapper中同名的方法。
    //通过T的类名由驼峰转为下划线，映射表。如T为 UserRole 映射表为 USER_ROLE
    //使用selectByPrimaryKey查询，一定要在实体类中增加 @Id 主键，否则参数会传入所有列名。
}
