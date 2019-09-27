1. 引入依赖
    ```xml
    <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-jta-atomikos</artifactId>
    </dependency>
    ```
2. 数据库连接配置
    ```properties
    #--------------------数据库----------------------------------------
    #一
    spring.datasource.primary.enable=true
    spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver
    spring.datasource.primary.url=jdbc:mysql://192.168.8.158:3306/discipline_inspection_commission?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false
    spring.datasource.primary.username=user
    spring.datasource.primary.password=1qazxsw2!
    
    spring.datasource.primary.minPoolSize = 3
    spring.datasource.primary.maxPoolSize = 10
    spring.datasource.primary.maxLifetime = 20000
    spring.datasource.primary.borrowConnectionTimeout = 30
    spring.datasource.primary.loginTimeout = 30
    spring.datasource.primary.maintenanceInterval = 60
    spring.datasource.primary.maxIdleTime = 60
    #二(未启用)
    spring.datasource.secondary.enable=false
    spring.datasource.secondary.driver-class-name=oracle.jdbc.driver.OracleDriver
    spring.datasource.secondary.url=jdbc:oracle:thin:@//127.0.0.1:1521/XE
    spring.datasource.secondary.username=MT
    spring.datasource.secondary.password=7777
    
    spring.datasource.secondary.minPoolSize = 3
    spring.datasource.secondary.maxPoolSize = 10
    spring.datasource.secondary.maxLifetime = 20000
    spring.datasource.secondary.borrowConnectionTimeout = 30
    spring.datasource.secondary.loginTimeout = 30
    spring.datasource.secondary.maintenanceInterval = 60
    spring.datasource.secondary.maxIdleTime = 60
    ```
