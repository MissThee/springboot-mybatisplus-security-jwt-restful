package server.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOServerConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8094);
		/*config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
			@Override
			public boolean isAuthorized(HandshakeData data) {
				//http://localhost:8081?username=test&password=test
				//例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息，本文不做身份验证
				// String username = data.getSingleUrlParam("username");
				// String password = data.getSingleUrlParam("password");
				return true;
			}
		});*/

        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
