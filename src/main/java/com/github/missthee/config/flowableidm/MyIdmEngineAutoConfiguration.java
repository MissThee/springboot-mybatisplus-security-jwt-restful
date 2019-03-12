//package com.github.missthee.config.flowableidm;
//
//import org.flowable.idm.spring.SpringIdmEngineConfiguration;
//import org.flowable.spring.boot.FlowableProperties;
//import org.flowable.spring.boot.idm.FlowableIdmProperties;
//import org.flowable.spring.boot.idm.IdmEngineAutoConfiguration;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class MyIdmEngineAutoConfiguration extends IdmEngineAutoConfiguration {
//    public MyIdmEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableIdmProperties idmProperties) {
//        super(flowableProperties, idmProperties);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public SpringIdmEngineConfiguration idmEngineConfiguration(@Qualifier("activitiDataSource") DataSource dataSource, @Qualifier("activitiTransactionManager")  PlatformTransactionManager platformTransactionManager) {
//        SpringIdmEngineConfiguration configuration = new SpringIdmEngineConfiguration();
//        configuration.setTransactionManager(platformTransactionManager);
//        this.configureEngine(configuration, dataSource);
//        return configuration;
//    }
//}
