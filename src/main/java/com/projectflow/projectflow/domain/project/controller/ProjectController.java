package com.projectflow.projectflow.domain.project.controller;

import com.projectflow.projectflow.domain.project.payload.ProjectMemberListResponse;
import com.projectflow.projectflow.domain.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final MemberService memberService;

    @GetMapping("/{projectId}/member")
    public ProjectMemberListResponse getProjectMember(@PathVariable String projectId) {
        return memberService.getMemberList(projectId);
    }

    @GetMapping("/{projectId}/member/{chatRoomId}")
    public ProjectMemberListResponse getProjectMember(@PathVariable String projectId,
                                                      @PathVariable String chatRoomId) {
        return memberService.getNotParticipatedMemberList(projectId, chatRoomId);
    }
}
