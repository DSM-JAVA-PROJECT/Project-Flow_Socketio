package com.projectflow.projectflow.global.fcm;

import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.enums.MessageType;

import java.util.List;

public interface FcmFacade {
    void sendFcmMessageOnSocket(User sender, List<User> users, String title, String content, MessageType type, String profileImage);
}
