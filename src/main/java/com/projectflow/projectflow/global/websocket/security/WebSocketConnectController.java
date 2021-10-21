package com.projectflow.projectflow.global.websocket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.projectflow.projectflow.global.security.httpsecurity.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebSocketConnectController {

    private final JwtTokenValidator validator;

    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getHttpHeaders().get("Authorization");
        Authentication authentication = validator.createAuthentication(token);
        client.set("userInfo", authentication.getName());
    }
}
