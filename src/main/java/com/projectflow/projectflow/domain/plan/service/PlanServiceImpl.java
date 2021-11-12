package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chat.entity.enums.ChatType;
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
    public Plan joinPlan(JoinPlanRequest request, User user) {
        validateChatRoomMember(request.getChatRoomId(), user);
        return planRepository.joinPlan(request.getPlanId(), user);
    }

    @Transactional
    @Override
    public Plan resignPlan(ResignPlanRequest request, User user) {
        Plan plan = planRepository.findById(request.getPlanId());
        validatePlanMember(plan, user);
        plan.getPlanUsers().removeIf(planUser -> planUser.getUser().equals(user));
        return plan;
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
                .plan(plan)
                .receiver(receivers)
                .chatRoom(chatRoom)
                .sender(user)
                .chatType(ChatType.PLAN)
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
