package com.projectflow.projectflow.domain.user.entity.facade;

import com.projectflow.projectflow.domain.user.entity.User;

public interface UserFacade {
    User getUserById(String id);

    User getCurrentUser();
}
