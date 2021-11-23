package com.projectflow.projectflow.domain.chatroom.message;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chatroom.message.payload.JoinMessage;
import com.projectflow.projectflow.domain.chatroom.message.payload.RejoinMessage;
import com.projectflow.projectflow.domain.chatroom.message.payload.ResignMessage;
import com.projectflow.projectflow.domain.chatroom.payload.OutChatRoomRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.facade.UserFacade;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomSocketServiceImpl implements ChatRoomSocketService {

    private final UserFacade userFacade;

    @Override
    public void joinChatRoom(String chatRoomId, String userId, SocketIOClient client, SocketIOServer server) {
        User user = userFacade.getUserById(userId);
        client.joinRoom(chatRoomId);

        JoinMessage message = JoinMessage.builder()
                .profileImage(user.getProfileImage())
                .userId(user.getId().toString())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .build();

        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.JOIN_KEY, message);
    }

    @Override
    public void rejoinChatRoom(String chatRoomId, User user, SocketIOClient client, SocketIOServer server) {
        RejoinMessage message = RejoinMessage.builder()
                .userEmail(user.getEmail())
                .build();
        client.joinRoom(chatRoomId);

        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.REJOIN_KEY, message);
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
                .sendEvent(SocketProperty.RESIGN_KEY, message);
    }

    @Override
    public void outChatRoom(SocketIOClient client, OutChatRoomRequest request) {
        client.leaveRoom(request.getChatRoomId());
    }
}
