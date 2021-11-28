package com.projectflow.projectflow.domain.user.entity.facade;

import com.projectflow.projectflow.domain.user.entity.User;

import java.util.List;

public interface UserFacade {
    User getUserById(String id);

    User getCurrentUser();

    List<User> getUserList(List<String> emails);
}
