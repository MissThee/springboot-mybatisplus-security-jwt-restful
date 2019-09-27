package com.github.common.config.db;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.secondary.enable", havingValue = "true")
@MapperScan(basePackages = "com.github.**.db.mapper.secondary", sqlSessionTemplateRef = "secondarySqlSessionTemplate")
@Slf4j
public class SecondaryDBConfig {
    @Bean(name = "secondaryDataSourceHikari")
    @ConfigurationProperties(prefix = "spring.datasource.secondary.hikari")
    public HikariConfig secondaryDataSourceHikari() {
        HikariConfig hikariConfig = new HikariConfig();
        if (StringUtils.isEmpty(hikariConfig.getPoolName())) {
            hikariConfig.setPoolName("SecondaryDBPool");
        }
        return hikariConfig;
    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource dataSource(@Qualifier("secondaryDataSourceHikari") HikariConfig hikariConfig) {
        HikariDataSource build =new HikariDataSource();
        hikariConfig.copyStateTo(build);
        return build;
    }

    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "secondarySqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource,
            @Qualifier("globalConfiguration") GlobalConfig globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setGlobalConfig(globalConfiguration);
        bean.setTransactionFactory(new SpringManagedTransactionFactory() {{
            newTransaction(dataSource, TransactionIsolationLevel.REPEATABLE_READ, true);
        }});
        try {
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/secondary/**/*.xml"));
        } catch (FileNotFoundException e) {
            log.info(e.getMessage() + ". File not exists.");
        }
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis.cfg.xml"));
        return bean;
    }

    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
