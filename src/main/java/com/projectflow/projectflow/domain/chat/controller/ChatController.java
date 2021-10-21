package com.projectflow.projectflow.domain.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.chat.service.ChatService;
import com.projectflow.projectflow.global.websocket.annotations.Payload;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SocketIOServer server;

    @SocketMapping(endpoint = "message")
    public int sendMessage(@Payload ChatRequest request, SocketIOClient client) {
        String chatRoomId = client.getHandshakeData().getSingleUrlParam("id");
        Chat savedChat = chatService.saveMessage(chatRoomId, request);
//        server.getRoomOperations(chatRoomId).send;
        return 201;
    }

}
