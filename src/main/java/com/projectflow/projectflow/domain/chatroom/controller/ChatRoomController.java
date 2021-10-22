package com.projectflow.projectflow.domain.chatroom.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;
import com.projectflow.projectflow.domain.chatroom.service.ChatRoomService;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SocketController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final SocketAuthenticationFacade authenticationFacade;

    @SocketMapping(endpoint = "chatroom.create", requestCls = CreateChatRoomRequest.class)
    public void createChatRoom(SocketIOClient client, SocketIOServer server, CreateChatRoomRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        chatRoomService.createChatRoom(request, user);

    }

//    @MessageMapping("/join/chatroom/{chatRoomId}")
//    public int joinChatRoom(@DestinationVariable String chatRoomId) {
//        String roomId = chatRoomService.joinChatRoom(chatRoomId);
//        messageService.sendJoinMessage(roomId);
//        return 200;
//    }
//    @MessageMapping("/resign/chatroom/{chatRoomId}")
//    public int resign(@DestinationVariable String chatRoomId) {
//        chatRoomService.resignChatRoom(chatRoomId);
//        messageService.sendResignMessage(chatRoomId);
//        return 200;
//    }

}
