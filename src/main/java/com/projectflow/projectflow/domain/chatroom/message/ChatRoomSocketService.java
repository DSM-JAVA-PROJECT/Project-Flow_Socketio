package com.projectflow.projectflow.domain.chatroom.message;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.user.entity.User;

public interface ChatRoomSocketService {
    void joinChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server);

    void rejoinChatRoom(String chatRoomId, SocketIOClient client);

    void resignChatRoom(String chatRoomId,User user, SocketIOClient client, SocketIOServer server);
}