3. 数据库连接配置bean
    1. PrimaryDBConfigAtomikos
        ```java
        @Configuration
        @ConditionalOnProperty(name = "spring.datasource.primary.enable", havingValue = "true")
        @MapperScan(basePackages = {"com.github.**.db.mapper.primary"}, sqlSessionTemplateRef = "primarySqlSessionTemplate")
        @Slf4j
        public class PrimaryDBConfigAtomikos {
        
            /**
             * 将application.properties配置文件中配置自动封装到实体类字段中
             *
             * @author Administrator
             */
            @Data
            @Component
            @ConfigurationProperties(prefix = "spring.datasource.primary") // 注意这个前缀要和application.properties文件的前缀一样
            public static class DBConfig {
                private String url;
                // 比如这个url在properties中是这样子的mysql.datasource.primary1.username = root
                private String username;
                private String password;
                private int minPoolSize;
                private int maxPoolSize;
                private int maxLifetime;
                private int borrowConnectionTimeout;
                private int loginTimeout;
                private int maintenanceInterval;
                private int maxIdleTime;
                private String testQuery;
            }
        
            // 配置数据源
            @Bean(name = "primaryDataSource")
            public DataSource primaryDataSource(DBConfig primaryConfig) throws SQLException {
                MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
                mysqlXaDataSource.setUrl(primaryConfig.getUrl());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
                mysqlXaDataSource.setPassword(primaryConfig.getPassword());
                mysqlXaDataSource.setUser(primaryConfig.getUsername());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        
                // 将本地事务注册到创 Atomikos全局事务
                AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
                xaDataSource.setXaDataSource(mysqlXaDataSource);
                xaDataSource.setUniqueResourceName("primaryDataSource");
        
                xaDataSource.setMinPoolSize(primaryConfig.getMinPoolSize());
                xaDataSource.setMaxPoolSize(primaryConfig.getMaxPoolSize());
                xaDataSource.setMaxLifetime(primaryConfig.getMaxLifetime());
                xaDataSource.setBorrowConnectionTimeout(primaryConfig.getBorrowConnectionTimeout());
                xaDataSource.setLoginTimeout(primaryConfig.getLoginTimeout());
                xaDataSource.setMaintenanceInterval(primaryConfig.getMaintenanceInterval());
                xaDataSource.setMaxIdleTime(primaryConfig.getMaxIdleTime());
                xaDataSource.setTestQuery(primaryConfig.getTestQuery());
                return xaDataSource;
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
            public SqlSessionTemplate primarySqlSessionTemplate(
                    @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
                return new SqlSessionTemplate(sqlSessionFactory);
            }
        //可不配置单独的事务管理器，统一使用jta；如果配置了单独的事务管理器，且未使用@Primary，使用@Transactional注解时需指定value值，因为多个事务管理器，spring无法自动选择使用哪一个
        //    @Bean(name = "primaryTransactionManager")
        //    public DataSourceTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        //        return new DataSourceTransactionManager(dataSource);
        //    }
        }
        
        ```

    2. SecondaryDBConfig.java
        ```java
        @Configuration
        @ConditionalOnProperty(name = "spring.datasource.secondary.enable", havingValue = "true")
        @MapperScan(basePackages = {"com.github.**.db.mapper.secondary"}, sqlSessionTemplateRef = "secondarySqlSessionTemplate")
        @Slf4j
        public class SecondaryDBConfigAtomikos {
        
            /**
             * 将application.properties配置文件中配置自动封装到实体类字段中
             *
             * @author Administrator
             */
            @Data
            @Component
            @ConfigurationProperties(prefix = "spring.datasource.secondary") // 注意这个前缀要和application.properties文件的前缀一样
            public static class DBConfig {
                private String url;
                // 比如这个url在properties中是这样子的mysql.datasource.secondary1.username = root
                private String username;
                private String password;
                private int minPoolSize;
                private int maxPoolSize;
                private int maxLifetime;
                private int borrowConnectionTimeout;
                private int loginTimeout;
                private int maintenanceInterval;
                private int maxIdleTime;
                private String testQuery;
            }
        
            // 配置数据源
            @Bean(name = "secondaryDataSource")
            public DataSource secondaryDataSource(DBConfig secondaryConfig) throws SQLException {
                MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
                mysqlXaDataSource.setUrl(secondaryConfig.getUrl());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
                mysqlXaDataSource.setPassword(secondaryConfig.getPassword());
                mysqlXaDataSource.setUser(secondaryConfig.getUsername());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        
                // 将本地事务注册到创 Atomikos全局事务
                AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
                xaDataSource.setXaDataSource(mysqlXaDataSource);
                xaDataSource.setUniqueResourceName("secondaryDataSource");
        
                xaDataSource.setMinPoolSize(secondaryConfig.getMinPoolSize());
                xaDataSource.setMaxPoolSize(secondaryConfig.getMaxPoolSize());
                xaDataSource.setMaxLifetime(secondaryConfig.getMaxLifetime());
                xaDataSource.setBorrowConnectionTimeout(secondaryConfig.getBorrowConnectionTimeout());
                xaDataSource.setLoginTimeout(secondaryConfig.getLoginTimeout());
                xaDataSource.setMaintenanceInterval(secondaryConfig.getMaintenanceInterval());
                xaDataSource.setMaxIdleTime(secondaryConfig.getMaxIdleTime());
                xaDataSource.setTestQuery(secondaryConfig.getTestQuery());
                return xaDataSource;
            }
        
            @Bean(name = "secondarySqlSessionFactory")
            public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
                MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
                bean.setDataSource(dataSource);
                try {
                    bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/secondary/**/*.xml"));
                } catch (FileNotFoundException e) {
                    log.info(e.getMessage() + ". File not exists.");
                }
                bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis.cfg.xml"));
                return bean;
            }
        
            @Bean(name = "secondarySqlSessionTemplate")
            public SqlSessionTemplate secondarySqlSessionTemplate(
                    @Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
                return new SqlSessionTemplate(sqlSessionFactory);
            }
        
        //    @Bean(name = "secondaryTransactionManager")
        //    public DataSourceTransactionManager transactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        //        return new DataSourceTransactionManager(dataSource);
        //    }
        }
        ```
