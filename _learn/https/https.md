1. 生成证书
    ```
    keytool -keystore [自定义名字].jks -genkey -alias tomcat -keyalg RSA
    keytool -genkey -alias tomcat -keyalg RSA -keystore ./[自定义名字].keystore
    ```
2. .keystore或.jks拷贝到项目resources目录下
3. 配置application.properties
    ```properties
    #https设置
    server.ssl.enabled=true
    server.ssl.key-store=classpath:[自定义名字].jks
    server.ssl.key-store-password=
    server.ssl.key-password=
    server.ssl.protocol=TLSv1.2
   
    #自定义配置
    server.port=8098
    server.http.port=8099
    server.redirect-https=false
    ```
4. 配置Bean（第一种）
    ```java
    @Configuration
    public class HttpsConfig {
        @Value("${server.port}")//原配置端口变为https访问
        private Integer port;
        @Value("${server.http.port}")//http访问端口，另行设置
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
    配置Bean（第二种）
    ```java
   @Configuration
   public class HttpsConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
       //  原端口变为https
   
       @Value("${server.http.port}")
       private Integer httpPort;//。增加一个http端口
   
       @Override
       public void customize(UndertowServletWebServerFactory factory) {
           factory.addBuilderCustomizers(builder -> {
               builder.addHttpListener(httpPort, "0.0.0.0");
           });
       }
   }
    ```