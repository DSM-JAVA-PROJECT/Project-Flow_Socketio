package com.projectflow.projectflow.domain.chat.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.user.entity.User;

public interface MessageService {
    void sendChatMessage(Chat chat, User user, SocketIOServer server);
}
