package com.projectflow.projectflow.domain.chatroom.message;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.message.payload.JoinMessage;
import com.projectflow.projectflow.domain.chatroom.message.payload.ResignMessage;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomSocketServiceImpl implements ChatRoomSocketService {
    @Override
    public void joinChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server) {
        client.joinRoom(chatRoomId);

        JoinMessage message = JoinMessage.builder()
                .profileImage(user.getProfileImage())
                .userId(user.getId().toString())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .build();

        server.getRoomOperations(chatRoomId)
                .sendEvent("join", message);
    }

    @Override
    public void rejoinChatRoom(String chatRoomId, SocketIOClient client) {
        client.joinRoom(chatRoomId);
    }

    @Override
    public void resignChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server) {
        client.leaveRoom(chatRoomId);

        ResignMessage message = ResignMessage.builder()
                .profileImage(user.getProfileImage())
                .userId(user.getId().toString())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .build();

        server.getRoomOperations(chatRoomId)
                .sendEvent("resign", message);
    }
}
