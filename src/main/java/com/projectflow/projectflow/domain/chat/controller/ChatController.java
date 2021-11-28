package com.projectflow.projectflow.domain.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.message.ChatSocketService;
import com.projectflow.projectflow.domain.chat.payload.*;
import com.projectflow.projectflow.domain.chat.service.ChatService;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SocketController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SocketAuthenticationFacade authenticationFacade;
    private final ChatSocketService socketService;

    @SocketMapping(endpoint = "message", requestCls = ChatRequest.class)
    public void sendMessage(SocketIOClient client, SocketIOServer server, ChatRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Chat chat = chatService.saveMessage(request, user);
        socketService.sendChatMessage(chat, request.getChatRoomId(), user, server);
    }

    @SocketMapping(endpoint = "message.image", requestCls = ImageChatRequest.class)
    public void sendImageMessage(SocketIOClient client, SocketIOServer server, ImageChatRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Chat chat = chatService.saveImageMessage(request, user);
        socketService.sendChatMessage(chat, request.getChatRoomId(), user, server);
    }

    @GetMapping("/chat/{chatRoomId}")
    public OldChatMessageListResponse getOldMessage(@PathVariable String chatRoomId, Pageable pageable) {
        return chatService.getOldChatMessage(chatRoomId, pageable);
    }

    @GetMapping("/pin/{chatRoomId}")
    public PinResponse getPinnedChat(@PathVariable String chatRoomId) {
        return chatService.getPinnedChat(chatRoomId);
    }

    @SocketMapping(endpoint = "/chat/{chatId}", requestCls = ChatPinRequest.class)
    public void pinMessage(SocketIOServer server, SocketIOClient client, ChatPinRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        chatService.pinMessage(request, user);
        socketService.sendPinMessage(request.getChatRoomId(), request.getChatId(), server);
    }
}