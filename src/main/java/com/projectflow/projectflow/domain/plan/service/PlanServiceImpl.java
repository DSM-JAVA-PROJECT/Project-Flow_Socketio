package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.entity.PlanUser;
import com.projectflow.projectflow.domain.plan.exceptions.AlreadyPlanParticipateException;
import com.projectflow.projectflow.domain.plan.exceptions.NotPlanMemberException;
import com.projectflow.projectflow.domain.plan.exceptions.PlanNotFoundException;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.plan.payload.ResignPlanRequest;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.facade.UserFacade;
import com.projectflow.projectflow.global.fcm.FcmFacade;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy??? MM??? dd???");
    private final CustomPlanRepository planRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserFacade userFacade;
    private final FcmFacade fcmFacade;
    private final MongoTemplate mongoTemplate;

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

        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), request.getPlanName() + " ????????? ??????????????????.", MessageType.PLAN, user.getProfileImage());

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
        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), plan.getName() + " ????????? ??????????????????.", MessageType.JOIN_PLAN, user.getProfileImage());

        return planRepository.joinPlan(request.getPlanId(), user);
    }

    @Transactional
    @Override
    public Plan resignPlan(ResignPlanRequest request, User user) {

        ChatRoom chatRoom = mongoTemplate.findOne(query(where("plans.id").is(request.getPlanId())),
                ChatRoom.class);
        assert chatRoom != null;

        Plan plan = chatRoom.getPlans().stream()
                .filter(plan1 -> plan1.getId().toString().equals(request.getPlanId()))
                .findFirst()
                .orElseThrow(() -> PlanNotFoundException.EXCEPTION);

        validatePlanMember(plan, user);

        plan.getPlanUsers().removeIf(planUser -> planUser.getUser().equals(user));
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        Chat unsavedChat = buildResignPlanChat(chatRoom, plan, user);
        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), plan.getName() + " ????????? ??????????????????.", MessageType.RESIGN_PLAN, user.getProfileImage());
        chatRepository.save(unsavedChat);

        return savedChatRoom.getPlans().stream()
                .filter(plan1 -> plan1.getId().toString().equals(request.getPlanId()))
                .findFirst()
                .orElseThrow(() -> PlanNotFoundException.EXCEPTION);
    }

    @Override
    public void removePlan(String planId) {
        User user = userFacade.getCurrentUser();
        Plan plan = planRepository.findById(planId);

        plan.getPlanUsers().stream()
                .map(PlanUser::getUser)
                .filter(user::equals)
                .findFirst()
                .orElseThrow(() -> NotPlanMemberException.EXCEPTION);

        planRepository.removePlan(planId);
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
