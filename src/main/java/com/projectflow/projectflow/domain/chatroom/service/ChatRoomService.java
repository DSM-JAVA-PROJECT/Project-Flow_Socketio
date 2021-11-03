package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;
import com.projectflow.projectflow.domain.user.entity.User;

public interface ChatRoomService {
    void createChatRoom(CreateChatRoomRequest request, User user);
    void joinChatRoom(String chatRoomId, User user);
    void resignChatRoom(String chatRoomId, User user);
}
