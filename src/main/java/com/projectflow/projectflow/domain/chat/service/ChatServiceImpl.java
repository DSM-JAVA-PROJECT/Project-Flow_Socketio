package com.projectflow.projectflow.domain.chat.service;

import com.mongodb.DBRef;
import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chat.exceptions.UserNotMessageOwnerException;
import com.projectflow.projectflow.domain.chat.payload.*;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.auth.facade.AuthenticationFacade;
import com.projectflow.projectflow.global.fcm.FcmFacade;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthenticationFacade authenticationFacade;
    private final CustomPlanRepository customPlanRepository;
    private final FcmFacade fcmFacade;
    private final MongoTemplate mongoTemplate;

    @Override
    public Chat saveMessage(ChatRequest request, User user) {
        validateChatRoom(request.getChatRoomId(), user);
        ChatRoom chatRoom = findChatRoomById(request.getChatRoomId());
        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), request.getMessage(), MessageType.MESSAGE, user.getProfileImage());
        return chatRepository.save(buildChat(MessageType.MESSAGE, chatRoom, user, request.getMessage()));
    }

    @Override
    public Chat saveImageMessage(ImageChatRequest request, User user) {
        validateChatRoom(request.getChatRoomId(), user);
        ChatRoom chatRoom = findChatRoomById(request.getChatRoomId());
        fcmFacade.sendFcmMessageOnSocket(user, chatRoom.getUserIds(), user.getName(), "이미지가 전송되었습니다.", MessageType.PICTURE, user.getProfileImage());
        return chatRepository.save(buildChat(MessageType.PICTURE, chatRoom, user, request.getImageUrl()));
    }

    @Override
    public String removeMessage(String chatId) {
        User user = authenticationFacade.getCurrentUser();
        Chat chat = messageIsMine(chatId, user);
        chatRepository.deleteById(new ObjectId(chatId));
        return chat.getChatRoom().getId().toString();
    }

    @Override
    @Transactional
    public OldChatMessageListResponse getOldChatMessage(String chatRoomId, Pageable pageable) {
        User user = authenticationFacade.getCurrentUser();
        ChatRoom chatRoom = findChatRoomById(chatRoomId);

        mongoTemplate.updateMulti(query(where("chatRoom")),
                new Update().pull("readers", new DBRef("chatRoom", user.getId())),
                Chat.class);

        Page<OldChatMessageResponse> responses = chatRepository.findAllByChatRoomOrderByCreatedAtAsc(chatRoom, pageable)
                .map(chat -> buildResponse(chat, user));

        return new OldChatMessageListResponse(responses.getContent(), responses.getNumberOfElements(), responses.hasNext());
    }

    @Override
    public PinResponse getPinnedChat(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        return new PinResponse(chatRoom.getPinnedChat().getMessage());
    }

    @Override
    public void deletePinnedChat(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);

        chatRoom.setPinnedChat(null);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void pinMessage(ChatPinRequest request, User user) {
        validateChatRoom(request.getChatRoomId(), user);
        Chat chat = chatRepository.findById(new ObjectId(request.getChatId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        ChatRoom chatRoom = chatRoomRepository.findById(new ObjectId(request.getChatRoomId()))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        chatRoom.setPinnedChat(chat);
        chatRoomRepository.save(chatRoom);
    }

    private OldChatMessageResponse buildResponse(Chat chat, User user) {
        String planName = null;
        String endDate = null;
        String startDate = null;
        String planId = null;

        if (chat.getPlanId() != null) {
            Plan plan = customPlanRepository.findById(chat.getPlanId());
            planName = plan.getName();
            endDate = plan.getEndDate().toString();
            startDate = plan.getStartDate().toString();
            planId = plan.getId().toString();
        }

        return OldChatMessageResponse.builder()
                .createdAt(chat.getCreatedAt())
                .id(chat.getId().toString())
                .isMine(user.equals(chat.getSender()))
                .message(chat.getMessage())
                .readerList(CollectionUtils.emptyIfNull(chat.getReceiver()).stream().map(User::getEmail).collect(Collectors.toList()))
                .senderImage(chat.getSender().getProfileImage())
                .senderName(chat.getSender().getName())
                .planName(planName)
                .planId(planId)
                .endDate(endDate)
                .startDate(startDate)
                .type(chat.getMessageType())
                .build();
    }

    private void validateChatRoom(String chatRoomId, User user) {
        if (!chatRoomRepository.isChatRoomMember(chatRoomId, user)) {
            throw NotChatRoomMemberException.EXCEPTION;
        }
    }

    private Chat messageIsMine(String chatId, User user) {
        return chatRepository.findByIdAndSender(new ObjectId(chatId), user)
                .orElseThrow(() -> UserNotMessageOwnerException.EXCEPTION);
    }

    private ChatRoom findChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(new ObjectId(chatRoomId))
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private Chat buildChat(MessageType type, ChatRoom chatRoom, User user, String message) {
        List<User> receivers = chatRoom.getUserIds();
        receivers.remove(user);
        return Chat.builder()
                .receiver(receivers)
                .sender(user)
                .chatRoom(chatRoom)
                .message(message)
                .messageType(type)
                .build();
    }
}
