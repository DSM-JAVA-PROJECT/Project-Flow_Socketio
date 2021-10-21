package com.projectflow.projectflow.global.auth.facade;

import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.UserRepository;
import com.projectflow.projectflow.global.auth.exceptions.AuthUserNotFoundException;
import com.projectflow.projectflow.global.auth.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(getEmail())
                .orElseThrow(() -> AuthUserNotFoundException.EXCEPTION);
    }

    @Override
    public String getCurrentEmail() {
        return getEmail();
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
