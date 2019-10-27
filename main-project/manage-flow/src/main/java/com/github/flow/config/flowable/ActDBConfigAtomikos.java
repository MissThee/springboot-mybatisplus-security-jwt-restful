package com.github.flow.config.flowable;//package com.github.missthee.config.flowable;
//
//import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.flowable.engine.impl.db.DbIdGenerator;
//import org.flowable.spring.SpringProcessEngineConfiguration;
//import org.flowable.spring.boot.EngineConfigurationConfigurer;
//import org.mybatis.spring.SqlSessionFactoryBean;
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
//@Slf4j
//public class ActDBConfigAtomikos {
//
//    /**
//     * 将application.properties配置文件中配置自动封装到实体类字段中
//     *
//     * @author Administrator
//     */
//    @Data
//    @Component
//    @ConfigurationProperties(prefix = "spring.datasource.act") // 注意这个前缀要和application.properties文件的前缀一样
//    public static class DBConfig {
//        private String url;
//        // 比如这个url在properties中是这样子的mysql.datasource.act1.username = root
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
//    @Bean(name = "actDataSource")
//    public DataSource actDataSource(DBConfig actConfig) throws SQLException {
//        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
//        mysqlXaDataSource.setUrl(actConfig.getUrl());
//        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
//        mysqlXaDataSource.setPassword(actConfig.getPassword());
//        mysqlXaDataSource.setUser(actConfig.getUsername());
//        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
//
//        // 将本地事务注册到创 Atomikos全局事务
//        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//        xaDataSource.setXaDataSource(mysqlXaDataSource);
//        xaDataSource.setUniqueResourceName("actDataSource");
//
//        xaDataSource.setMinPoolSize(actConfig.getMinPoolSize());
//        xaDataSource.setMaxPoolSize(actConfig.getMaxPoolSize());
//        xaDataSource.setMaxLifetime(actConfig.getMaxLifetime());
//        xaDataSource.setBorrowConnectionTimeout(actConfig.getBorrowConnectionTimeout());
//        xaDataSource.setLoginTimeout(actConfig.getLoginTimeout());
//        xaDataSource.setMaintenanceInterval(actConfig.getMaintenanceInterval());
//        xaDataSource.setMaxIdleTime(actConfig.getMaxIdleTime());
//        xaDataSource.setTestQuery(actConfig.getTestQuery());
//        return xaDataSource;
//    }
//
//    //  此处可不单独配置事务管理器，直接统一使用jta
////    @Bean(name = "actTransactionManager")
////    public DataSourceTransactionManager transactionManager(@Qualifier("actDataSource") DataSource dataSource) {
////        return new DataSourceTransactionManager(dataSource);
////    }
//    //不使用uuid主键生成器
//    @Bean
//    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> processEngineDbIdGeneratorConfigurer() {
//        return engineConfiguration -> engineConfiguration.setIdGenerator(new DbIdGenerator());
//    }
//}
