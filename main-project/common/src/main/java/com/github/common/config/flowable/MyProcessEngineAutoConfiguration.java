//package com.github.common.config.flowable;
//
//import org.flowable.common.engine.impl.cfg.IdGenerator;
//import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
//import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
//import org.flowable.spring.SpringProcessEngineConfiguration;
//import org.flowable.spring.boot.*;
//import org.flowable.spring.boot.app.FlowableAppProperties;
//import org.flowable.spring.boot.idm.FlowableIdmProperties;
//import org.flowable.spring.boot.process.FlowableProcessProperties;
//import org.flowable.spring.boot.process.Process;
//import org.flowable.spring.boot.process.ProcessAsync;
//import org.flowable.spring.boot.process.ProcessAsyncHistory;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.util.List;
////重写配置类，使flowable使用单独的数据库
//@Configuration
//public class MyProcessEngineAutoConfiguration extends ProcessEngineAutoConfiguration {
//
//    public MyProcessEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableAppProperties appProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties) {
//        super(flowableProperties, processProperties, appProperties, idmProperties, mailProperties);
//    }
//
//    @Bean
//    @SuppressWarnings("all")
//    public SpringProcessEngineConfiguration springProcessEngineConfiguration(@Qualifier("actDataSource") DataSource dataSource, @Qualifier("actTransactionManager") PlatformTransactionManager platformTransactionManager, @Process ObjectProvider<IdGenerator> processIdGenerator, ObjectProvider<IdGenerator> globalIdGenerator, @ProcessAsync ObjectProvider<AsyncExecutor> asyncExecutorProvider, @ProcessAsyncHistory ObjectProvider<AsyncExecutor> asyncHistoryExecutorProvider) throws IOException {
//        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
//        List<Resource> resources = this.discoverDeploymentResources(this.flowableProperties.getProcessDefinitionLocationPrefix(), this.flowableProperties.getProcessDefinitionLocationSuffixes(), this.flowableProperties.isCheckProcessDefinitions());
//        if (resources != null && !resources.isEmpty()) {
//            conf.setDeploymentResources(resources.toArray(new Resource[0]));
//            conf.setDeploymentName(this.flowableProperties.getDeploymentName());
//        }
//
//        AsyncExecutor springAsyncExecutor = asyncExecutorProvider.getIfUnique();
//        if (springAsyncExecutor != null) {
//            conf.setAsyncExecutor(springAsyncExecutor);
//        }
//
//        AsyncExecutor springAsyncHistoryExecutor = asyncHistoryExecutorProvider.getIfUnique();
//        if (springAsyncHistoryExecutor != null) {
//            conf.setAsyncHistoryEnabled(true);
//            conf.setAsyncHistoryExecutor(springAsyncHistoryExecutor);
//        }
//
//        this.configureSpringEngine(conf, platformTransactionManager);
//        this.configureEngine(conf, dataSource);
//        conf.setDeploymentName(this.defaultText(this.flowableProperties.getDeploymentName(), conf.getDeploymentName()));
//        conf.setDisableIdmEngine(!this.flowableProperties.isDbIdentityUsed() || !this.idmProperties.isEnabled());
//        conf.setAsyncExecutorActivate(this.flowableProperties.isAsyncExecutorActivate());
//        conf.setAsyncHistoryExecutorActivate(this.flowableProperties.isAsyncHistoryExecutorActivate());
//        conf.setMailServerHost(this.mailProperties.getHost());
//        conf.setMailServerPort(this.mailProperties.getPort());
//        conf.setMailServerUsername(this.mailProperties.getUsername());
//        conf.setMailServerPassword(this.mailProperties.getPassword());
//        conf.setMailServerDefaultFrom(this.mailProperties.getDefaultFrom());
//        conf.setMailServerForceTo(this.mailProperties.getForceTo());
//        conf.setMailServerUseSSL(this.mailProperties.isUseSsl());
//        conf.setMailServerUseTLS(this.mailProperties.isUseTls());
//        conf.setEnableProcessDefinitionHistoryLevel(this.processProperties.isEnableProcessDefinitionHistoryLevel());
//        conf.setProcessDefinitionCacheLimit(this.processProperties.getDefinitionCacheLimit());
//        conf.setEnableSafeBpmnXml(this.processProperties.isEnableSafeXml());
//        conf.setHistoryLevel(this.flowableProperties.getHistoryLevel());
//        conf.setActivityFontName(this.flowableProperties.getActivityFontName());
//        conf.setAnnotationFontName(this.flowableProperties.getAnnotationFontName());
//        conf.setLabelFontName(this.flowableProperties.getLabelFontName());
//        IdGenerator idGenerator = this.getIfAvailable(processIdGenerator, globalIdGenerator);
//        if (idGenerator == null) {
//            idGenerator = new StrongUuidGenerator();
//        }
//
//        conf.setIdGenerator(idGenerator);
//        return conf;
//    }
//
//}
