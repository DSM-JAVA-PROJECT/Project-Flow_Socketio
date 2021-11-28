package com.projectflow.projectflow.domain.chatroom.entity;

import com.projectflow.projectflow.domain.user.entity.User;

import java.util.List;

public interface CustomChatRoomRepository {

    boolean isNotProjectMember(User user, String projectId);

    void joinChatRoom(String chatRoomId, User users);

    boolean isChatRoomMember(String chatRoomId, User user);

    void deleteMember(String chatRoomId, User user);

    List<ChatRoom> findChatRoomList(String projectId, User user);

    void setPinChat(String chatRoomId, String chatId);

}
