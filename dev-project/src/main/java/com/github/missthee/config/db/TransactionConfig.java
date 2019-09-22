//package com.github.missthee.config.db;
//
//import com.atomikos.icatch.jta.UserTransactionImp;
//import com.atomikos.icatch.jta.UserTransactionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//import javax.transaction.TransactionManager;
//import javax.transaction.UserTransaction;
//
//@Configuration
//@EnableTransactionManagement
//public class TransactionConfig {
//    @Bean(name = "userTransaction")
//    public UserTransaction userTransaction() throws Throwable {
//        UserTransactionImp userTransactionImp = new UserTransactionImp();
//        userTransactionImp.setTransactionTimeout(30000);
//        return userTransactionImp;
//    }
//
//    @Bean(name = "atomikosTransactionManager")
//    public TransactionManager atomikosTransactionManager() {
//        UserTransactionManager userTransactionManager = new UserTransactionManager();
//        userTransactionManager.setForceShutdown(false);
//        return userTransactionManager;
//    }
//
//    @Bean(name = "jta")//如果仅配置了一个事务管理器，@Transactional注解不需要设置value值；否则，必须设置value值，指定职务管理器。或在默认的事务管理器加@Primary注解
//    @DependsOn({"userTransaction", "atomikosTransactionManager"})
//    public PlatformTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager atomikosTransactionManager) {
//        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, atomikosTransactionManager);
//        jtaTransactionManager.setDefaultTimeout(30000);
//        return jtaTransactionManager;
//    }
//}
