//package com.github.common.config.db;
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
//    @Bean(name = "jta")
//    @DependsOn({"userTransaction", "atomikosTransactionManager"})
//    public PlatformTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager atomikosTransactionManager)  {
//        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
//    }
//}
