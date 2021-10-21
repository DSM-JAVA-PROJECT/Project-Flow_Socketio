package com.projectflow.projectflow.global.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Component
public class WebSocketConfig {

    private SocketIOServer server;

    @Value("${socket.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setOrigin("*");
        SocketIOServer server = new SocketIOServer(config);
        server.start();

        this.server = server;
        return server;
    }

    @PreDestroy
    public void socketStop() {
        server.stop();
    }

}