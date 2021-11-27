package com.projectflow.projectflow.domain.plan.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.message.payload.CreatePlanMessage;
import com.projectflow.projectflow.domain.plan.message.payload.JoinPlanMessage;
import com.projectflow.projectflow.domain.plan.message.payload.ResignPlanMessage;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import com.projectflow.projectflow.global.websocket.security.AuthenticationProperty;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlanSocketServiceImpl implements PlanSocketService {

    private final SocketAuthenticationFacade authenticationFacade;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void sendCreatePlanMessage(String chatRoomId, Plan plan, User user, SocketIOServer server, Boolean isForced) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        var message = buildCreatePlanResponse(getReceiverIds(chatRoom, server), plan, user, isForced);
        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.CREATE_PLAN_KEY, message);
    }

    @Override
    public void sendJoinPlanMessage(String chatRoomId, Plan plan, User sender, SocketIOServer server) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        server.getRoomOperations(chatRoomId)
                .getClients()
                .forEach(client -> {
                    User receiver = authenticationFacade.getCurrentUser(client);
                    client.sendEvent(SocketProperty.JOIN_PLAN_KEY, buildJoinPlanMessage(getReceiverIds(chatRoom, server), plan, sender, receiver));
                });
    }

    @Override
    public void sendResignPlanMessage(String chatRoomId, Plan plan, User sender, SocketIOServer server) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        server.getRoomOperations(chatRoomId)
                .getClients()
                .forEach(client -> {
                    User receiver = authenticationFacade.getCurrentUser(client);
                    client.sendEvent(SocketProperty.RESIGN_PLAN_KEY, buildResignPlanMessage(getReceiverIds(chatRoom, server), plan, sender, receiver));
                });
    }

    /**
     * @Param 저장된 Plan
     * plan을 채팅방에 전송할 message로 변환해서 반환해 준다.
     */
    private CreatePlanMessage buildCreatePlanResponse(List<String> userIds, Plan plan, User user, Boolean isForced) {
        return CreatePlanMessage.builder()
                .createdAt(plan.getCreatedAt().toString())
                .planId(plan.getId().toString())
                .planName(plan.getName())
                .endDate(plan.getEndDate().toString())
                .startDate(plan.getStartDate().toString())
                .senderImage(user.getProfileImage())
                .senderName(user.getName())
                .isMine(true)
                .type(isForced ? MessageType.FORCED_PLAN : MessageType.PLAN)
                .readerList(userIds)
                .build();
    }

    /**
     * @Param 일정
     * @Param 일정에 참가한 사용자
     * @Param 일정 참가 메세지를 받을 사용자
     * Plan 객체를 채팅방에 전송할 메세지로 변환해서 반환해 준다.
     */
    private JoinPlanMessage buildJoinPlanMessage(List<String> userIds, Plan plan, User sender, User receiver) {
        return JoinPlanMessage.builder()
                .planId(plan.getId().toString())
                .isMine(sender.equals(receiver))
                .createdAt(plan.getCreatedAt().toString())
                .endDate(plan.getEndDate().toString())
                .planName(plan.getName())
                .startDate(plan.getStartDate().toString())
                .senderImage(sender.getProfileImage())
                .senderName(sender.getName())
                .readerList(userIds)
                .build();
    }

    /**
     * @Param 일정
     * @Param 일정을 탈퇴한 사용자
     * @Param 일정 탈퇴 메세지를 받을 사용자
     * Plan 객체를 채팅방에 전송할 메세지로 변환해서 반환해 준다.
     */
    private ResignPlanMessage buildResignPlanMessage(List<String> userIds, Plan plan, User sender, User receiver) {


        return ResignPlanMessage.builder()
                .planId(plan.getId().toString())
                .isMine(sender.equals(receiver))
                .createdAt(plan.getCreatedAt().toString())
                .endDate(plan.getEndDate().toString())
                .planName(plan.getName())
                .startDate(plan.getStartDate().toString())
                .senderImage(sender.getProfileImage())
                .senderName(sender.getName())
                .readerList(userIds)
                .build();
    }

    private List<String> getReceiverIds(ChatRoom chatRoom, SocketIOServer server) {
        List<User> receivers = chatRoom.getUserIds();

        receivers.removeIf(user1 -> server.getAllClients()
                .stream().map(client1 -> client1.get("userInfo"))
                .anyMatch(user2 -> user2.equals(user1.getEmail())));

        return receivers.stream()
                .map(User::getId).map(ObjectId::toString)
                .collect(Collectors.toList());
    }

}
