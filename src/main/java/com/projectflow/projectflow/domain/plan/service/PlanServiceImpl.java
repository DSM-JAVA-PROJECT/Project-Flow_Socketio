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
import com.projectflow.projectflow.global.fcm.FcmFacade;
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
    private final FcmFacade fcmFacade;

    @Transactional
    @Override
    public Plan createPlan(CreatePlanRequest request, User user) {
        List<User> users = new ArrayList<>();

        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        validateChatRoomMember(chatRoom, user);

        if (request.getIsForced()) {
            users = chatRoom.getUserIds();
        }

        Plan unsavedPlan = buildPlan(request, users);
        Plan plan = planRepository.savePlan(request.getChatRoomId(), unsavedPlan);

        Chat unsavedChat = buildCreatePlanChat(chatRoom, plan, user);
        chatRepository.save(unsavedChat);

        fcmFacade.sendFcmMessage(chatRoom.getUserIds(), user.getName(), request.getPlanName() + " 일정을 추가했습니다.", MessageType.PLAN,  user.getProfileImage());

        return plan;
    }

    @Override
    @Transactional
    public Plan joinPlan(JoinPlanRequest request, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        validateChatRoomMember(chatRoom, user);
        Plan plan = planRepository.findById(request.getPlanId());
        validateAlreadyPlanParticipated(plan, user);

        Chat unsavedChat = buildJoinPlanChat(chatRoom, plan, user);
        chatRepository.save(unsavedChat);
        fcmFacade.sendFcmMessage(chatRoom.getUserIds(), user.getName(), plan.getName() + " 일정에 참여했습니다.", MessageType.JOIN_PLAN,  user.getProfileImage());

        return planRepository.joinPlan(request.getPlanId(), user);
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

        Chat unsavedChat = buildResignPlanChat(chatRoom, plan, user);
        fcmFacade.sendFcmMessage(chatRoom.getUserIds(), user.getName(), plan.getName() + " 일정에 탈퇴했습니다.", MessageType.RESIGN_PLAN,  user.getProfileImage());
        chatRepository.save(unsavedChat);

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

    private Chat buildCreatePlanChat(ChatRoom chatRoom, Plan plan, User user) {
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

    private Chat buildJoinPlanChat(ChatRoom chatRoom, Plan plan, User user) {
        List<User> receivers = chatRoom.getUserIds();
        CollectionUtils.emptyIfNull(receivers).remove(user);

        return Chat.builder()
                .planId(plan.getId().toString())
                .receiver(receivers)
                .chatRoom(chatRoom)
                .sender(user)
                .messageType(MessageType.JOIN_PLAN)
                .build();
    }

    private Chat buildResignPlanChat(ChatRoom chatRoom, Plan plan, User user) {
        List<User> receivers = chatRoom.getUserIds();
        CollectionUtils.emptyIfNull(receivers).remove(user);

        return Chat.builder()
                .planId(plan.getId().toString())
                .receiver(receivers)
                .chatRoom(chatRoom)
                .sender(user)
                .messageType(MessageType.RESIGN_PLAN)
                .build();
    }

    private void validatePlanMember(Plan plan, User user) {
        if (plan.getPlanUsers().stream().noneMatch(planUser -> planUser.getUser().equals(user))) {
            throw NotPlanMemberException.EXCEPTION;
        }
    }

    private void validateChatRoomMember(ChatRoom chatRoom, User user) {
        chatRoom.getUserIds()
                .stream().filter(user1 -> user1.equals(user))
                .findFirst()
                .orElseThrow(() -> NotChatRoomMemberException.EXCEPTION);
    }

    private void validateAlreadyPlanParticipated(Plan plan, User user) {
        if (!plan.getPlanUsers().isEmpty() &&
                plan.getPlanUsers().stream().anyMatch(planUser -> planUser.getUser().equals(user))) {
            throw AlreadyPlanParticipateException.EXCEPTION;
        }
    }

}
