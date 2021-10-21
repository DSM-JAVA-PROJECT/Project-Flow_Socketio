package com.projectflow.projectflow.domain.chat.controller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.chat.service.ChatService;
import com.projectflow.projectflow.global.websocket.annotations.Payload;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import lombok.RequiredArgsConstructor;

@SocketController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @SocketMapping(endpoint = "message")
    public void sendMessage(SocketIOClient client, SocketIOServer server, @Payload ChatRequest request) {
        String chatRoomId = client.getHandshakeData().getSingleUrlParam("id");
        Chat savedChat = chatService.saveMessage(chatRoomId, request);
        server.getRoomOperations(chatRoomId);
    }

}
