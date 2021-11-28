package com.projectflow.projectflow.domain.chatroom.message;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chatroom.payload.OutChatRoomRequest;
import com.projectflow.projectflow.domain.user.entity.User;

import java.util.List;

public interface ChatRoomSocketService {
    void joinChatRoom(String chatRoomId, List<String> userId, SocketIOClient client, SocketIOServer server);

    void rejoinChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server);

    void resignChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server);

    void outChatRoom(SocketIOClient client, OutChatRoomRequest request);
}
