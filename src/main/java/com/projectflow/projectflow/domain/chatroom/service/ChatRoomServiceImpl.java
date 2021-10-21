package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotProjectMemberException;
import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomListResponse;
import com.projectflow.projectflow.domain.chatroom.payload.ChatRoomResponse;
import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.project.exceptions.ProjectNotFoundException;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.auth.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProjectRepository projectRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    @Override
    public void createChatRoom(String projectId, CreateChatRoomRequest request) {
        User user = authenticationFacade.getCurrentUser();
        validateProjectMember(projectId, user);

        Project project = projectRepository.findById(new ObjectId(projectId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        ChatRoom unsavedChatRoom = buildChatRoom(request, user, project.getLogoImage());
        System.out.println("sdsfadf");
        ChatRoom chatRoom = chatRoomRepository.save(unsavedChatRoom);

        project.getChatRooms().add(chatRoom);
        projectRepository.save(project);
    }

    @Override
    public String joinChatRoom(String chatRoomId) {
        User user = authenticationFacade.getCurrentUser();
        validateNotChatRoomMember(chatRoomId, user);
        return chatRoomRepository.joinChatRoom(chatRoomId, user);
    }

    @Override
    public void resignChatRoom(String chatRoomId) {
        User user = authenticationFacade.getCurrentUser();
        validateChatRoomMember(chatRoomId, user);
        chatRoomRepository.deleteMember(chatRoomId, user);
    }

    @Override
    public ChatRoomListResponse getChatRooms(String projectId) {
        Project project = projectRepository.findById(new ObjectId(projectId))
                .orElseThrow(() -> ProjectNotFoundException.EXCEPTION);

        List<ChatRoomResponse> responses = project.getChatRooms()
                .stream().map(chatRoom -> ChatRoomResponse.builder()
                        .chatRoomImage(chatRoom.getProfileImage())
                        .chatRoomName(chatRoom.getName())
                        .id(chatRoom.getId().toString())
                        .build())
                .collect(Collectors.toList());
        return new ChatRoomListResponse(responses);
    }

    private ChatRoom buildChatRoom(CreateChatRoomRequest request, User authUser, String image) {
        List<User> users = request.getEmails().stream()
                .map(authenticationFacade::getUser)
                .collect(Collectors.toList());
        users.add(authUser);

        return ChatRoom.builder()
                .name(authUser.getName() + "님의 채팅방")
                .userIds(users)
                .profileImage(image)
                .build();
    }

    private void validateChatRoomMember(String chatRoomId, User user) {
        if (!chatRoomRepository.isChatRoomMember(chatRoomId, user)) {
            throw NotChatRoomMemberException.EXCEPTION;
        }
    }

    private void validateNotChatRoomMember(String chatRoomId, User user) {
        if (chatRoomRepository.isChatRoomMember(chatRoomId, user)) {
            throw NotChatRoomMemberException.EXCEPTION;
        }
    }

    private void validateProjectMember(String projectId, User user) {
        if (!chatRoomRepository.isProjectMember(user, projectId)) {
            throw NotProjectMemberException.EXCEPTION;
        }
    }

}
