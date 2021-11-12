package com.projectflow.projectflow.domain.chatroom.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotProjectMemberException;
import com.projectflow.projectflow.domain.chatroom.payload.CreateChatRoomRequest;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.facade.UserFacade;
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
    private final UserFacade userFacade;

    @Transactional
    @Override
    public void createChatRoom(CreateChatRoomRequest request, User user) {
        validateProjectMember(request.getProjectId(), user);

        Project project = projectRepository.findById(new ObjectId(request.getProjectId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        ChatRoom unsavedChatRoom = buildChatRoom(request, user, project.getLogoImage());
        ChatRoom chatRoom = chatRoomRepository.save(unsavedChatRoom);

        project.getChatRooms().add(chatRoom);
        projectRepository.save(project);
    }

    @Override
    public void joinChatRoom(String chatRoomId, User user) {
        validateNotChatRoomMember(chatRoomId, user);
        chatRoomRepository.joinChatRoom(chatRoomId, user);
    }

    @Override
    public void resignChatRoom(String chatRoomId, User user) {
        validateChatRoomMember(chatRoomId, user);
        chatRoomRepository.deleteMember(chatRoomId, user);
    }

    private ChatRoom buildChatRoom(CreateChatRoomRequest request, User authUser, String image) {
        List<User> users = request.getUserIds().stream()
                .map(userFacade::getUserById)
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
        if (chatRoomRepository.isNotProjectMember(user, projectId)) {
            throw NotProjectMemberException.EXCEPTION;
        }
    }

}