4. 配置全局事务管理器
    ```java
    @Configuration
    @EnableTransactionManagement
    public class TransactionConfig {
        @Bean(name = "userTransaction")
        public UserTransaction userTransaction() throws Throwable {
            UserTransactionImp userTransactionImp = new UserTransactionImp();
            userTransactionImp.setTransactionTimeout(30000);
            return userTransactionImp;
        }
    
        @Bean(name = "atomikosTransactionManager")
        public TransactionManager atomikosTransactionManager() {
            UserTransactionManager userTransactionManager = new UserTransactionManager();
            userTransactionManager.setForceShutdown(false);
            return userTransactionManager;
        }
       
        @Bean(name = "jta")
        @DependsOn({"userTransaction", "atomikosTransactionManager"})
        public PlatformTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager atomikosTransactionManager)  {
            return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
        }
    }
    ```
5. 以上配置中仅配置了名为jta的一个事务管理器，使用@Transactional时默认全部使用此事务管理器
6. 附加：flowable配置jta事务管理器
    1. ActDBConfigAtomikos
        ```java
        @Configuration
        @Slf4j
        public class ActDBConfigAtomikos {
            @Data
            @Component
            @ConfigurationProperties(prefix = "spring.datasource.act") // 注意这个前缀要和application.properties文件的前缀一样
            public static class DBConfig {
                private String url;
                // 比如这个url在properties中是这样子的mysql.datasource.act1.username = root
                private String username;
                private String password;
                private int minPoolSize;
                private int maxPoolSize;
                private int maxLifetime;
                private int borrowConnectionTimeout;
                private int loginTimeout;
                private int maintenanceInterval;
                private int maxIdleTime;
                private String testQuery;
            }
        
            // 配置数据源
            @Bean(name = "actDataSource")
            public DataSource actDataSource(DBConfig actConfig) throws SQLException {
                MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
                mysqlXaDataSource.setUrl(actConfig.getUrl());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
                mysqlXaDataSource.setPassword(actConfig.getPassword());
                mysqlXaDataSource.setUser(actConfig.getUsername());
                mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        
                // 将本地事务注册到创 Atomikos全局事务
                AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
                xaDataSource.setXaDataSource(mysqlXaDataSource);
                xaDataSource.setUniqueResourceName("actDataSource");
        
                xaDataSource.setMinPoolSize(actConfig.getMinPoolSize());
                xaDataSource.setMaxPoolSize(actConfig.getMaxPoolSize());
                xaDataSource.setMaxLifetime(actConfig.getMaxLifetime());
                xaDataSource.setBorrowConnectionTimeout(actConfig.getBorrowConnectionTimeout());
                xaDataSource.setLoginTimeout(actConfig.getLoginTimeout());
                xaDataSource.setMaintenanceInterval(actConfig.getMaintenanceInterval());
                xaDataSource.setMaxIdleTime(actConfig.getMaxIdleTime());
                xaDataSource.setTestQuery(actConfig.getTestQuery());
                return xaDataSource;
            }
        //未使用jta时，flowable使用此事务管理器
        //    @Bean(name = "actTransactionManager")
        //    public DataSourceTransactionManager transactionManager(@Qualifier("actDataSource") DataSource dataSource) {
        //        return new DataSourceTransactionManager(dataSource);
        //    }
        
            //不使用uuid主键生成器
            @Bean
            public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> processEngineDbIdGeneratorConfigurer() {
                return engineConfiguration -> engineConfiguration.setIdGenerator(new DbIdGenerator());
            }
        }
        ```
    2. MyProcessEngineAutoConfiguration  
        ***替换事务管理器为@Qualifier("jta") PlatformTransactionManager platformTransactionManager，否则flowable数据库连接工具找不到事务管理器***
        ```java
        //重写配置类，使flowable使用单独的数据库
        @Configuration
        public class MyProcessEngineAutoConfiguration extends ProcessEngineAutoConfiguration {
        
            public MyProcessEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableAppProperties appProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties) {
                super(flowableProperties, processProperties, appProperties, idmProperties, mailProperties);
            }
        
            @Bean
            @SuppressWarnings("all")
            public SpringProcessEngineConfiguration springProcessEngineConfiguration(@Qualifier("actDataSource") DataSource dataSource, @Qualifier("jta") PlatformTransactionManager platformTransactionManager, @Process ObjectProvider<IdGenerator> processIdGenerator, ObjectProvider<IdGenerator> globalIdGenerator, @ProcessAsync ObjectProvider<AsyncExecutor> asyncExecutorProvider, @ProcessAsyncHistory ObjectProvider<AsyncExecutor> asyncHistoryExecutorProvider) throws IOException {
                SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
                List<Resource> resources = this.discoverDeploymentResources(this.flowableProperties.getProcessDefinitionLocationPrefix(), this.flowableProperties.getProcessDefinitionLocationSuffixes(), this.flowableProperties.isCheckProcessDefinitions());
                if (resources != null && !resources.isEmpty()) {
                    conf.setDeploymentResources(resources.toArray(new Resource[0]));
                    conf.setDeploymentName(this.flowableProperties.getDeploymentName());
                }
        
                AsyncExecutor springAsyncExecutor = asyncExecutorProvider.getIfUnique();
                if (springAsyncExecutor != null) {
                    conf.setAsyncExecutor(springAsyncExecutor);
                }
        
                AsyncExecutor springAsyncHistoryExecutor = asyncHistoryExecutorProvider.getIfUnique();
                if (springAsyncHistoryExecutor != null) {
                    conf.setAsyncHistoryEnabled(true);
                    conf.setAsyncHistoryExecutor(springAsyncHistoryExecutor);
                }
        
                this.configureSpringEngine(conf, platformTransactionManager);
                this.configureEngine(conf, dataSource);
                conf.setDeploymentName(this.defaultText(this.flowableProperties.getDeploymentName(), conf.getDeploymentName()));
                conf.setDisableIdmEngine(!this.flowableProperties.isDbIdentityUsed() || !this.idmProperties.isEnabled());
                conf.setAsyncExecutorActivate(this.flowableProperties.isAsyncExecutorActivate());
                conf.setAsyncHistoryExecutorActivate(this.flowableProperties.isAsyncHistoryExecutorActivate());
                conf.setMailServerHost(this.mailProperties.getHost());
                conf.setMailServerPort(this.mailProperties.getPort());
                conf.setMailServerUsername(this.mailProperties.getUsername());
                conf.setMailServerPassword(this.mailProperties.getPassword());
                conf.setMailServerDefaultFrom(this.mailProperties.getDefaultFrom());
                conf.setMailServerForceTo(this.mailProperties.getForceTo());
                conf.setMailServerUseSSL(this.mailProperties.isUseSsl());
                conf.setMailServerUseTLS(this.mailProperties.isUseTls());
                conf.setEnableProcessDefinitionHistoryLevel(this.processProperties.isEnableProcessDefinitionHistoryLevel());
                conf.setProcessDefinitionCacheLimit(this.processProperties.getDefinitionCacheLimit());
                conf.setEnableSafeBpmnXml(this.processProperties.isEnableSafeXml());
                conf.setHistoryLevel(this.flowableProperties.getHistoryLevel());
                conf.setActivityFontName(this.flowableProperties.getActivityFontName());
                conf.setAnnotationFontName(this.flowableProperties.getAnnotationFontName());
                conf.setLabelFontName(this.flowableProperties.getLabelFontName());
                IdGenerator idGenerator = this.getIfAvailable(processIdGenerator, globalIdGenerator);
                if (idGenerator == null) {
                    idGenerator = new StrongUuidGenerator();
                }
        
                conf.setIdGenerator(idGenerator);
                return conf;
            }
        
        }

        ```