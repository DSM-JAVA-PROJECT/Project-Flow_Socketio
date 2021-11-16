package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.payload.ChatMemberListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomNameRequest;

public interface ChatRoomRestService {
    ChatMemberListResponse getChatRoomMember(String chatRoomId);

    ChatRoomListResponse getChatRooms(String projectId);

    void updateChatRoomImage(String chatRoomId, String imageUrl);

    void updateChatRoomName(String chatRoomId, ChatRoomNameRequest request);
}
