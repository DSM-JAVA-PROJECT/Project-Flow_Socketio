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
import com.projectflow.projectflow.global.fcm.FcmFacade;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
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
    private final FcmFacade fcmFacade;

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
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), chatRoom.getName() + " 채팅방에 참여했습니다.", MessageType.JOIN_CHATROOM, user.getProfileImage());
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
