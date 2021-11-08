package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.payload.ChatMemberListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomListResponse;

public interface ChatRoomRestService {
    ChatMemberListResponse getChatRoomMember(String chatRoomId);

    ChatRoomListResponse getChatRooms(String projectId);
}
