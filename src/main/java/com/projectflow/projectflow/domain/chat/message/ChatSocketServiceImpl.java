package com.projectflow.projectflow.domain.chat.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chat.message.payload.ChatMessage;
import com.projectflow.projectflow.domain.chat.message.payload.PinMessage;
import com.projectflow.projectflow.domain.chat.message.payload.RemovePinMessage;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatSocketServiceImpl implements ChatSocketService {

    private final SocketAuthenticationFacade authenticationFacade;
    private final ChatRepository chatRepository;

    @Override
    public void sendChatMessage(Chat chat, String chatRoomId, User user, SocketIOServer server) {
        server.getRoomOperations(chatRoomId)
                .getClients()
                .forEach(client -> {
                    List<User> receivers = chat.getReceiver();

                    receivers.removeIf(user1 -> server.getAllClients()
                            .stream().map(client1 -> client1.get("userInfo"))
                            .anyMatch(user2 -> user2.equals(user1.getEmail())));

                    List<String> receiverIds = receivers.stream()
                            .map(User::getEmail)
                            .collect(Collectors.toList());

                    ChatMessage message = ChatMessage.builder()
                            .type(chat.getMessageType())
                            .id(chat.getId().toString())
                            .createdAt(chat.getCreatedAt().toString())
                            .readerList(receiverIds)
                            .senderImage(chat.getSender().getProfileImage())
                            .senderName(chat.getSender().getName())
                            .message(chat.getMessage())
                            .isMine(authenticationFacade.getCurrentUser(client).getId().equals(user.getId()))
                            .size(1)
                            .build();
                    client.sendEvent(SocketProperty.MESSAGE_KEY, message);
                });
    }

    @Override
    public void sendPinMessage(String chatRoomId, String chatId, SocketIOServer server) {
        Chat chat = chatRepository.findById(new ObjectId(chatId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        var message = PinMessage.builder()
                .content(chat.getMessage())
                .build();
        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.PIN_KEY, message);
    }

    @Override
    public void sendRemovePinMessage(String chatRoomId, SocketIOServer server) {
        var message = new RemovePinMessage();

        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.REMOVE_KEY, message);
    }
}
