package com.projectflow.projectflow.global.websocket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.UserRepository;
import com.projectflow.projectflow.global.auth.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class SocketAuthenticationFacadeImpl implements SocketAuthenticationFacade {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser(SocketIOClient client) {
        String email = client.get(AuthenticationProperty.USER_KEY);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
