//package com.github.common.config.db;
//
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.io.FileNotFoundException;
//import java.sql.SQLException;
//
//@Configuration
//@ConditionalOnProperty(name = "spring.datasource.primary.enable", havingValue = "true")
//@MapperScan(basePackages = {"com.github.**.db.mapper.primary"}, sqlSessionTemplateRef = "primarySqlSessionTemplate")
//@Slf4j
//public class PrimaryDBConfigAtomikos {
//
//    /**
//     * 将application.properties配置文件中配置自动封装到实体类字段中
//     *
//     * @author Administrator
//     */
//    @Data
//    @Component
//    @ConfigurationProperties(prefix = "spring.datasource.primary") // 注意这个前缀要和application.properties文件的前缀一样
//    public static class DBConfig {
//        private String url;
//        // 比如这个url在properties中是这样子的mysql.datasource.primary1.username = root
//        private String username;
//        private String password;
//        private int minPoolSize;
//        private int maxPoolSize;
//        private int maxLifetime;
//        private int borrowConnectionTimeout;
//        private int loginTimeout;
//        private int maintenanceInterval;
//        private int maxIdleTime;
//        private String testQuery;
//    }
//
//    // 配置数据源
//    @Bean(name = "primaryDataSource")
//    public DataSource primaryDataSource(DBConfig primaryConfig) throws SQLException {
//        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
//        mysqlXaDataSource.setUrl(primaryConfig.getUrl());
//        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
//        mysqlXaDataSource.setPassword(primaryConfig.getPassword());
//        mysqlXaDataSource.setUser(primaryConfig.getUsername());
//        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
//
//        // 将本地事务注册到创 Atomikos全局事务
//        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//        xaDataSource.setXaDataSource(mysqlXaDataSource);
//        xaDataSource.setUniqueResourceName("primaryDataSource");
//
//        xaDataSource.setMinPoolSize(primaryConfig.getMinPoolSize());
//        xaDataSource.setMaxPoolSize(primaryConfig.getMaxPoolSize());
//        xaDataSource.setMaxLifetime(primaryConfig.getMaxLifetime());
//        xaDataSource.setBorrowConnectionTimeout(primaryConfig.getBorrowConnectionTimeout());
//        xaDataSource.setLoginTimeout(primaryConfig.getLoginTimeout());
//        xaDataSource.setMaintenanceInterval(primaryConfig.getMaintenanceInterval());
//        xaDataSource.setMaxIdleTime(primaryConfig.getMaxIdleTime());
//        xaDataSource.setTestQuery(primaryConfig.getTestQuery());
//        return xaDataSource;
//    }
//
//    @Bean(name = "primarySqlSessionFactory")
//    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
//        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        try {
//            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/primary/**/*.xml"));
//        } catch (FileNotFoundException e) {
//            log.info(e.getMessage() + ". File not exists.");
//        }
//        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis.cfg.xml"));
//        return bean;
//    }
//
//    @Bean(name = "primarySqlSessionTemplate")
//    public SqlSessionTemplate primarySqlSessionTemplate(
//            @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
////    @Bean(name = "primaryTransactionManager")
////    public DataSourceTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
////        return new DataSourceTransactionManager(dataSource);
////    }
//}
