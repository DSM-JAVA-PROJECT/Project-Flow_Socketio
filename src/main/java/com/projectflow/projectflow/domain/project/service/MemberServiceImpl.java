package com.projectflow.projectflow.domain.project.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.project.entity.ProjectUser;
import com.projectflow.projectflow.domain.project.exceptions.ProjectNotFoundException;
import com.projectflow.projectflow.domain.project.payload.ProjectMemberListResponse;
import com.projectflow.projectflow.domain.project.payload.ProjectMemberResponse;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.auth.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final ProjectRepository projectRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public ProjectMemberListResponse getMemberList(String projectId) {
        Project project = getProject(projectId);

        return new ProjectMemberListResponse(project.getProjectUsers()
                .stream()
                .map(ProjectUser::getUser)
                .filter(user -> !user.getEmail().equals(authenticationFacade.getCurrentEmail()))
                .map(this::buildMemberResponse)
                .collect(Collectors.toList()));
    }

    @Override
    public ProjectMemberListResponse getNotParticipatedMemberList(String projectId, String chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Project project = getProject(projectId);
        return new ProjectMemberListResponse(chatRoom.getUserIds()
                .stream()
                .filter(projectUser -> project.getProjectUsers().stream()
                        .noneMatch(member -> member.getUser().getEmail()
                                .equals(projectUser.getEmail())))
                .map(this::buildMemberResponse)
                .collect(Collectors.toList()));
    }

    private ChatRoom getChatRoom(String chatRoomId) {
        return chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private ProjectMemberResponse buildMemberResponse(User user) {
        return ProjectMemberResponse.builder()
                .name(user.getName())
                .id(user.getId().toString())
                .profileImage(user.getProfileImage())
                .build();
    }

    private Project getProject(String projectId) {
        return projectRepository.findById(new ObjectId(projectId))
                .orElseThrow(() -> ProjectNotFoundException.EXCEPTION);
    }

}
