package com.github.common.config.db;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.common.config.mybatis.resultinmap.MapWrapperFactory;
import com.github.common.db.mapper.common.CustomerSqlInjector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.primary.enable", havingValue = "true")
//此处的basePackages决定哪些包下的方法使用这个数据源
@MapperScan(basePackages = {"com.github.**.db.mapper.primary"}, sqlSessionTemplateRef = "primarySqlSessionTemplate")
@Slf4j
public class PrimaryDBConfig {
    //创建 数据库配置对象，从配置文件（application-common.properties）的spring.datasource.primary.hikari.xxx加载配置
    @Bean(name = "primaryDataSourceHikari")
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    public HikariConfig primaryDataSourceHikari() {
        HikariConfig hikariConfig = new HikariConfig();
        if (StringUtils.isEmpty(hikariConfig.getPoolName())) {
            hikariConfig.setPoolName("PrimaryDBPool");
        }
        return hikariConfig;
    }

    //创建 数据源对象。使用上面的配置对象创建DataSource对象
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource(@Qualifier("primaryDataSourceHikari") HikariConfig hikariConfig) {
        HikariDataSource build = DataSourceBuilder.create().type(HikariDataSource.class).build();
        hikariConfig.copyStateTo(build);
        return build;
    }

    //创建 数据源事务管理器对象。
    //多数据源注意在使用事务时，如注解@Transactional时要使用value="primaryTransactionManager",指定使用哪个事务管理器，否则调用被注解的方法时报错，发现多个事务管理器未指定使用哪个
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //创建 MybatisSqlSessionFactoryBean对象。通过加载器读取mybatis.xml 和 myabtis-spring.xml生成 SqlSessionFactory。读取XxxMapper.xml生成对象
    @Bean(name = "primarySqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource dataSource,
            @Qualifier("globalConfiguration") GlobalConfig globalConfiguration,
            GlobalConfig globalConfig,
            MapWrapperFactory mapWrapperFactory) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setGlobalConfig(globalConfig);
        bean.setObjectWrapperFactory(mapWrapperFactory);
        bean.setDataSource(dataSource);
        bean.setGlobalConfig(globalConfiguration);
        SpringManagedTransactionFactory springManagedTransactionFactory = new SpringManagedTransactionFactory();
        springManagedTransactionFactory.newTransaction(dataSource, TransactionIsolationLevel.REPEATABLE_READ, true);
        bean.setTransactionFactory(springManagedTransactionFactory);
        try {
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/primary/**/*.xml"));
        } catch (FileNotFoundException e) {
            log.info(e.getMessage() + ". File not exists.");
        }
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis.cfg.xml"));
        return bean;
    }

    //创建 SqlSessionTemplate对象。mybatis的核心，负责管理mybatis的sqlSession
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
