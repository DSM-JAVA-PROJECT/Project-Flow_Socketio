package com.projectflow.projectflow.domain.user.entity.facade;

import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.UserRepository;
import com.projectflow.projectflow.global.auth.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacadeImpl implements UserFacade {

    private final UserRepository userRepository;

    @Override
    public User getUserById(String id) {
        return userRepository.findById(new ObjectId(id))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
