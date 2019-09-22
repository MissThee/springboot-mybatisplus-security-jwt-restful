1. 生成证书
    ```
    keytool -keystore [自定义名字].jks -genkey -alias tomcat -keyalg RSA
    keytool -genkey -alias tomcat -keyalg RSA -keystore ./[自定义名字].keystore
    ```
2. .keystore或.jks拷贝到项目resources目录下
3. 配置application.properties
    ```properties
    server.ssl.key-store= classpath:[自定义名字].jks
    server.ssl.key-store-password= [密码]
    server.ssl.key-password= [密码]
    #自定义配置
    server.http.port=8098
    server.redirect-https=false
    ```
4. 配置Bean
    ```java
    @Configuration
    public class HttpsConfig {
        @Value("${server.port}")
        private Integer port;
        @Value("${server.http.port}")
        private Integer httpPort;
        @Value("${server.redirect-https}")
        private Boolean redirectHttps;
    
        @Bean
        public TomcatServletWebServerFactory servletContainer() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
            tomcat.addAdditionalTomcatConnectors(httpConnector());
            return tomcat;
        }
    
        @Bean
        public Connector httpConnector() {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(httpPort);//表示用8099端口来供http访问
            connector.setSecure(false);
            if (redirectHttps) {
                connector.setRedirectPort(port);//自动重定向到8098端口https访问
            }
            return connector;
        }
    }
    ```