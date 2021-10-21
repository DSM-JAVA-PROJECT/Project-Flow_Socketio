package com.projectflow.projectflow.global.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Component
public class WebSocketConfig {

//    private SocketIOServer server;
    private final WebSocketAddMappingSupporter mappingSupporter;

    @Value("${socket.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setOrigin("*");
        SocketIOServer server = new SocketIOServer(config);
        mappingSupporter.addListeners(server);
        return server;
    }

//    @PreDestroy
//    public void socketStop() {
//        server.stop();
//    }

}