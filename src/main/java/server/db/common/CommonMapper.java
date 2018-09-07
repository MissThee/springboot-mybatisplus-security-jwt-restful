package server.db.common;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
//通用mapper内含生成器生成的一般方法，若自己的mapper继承mapper需自行删除自己mapper.java以及mapper.xml中与此同名的方法。
public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T>,MyInsertMapper<T>,MySelectMapper<T>,MyExampleMapper<T> {
    //通过T的类名由驼峰转为下划线，映射表。如T为 UserRole 映射表为 USER_ROLE
    //使用selectByPrimaryKey查询，一定要在实体类中增加 @Id 主键，否则参数会传入所有列名。
}
