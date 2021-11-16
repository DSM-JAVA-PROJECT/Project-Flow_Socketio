package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotProjectMemberException;
import com.projectflow.projectflow.domain.chatroom.payload.*;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.project.exceptions.ProjectNotFoundException;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.auth.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomRestServiceImpl implements ChatRoomRestService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProjectRepository projectRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public ChatMemberListResponse getChatRoomMember(String chatRoomId) {
        List<User> users = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .map(ChatRoom::getUserIds)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        return new ChatMemberListResponse(users.stream().map(user -> ChatMemberResponse.builder()
                        .id(user.getId().toString())
                        .name(user.getName())
                        .profileImage(user.getProfileImage())
                        .build())
                .collect(Collectors.toList()));
    }

    @Override
    public ChatRoomListResponse getChatRooms(String projectId) {
        Project project = projectRepository.findById(new ObjectId(projectId))
                .orElseThrow(() -> ProjectNotFoundException.EXCEPTION);

        validateProjectMember(project);

        List<ChatRoomResponse> responses = project.getChatRooms()
                .stream().map(chatRoom -> ChatRoomResponse.builder()
                        .chatRoomImage(chatRoom.getProfileImage())
                        .chatRoomName(chatRoom.getName())
                        .id(chatRoom.getId().toString())
                        .build())
                .collect(Collectors.toList());
        return new ChatRoomListResponse(responses);
    }

    @Override
    public void updateChatRoomImage(String chatRoomId, String imageUrl) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        validateChatRoomMember(chatRoom);
        chatRoom.updateChatRoomImage(imageUrl);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void updateChatRoomName(String chatRoomId, ChatRoomNameRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        validateChatRoomMember(chatRoom);
        chatRoom.updateChatRoomName(request.getName());
        chatRoomRepository.save(chatRoom);
    }

    private void validateChatRoomMember(ChatRoom chatRoom) {
        User user = authenticationFacade.getCurrentUser();
        chatRoom.getUserIds()
                .stream().filter(user::equals)
                .findFirst()
                .orElseThrow(() -> NotChatRoomMemberException.EXCEPTION);
    }

    private void validateProjectMember(Project project) {
        User user = authenticationFacade.getCurrentUser();
        if (chatRoomRepository.isNotProjectMember(user, project.getId().toString())) {
            throw NotProjectMemberException.EXCEPTION;
        }
    }

}
