package com.projectflow.projectflow.global.security.httpsecurity;

import com.projectflow.projectflow.domain.user.entity.UserRepository;
import com.projectflow.projectflow.global.auth.exceptions.AuthUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(AuthDetails::new)
                .orElseThrow(() -> {
                    throw AuthUserNotFoundException.EXCEPTION;
                });
    }
}
