package com.github.missthee.config.https;

//import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    @Value("${server.port}")
    private Integer port;
    @Value("${server.http.port}")
    private Integer httpPort;
    @Value("${server.redirect-https}")
    private Boolean redirectHttps;

//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }

//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(httpPort);//表示用8099端口来供http访问
//        connector.setSecure(false);
//        if (redirectHttps) {
//            connector.setRedirectPort(port);//自动重定向到8098端口https访问
//        }
//        return connector;
//    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addBuilderCustomizers((UndertowBuilderCustomizer) builder -> {
            builder.addHttpListener(httpPort, "0.0.0.0");
        });
    }
}