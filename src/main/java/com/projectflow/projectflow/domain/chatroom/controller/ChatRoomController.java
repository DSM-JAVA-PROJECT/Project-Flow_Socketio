package com.projectflow.projectflow.domain.chatroom.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chatroom.message.ChatRoomSocketService;
import com.projectflow.projectflow.domain.chatroom.payload.*;
import com.projectflow.projectflow.domain.chatroom.service.ChatRoomRestService;
import com.projectflow.projectflow.domain.chatroom.service.ChatRoomService;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@SocketController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomRestService roomRestService;
    private final ChatRoomSocketService socketService;
    private final SocketAuthenticationFacade authenticationFacade;

    @SocketMapping(endpoint = "chatroom.create", requestCls = CreateChatRoomRequest.class)
    public void createChatRoom(SocketIOClient client, CreateChatRoomRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        chatRoomService.createChatRoom(request, user);

    }

    @SocketMapping(endpoint = "chatroom.rejoin", requestCls = JoinChatRoomRequest.class)
    public void rejoinChatRoom(JoinChatRoomRequest request, SocketIOClient client, SocketIOServer server) {
        User user = authenticationFacade.getCurrentUser(client);
        socketService.rejoinChatRoom(request.getChatRoomId(), user, client, server);
    }

    @SocketMapping(endpoint = "chatroom.out", requestCls = OutChatRoomRequest.class)
    public void outChatRoom(SocketIOClient client, OutChatRoomRequest request) {
        socketService.outChatRoom(client, request);
    }

    @SocketMapping(endpoint = "chatroom.resign", requestCls = ResignChatRoomRequest.class)
    public void resign(ResignChatRoomRequest request, SocketIOClient client, SocketIOServer server) {
        User user = authenticationFacade.getCurrentUser(client);
        chatRoomService.resignChatRoom(request.getChatRoomId(), user);
        socketService.resignChatRoom(request.getChatRoomId(), user, client, server);
    }

    @SocketMapping(endpoint = "chatroom.participate", requestCls = ParticipateChatRoomRequest.class)
    public void participateChatRoom(ParticipateChatRoomRequest request, SocketIOClient client, SocketIOServer server) {
        User user = authenticationFacade.getCurrentUser(client);
        chatRoomService.joinChatRoom(request, user);
        socketService.joinChatRoom(request.getChatRoomId(), request.getUsers(), client, server);
    }

    @GetMapping("/chatroom/{projectId}/rooms")
    public ChatRoomListResponse getChatRoom(@PathVariable String projectId) {
        return roomRestService.getChatRooms(projectId);
    }

    @GetMapping("/chatroom/{chatRoomId}/members")
    public ChatMemberListResponse getMembers(@PathVariable String chatRoomId) {
        return roomRestService.getChatRoomMember(chatRoomId);
    }

    @PatchMapping("/chatroom/image/{chatRoomId}")
    public void updateChatRoomImage(@PathVariable String chatRoomId,
                                    @RequestBody ChatRoomImageRequest request) {
        roomRestService.updateChatRoomImage(chatRoomId, request.getImageUrl());
    }

    @PatchMapping("/chatroom/name/{chatRoomId}")
    public void updateChatRoomName(@PathVariable String chatRoomId,
                                   @RequestBody ChatRoomNameRequest request) {
        roomRestService.updateChatRoomName(chatRoomId, request);
    }

    @GetMapping("/chatroom/profile/{userId}")
    public ChatMemberProfileResponse userProfileResponse(@PathVariable String userId) {
        return roomRestService.getUserProfile(userId);
    }

}
