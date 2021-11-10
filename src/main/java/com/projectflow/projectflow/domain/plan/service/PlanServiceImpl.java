package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.exceptions.NotPlanMemberException;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.plan.payload.ResignPlanRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {

    private final CustomPlanRepository planRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    @Override
    public Plan createPlan(CreatePlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);

        List<User> users = new ArrayList<>();

        if (request.isForced()) {
            ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                    .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
            users = chatRoom.getUserIds();
        }

        Plan unsavedPlan = buildPlan(request, users);

        return planRepository.savePlan(request.getChatRoomId(), unsavedPlan);
    }

    @Override
    public Plan joinPlan(JoinPlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);
        return planRepository.joinPlan(request.getPlanId(), user);
    }

    @Transactional
    @Override
    public Plan resignPlan(ResignPlanRequest request, User user) {
        Plan plan = planRepository.findById(request.getPlanId());
        plan.getPlanUsers().removeIf(planUser -> planUser.getUser().equals(user));
        return plan;
    }

    private Plan buildPlan(CreatePlanRequest request, List<User> users) {
        return Plan.builder()
                .endDate(request.getPlanEndDate())
                .startDate(request.getPlanStartDate())
                .name(request.getPlanName())
                .users(users)
                .build();
    }

    private void validatePlanMember(Plan plan, User user) {
        if (plan.getPlanUsers().stream().noneMatch(planUser -> planUser.getUser().equals(user))) {
            throw NotPlanMemberException.EXCEPTION;
        }
    }

    private void validateChatRoomMember(String chatRoomId, User user) {
        if (!chatRoomRepository.isChatRoomMember(chatRoomId, user)) {
            throw NotChatRoomMemberException.EXCEPTION;
        }
    }

}
