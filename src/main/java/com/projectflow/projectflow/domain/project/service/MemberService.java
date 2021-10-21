package com.projectflow.projectflow.domain.project.service;

import com.projectflow.projectflow.domain.project.payload.ProjectMemberListResponse;

public interface MemberService {
    ProjectMemberListResponse getMemberList(String projectId);

    ProjectMemberListResponse getNotParticipatedMemberList(String projectId, String chatRoomId);
}
