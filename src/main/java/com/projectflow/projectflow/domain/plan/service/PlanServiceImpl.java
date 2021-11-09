package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {

    private final CustomPlanRepository planRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Plan createPlan(CreatePlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);
        Plan unsavedPlan = buildPlan(request);
        return planRepository.savePlan(request.getChatRoomId(), unsavedPlan);
    }

    @Override
    public void joinPlan(JoinPlanRequest request, User user) {
        planRepository.joinPlan(request.getPlanId(), user);
    }

    private Plan buildPlan(CreatePlanRequest request) {
        return Plan.builder()
                .endDate(request.getPlanEndDate())
                .startDate(request.getPlanStartDate())
                .name(request.getPlanName())
                .build();
    }

    private void validateChatRoomMember(String chatRoomId, User user) {
        if (!chatRoomRepository.isChatRoomMember(chatRoomId, user)) {
            throw NotChatRoomMemberException.EXCEPTION;
        }
    }

}
