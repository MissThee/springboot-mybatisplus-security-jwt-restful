package server.config;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.EchoEncoder;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.layout.EchoLayout;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogBackConfig {
    @Bean
    public void RollingFileAppender(){
        RollingFileAppender rollingFileAppender=new RollingFileAppender();
        rollingFileAppender.setFile("log/trace.log");
        TimeBasedRollingPolicy timeBasedRollingPolicy=new TimeBasedRollingPolicy();
        timeBasedRollingPolicy.setFileNamePattern("logs/trace.%d{yyyy-MM-dd}.log");
        rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);
        Layout  eLayout = new EchoLayout();

    }
}
