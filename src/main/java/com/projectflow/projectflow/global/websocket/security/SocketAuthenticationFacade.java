package com.projectflow.projectflow.global.websocket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.projectflow.projectflow.domain.user.entity.User;

public interface SocketAuthenticationFacade {
    User getCurrentUser(SocketIOClient client);
}
