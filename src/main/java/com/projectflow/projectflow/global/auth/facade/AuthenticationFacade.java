package com.projectflow.projectflow.global.auth.facade;

import com.projectflow.projectflow.domain.user.entity.User;

public interface AuthenticationFacade {
    User getCurrentUser();
    String getCurrentEmail();
    User getUser(String email);
}
