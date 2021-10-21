package com.projectflow.projectflow.global.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.global.websocket.security.WebSocketConnectController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebSocketConfig {

    private final WebSocketAddMappingSupporter mappingSupporter;
    private final WebSocketConnectController connectController;

    @Value("${socket.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setOrigin("*");
        SocketIOServer server = new SocketIOServer(config);
        mappingSupporter.addListeners(server);
        server.addConnectListener(connectController::onConnect);
        return server;
    }

}