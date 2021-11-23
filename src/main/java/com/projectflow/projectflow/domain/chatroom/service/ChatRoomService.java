package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;
import com.projectflow.projectflow.domain.chatroom.payload.ParticipateChatRoomRequest;
import com.projectflow.projectflow.domain.user.entity.User;

public interface ChatRoomService {
    void createChatRoom(CreateChatRoomRequest request, User user);

    void joinChatRoom(ParticipateChatRoomRequest request);

    void resignChatRoom(String chatRoomId, User user);
}
