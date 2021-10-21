package com.projectflow.projectflow.domain.chatroom.entity;

import com.projectflow.projectflow.domain.user.entity.User;

public interface CustomChatRoomRepository {

    boolean isProjectMember(User user, String projectId);

    String joinChatRoom(String chatRoomId, User users);

    boolean isChatRoomMember(String chatRoomId, User user);

    void deleteMember(String chatRoomId, User user);

}
