package com.github.missthee.config.flowable;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class ActDBConfig {
    @Bean(name = "actDataSourceHikari")
    @ConfigurationProperties(prefix = "spring.datasource.act.hikari")
    public HikariConfig actDataSourceHikari() {
        HikariConfig hikariConfig = new HikariConfig();
        if (StringUtils.isEmpty(hikariConfig.getPoolName())) {
            hikariConfig.setPoolName("ActDBPool");
        }
        return hikariConfig;
    }

    @Bean(name = "actDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.act")
    public DataSource dataSource(@Qualifier("actDataSourceHikari") HikariConfig hikariConfig) {
        HikariDataSource build = new HikariDataSource();
        hikariConfig.copyStateTo(build);
        return build;
    }

    @Bean(name = "actTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("actDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
