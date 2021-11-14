package com.projectflow.projectflow.domain.chat.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chat.entity.enums.ChatType;
import com.projectflow.projectflow.domain.chat.exceptions.UserNotMessageOwnerException;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.chat.payload.OldChatMessageListResponse;
import com.projectflow.projectflow.domain.chat.payload.OldChatMessageResponse;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.chatroom.exceptions.NotChatRoomMemberException;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.auth.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthenticationFacade authenticationFacade;
    private final CustomPlanRepository customPlanRepository;

    @Override
    public Chat saveMessage(ChatRequest request, User user) {
        validateChatRoom(request.getChatRoomId(), user);
        ChatRoom chatRoom = findChatRoomById(request.getChatRoomId());
        return chatRepository.save(buildChat(chatRoom, user, request.getMessage()));
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
        return new OldChatMessageListResponse(
                chatRepository.findAllByChatRoomOrderByCreatedAtAsc(chatRoom, pageable)
                        .map(chat -> {
                            CollectionUtils.emptyIfNull(chat.getReceiver()).remove(user);
                            return chat;
                        })
                        .map(chat -> buildResponse(chat, user)).getContent()
        );
    }

    private OldChatMessageResponse buildResponse(Chat chat, User user) {
        String planName = null;
        String endDate = null;
        String startDate = null;

        if(chat.getPlanId() != null) {
            Plan plan = customPlanRepository.findById(chat.getPlanId());
            planName = plan.getName();
            endDate = plan.getEndDate().toString();
            startDate = plan.getStartDate().toString();
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
                .endDate(endDate)
                .startDate(startDate)
                .type(chat.getChatType().getMessageType())
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

    private Chat buildChat(ChatRoom chatRoom, User user, String message) {
        List<User> receivers = chatRoom.getUserIds();
        receivers.remove(user);
        return Chat.builder()
                .receiver(receivers)
                .sender(user)
                .chatRoom(chatRoom)
                .message(message)
                .chatType(ChatType.CHAT)
                .build();
    }
}
