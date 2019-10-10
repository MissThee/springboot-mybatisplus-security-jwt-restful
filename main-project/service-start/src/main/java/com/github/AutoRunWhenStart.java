//package com.github;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.Properties;
//
///**
// * 继承Application接口后项目启动时会按照执行顺序执行run方法
// * 通过设置Order的value来指定执行的顺序
// */
//@Component
//@Order(value = 1)
//public class AutoRunWhenStart implements ApplicationRunner {
//    private final DataSource primaryDataSource;
//    private final DataSource secondaryDataSource;
//
//    public AutoRunWhenStart(@Qualifier("primaryDataSource") DataSource primaryDataSource, @Qualifier("secondaryDataSource")  DataSource secondaryDataSource) {
//        this.primaryDataSource = primaryDataSource;
//        this.secondaryDataSource = secondaryDataSource;
//    }
//
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        Connection conn = primaryDataSource.getConnection();
//
//        Statement st=conn.createStatement();
//        //Statement用于执行静态的sql语句
//        ResultSet re=st.executeQuery("select * from dept");
//        //ResultSet游标遍历数据行 next()
//        while(re.next()){
//            String deptno=re.getString("deptno");//通过列明获取数据
//            String dname=re.getString("dname");
//            String loc=re.getString(3);//通过索引获取数据
//            System.out.println(deptno+"--"+dname+"--"+loc);
//        }
//        st.close();
//        conn.close();
//    }
//
//
//
//}
