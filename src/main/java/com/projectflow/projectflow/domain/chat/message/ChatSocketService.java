package com.projectflow.projectflow.domain.chat.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.user.entity.User;

public interface ChatSocketService {
    void sendChatMessage(Chat chat, String chatRoomId, User user, SocketIOServer server);
}
