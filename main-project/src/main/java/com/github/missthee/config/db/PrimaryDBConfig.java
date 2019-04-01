package com.github.missthee.config.db;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.primary.enable", havingValue ="true")
@MapperScan(basePackages = {"com.github.missthee.db.mapper.primary"}, sqlSessionTemplateRef = "primarySqlSessionTemplate")
@Slf4j
public class PrimaryDBConfig {

    @Bean(name = "primaryDataSourceHikari")
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    public HikariConfig primaryDataSourceHikari() {
        HikariConfig hikariConfig = new HikariConfig();
        if (StringUtils.isEmpty(hikariConfig.getPoolName())) {
            hikariConfig.setPoolName("PrimaryDBPool");
        }
        return hikariConfig;
    }

    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource(@Qualifier("primaryDataSourceHikari") HikariConfig hikariConfig) {
        HikariDataSource build = DataSourceBuilder.create().type(HikariDataSource.class).build();
        hikariConfig.copyStateTo(build);
        return build;
    }

    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primarySqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        try {
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/primary/**/*.xml"));
        } catch (FileNotFoundException e) {
            log.info(e.getMessage() + ". File not exists.");
        }
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis.cfg.xml"));
        return bean;
    }

    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
