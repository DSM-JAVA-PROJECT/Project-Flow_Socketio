package com.projectflow.projectflow.domain.chat.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.message.payload.ChatMessage;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatSocketServiceImpl implements ChatSocketService {

    private final SocketAuthenticationFacade authenticationFacade;

    @Override
    public void sendChatMessage(Chat chat, String chatRoomId, User user, SocketIOServer server) {
        server.getRoomOperations(chatRoomId)
                .getClients()
                .forEach(client -> {
                    ChatMessage message = ChatMessage.builder()
                            .id(chat.getId().toString())
                            .createdAt(chat.getCreatedAt().toString())
                            .readerList(chat.getReceiver().stream()
                                    .map(User::getId)
                                    .map(ObjectId::toString)
                                    .toList())
                            .senderImage(chat.getSender().getProfileImage())
                            .senderName(chat.getSender().getName())
                            .message(chat.getMessage())
                            .isMine(authenticationFacade.getCurrentUser(client).getId().equals(user.getId()))
                            .build();
                    client.sendEvent(SocketProperty.MESSAGE_KEY, message);
                });
    }
}
