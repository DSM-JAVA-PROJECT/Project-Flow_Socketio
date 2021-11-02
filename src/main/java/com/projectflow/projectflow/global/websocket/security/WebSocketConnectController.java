package com.projectflow.projectflow.global.websocket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.projectflow.projectflow.global.security.exceptions.JwtExpiredException;
import com.projectflow.projectflow.global.security.exceptions.JwtValidatingFailedException;
import com.projectflow.projectflow.global.security.httpsecurity.JwtTokenValidator;
import com.projectflow.projectflow.global.websocket.security.exceptions.SocketJwtExpiredException;
import com.projectflow.projectflow.global.websocket.security.exceptions.SocketJwtValidatingFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebSocketConnectController {

    private final JwtTokenValidator validator;

    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("Authorization");

        try {
            Authentication authentication = validator.createAuthentication(token);
            client.set("userInfo", authentication.getName());
        } catch (JwtValidatingFailedException e) {
            throw SocketJwtValidatingFailedException.EXCEPTION;
        } catch (JwtExpiredException e) {
            throw SocketJwtExpiredException.EXCEPTION;
        }

    }
}
