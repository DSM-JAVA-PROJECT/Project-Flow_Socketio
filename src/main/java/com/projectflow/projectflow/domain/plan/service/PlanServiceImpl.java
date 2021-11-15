package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.exceptions.AlreadyPlanParticipateException;
import com.projectflow.projectflow.domain.plan.exceptions.NotPlanMemberException;
import com.projectflow.projectflow.domain.plan.exceptions.PlanNotFoundException;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.plan.payload.ResignPlanRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    private final CustomPlanRepository planRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Transactional
    @Override
    public Plan createPlan(CreatePlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);

        List<User> users = new ArrayList<>();

        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        if (request.isForced()) {
            users = chatRoom.getUserIds();
        }

        Plan unsavedPlan = buildPlan(request, users);
        Plan plan = planRepository.savePlan(request.getChatRoomId(), unsavedPlan);

        Chat unsavedChat = buildPlanChat(chatRoom, plan, user);
        chatRepository.save(unsavedChat);

        return plan;
    }

    @Override
    @Transactional
    public Plan joinPlan(JoinPlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);
        Plan plan = planRepository.findById(request.getPlanId());
        validateAlreadyPlanParticipated(plan, user);
        Plan updatedPlan = planRepository.joinPlan(request.getPlanId(), user);

        return plan;
    }

    @Transactional
    @Override
    public Plan resignPlan(ResignPlanRequest request, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        Plan plan = chatRoom.getPlans().stream()
                .filter(plan1 -> plan1.getId().toString().equals(request.getPlanId()))
                .findFirst()
                .orElseThrow(() -> PlanNotFoundException.EXCEPTION);

        validatePlanMember(plan, user);

        plan.getPlanUsers().removeIf(planUser -> planUser.getUser().equals(user));
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom.getPlans().stream()
                .filter(plan1 -> plan1.getId().toString().equals(request.getPlanId()))
                .findFirst()
                .orElseThrow(() -> PlanNotFoundException.EXCEPTION);
    }

    private Plan buildPlan(CreatePlanRequest request, List<User> users) {
        return Plan.builder()
                .endDate(LocalDate.parse(request.getPlanEndDate(), FORMATTER))
                .startDate(LocalDate.parse(request.getPlanStartDate(), FORMATTER))
                .name(request.getPlanName())
                .users(users)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Chat buildPlanChat(ChatRoom chatRoom, Plan plan, User user) {
        List<User> receivers = chatRoom.getUserIds();
        CollectionUtils.emptyIfNull(receivers).remove(user);

        return Chat.builder()
                .planId(plan.getId().toString())
                .receiver(receivers)
                .chatRoom(chatRoom)
                .sender(user)
                .messageType(MessageType.PLAN)
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

    private void validateAlreadyPlanParticipated(Plan plan, User user) {
        if (plan.getPlanUsers().isEmpty() ||
                plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user))) {
            throw AlreadyPlanParticipateException.EXCEPTION;
        }
    }

}
