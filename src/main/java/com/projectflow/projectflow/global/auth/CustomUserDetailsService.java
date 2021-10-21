package com.projectflow.projectflow.global.auth;

import com.projectflow.projectflow.domain.user.entity.UserRepository;
import com.projectflow.projectflow.global.auth.exceptions.AuthUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> AuthUserNotFoundException.EXCEPTION);
    }
}
