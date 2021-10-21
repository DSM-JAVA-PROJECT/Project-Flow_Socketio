package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;

public interface ChatRoomService {
    void createChatRoom(String projectId, CreateChatRoomRequest request);
    String joinChatRoom(String chatRoomId);
    void resignChatRoom(String chatRoomId);
    ChatRoomListResponse getChatRooms(String projectId);
}
