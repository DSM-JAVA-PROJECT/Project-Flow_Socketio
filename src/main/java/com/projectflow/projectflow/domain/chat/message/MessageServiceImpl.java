package com.projectflow.projectflow.domain.chat.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.message.payload.ChatMessage;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public void sendChatMessage(Chat chat, User user, SocketIOServer server) {
        ChatMessage message = ChatMessage.builder()
                .id(chat.getId().toString())
                .createdAt(chat.getCreatedAt())
                .readerList(chat.getReceiver().stream()
                        .map(User::getId)
                        .map(ObjectId::toString)
                        .toList())
                .senderImage(chat.getSender().getProfileImage())
                .senderName(chat.getSender().getName())
                .message(chat.getMessage())
                .build();

        System.out.println(chat.getChatRoom().getId().toString());
        server.getBroadcastOperations()
                .sendEvent("message", message);
    }
}
